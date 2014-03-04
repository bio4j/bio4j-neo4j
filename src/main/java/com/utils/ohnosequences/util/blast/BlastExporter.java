/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.blast;

import com.ohnosequences.xml.model.BlastOutput;
import com.ohnosequences.xml.model.Iteration;
import com.ohnosequences.xml.model.uniprot.ProteinXML;
import com.ohnosequences.xml.model.ContigXML;
import com.ohnosequences.xml.model.Hit;
import com.ohnosequences.xml.model.Hsp;
import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import com.ohnosequences.xml.api.util.XMLUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class BlastExporter {

    public static final int TRUNCATE_STRING_LENGTH = 30;

    public static void main(String[] args) throws FileNotFoundException, Exception {

        BlastExporter.exportBlastXMLtoIsotigsCoverage(new BufferedReader(new FileReader(new File("PPIN_Coverage.xml"))));

    }
    
    public static String prettyPrintBlast(String blastSt, int indentAmount) throws TransformerConfigurationException, TransformerException{
        return XMLUtil.prettyPrintXML(blastSt, indentAmount);
    }

    public static String exportBlastXMLtoIsotigsCoverage(BufferedReader blastOutput) throws Exception {


        StringBuilder stBuilder = new StringBuilder();
        stBuilder.append("<proteins>\n");

        String line = null;
        StringBuilder iterationStBuilder = new StringBuilder();

        HashMap<String, ArrayList<ContigXML>> proteinContigs = new HashMap<String, ArrayList<ContigXML>>();
        //Protein info map
        HashMap<String, ProteinXML> proteinInfoMap = new HashMap<String, ProteinXML>();

        do {
            line = blastOutput.readLine();
        } while (!line.trim().startsWith("<" + Iteration.TAG_NAME + ">"));

        while (line != null) {

            iterationStBuilder.append(line);
            line = blastOutput.readLine();
            while (line != null && !line.trim().startsWith("<" + Iteration.TAG_NAME + ">")
                    && !line.trim().startsWith("</BlastOutput_iterations>")) {
                iterationStBuilder.append(line);
                line = blastOutput.readLine();
            }

            if (!line.trim().startsWith("</BlastOutput_iterations>")) {

                XMLElement entryXMLElem = new XMLElement(iterationStBuilder.toString());

                iterationStBuilder.delete(0, iterationStBuilder.length());
                Iteration iteration = new Iteration(entryXMLElem.asJDomElement());
                parseIteration(iteration, proteinContigs, proteinInfoMap);
            }

        }

        blastOutput.close();

        parseAndExportProteins(proteinContigs, proteinInfoMap, stBuilder);

        stBuilder.append("</proteins>\n");

        return stBuilder.toString();



    }

    public static String exportBlastXMLtoIsotigsCoverage(BlastOutput blastOutput) throws XMLElementException {

        StringBuilder stBuilder = new StringBuilder();

        stBuilder.append("<proteins>\n");

        ArrayList<Iteration> iterations = blastOutput.getBlastOutputIterations();
        //Map with isotigs/contigs per protein
        HashMap<String, ArrayList<ContigXML>> proteinContigs = new HashMap<String, ArrayList<ContigXML>>();
        //Protein info map
        HashMap<String, ProteinXML> proteinInfoMap = new HashMap<String, ProteinXML>();

        for (Iteration iteration : iterations) {
            parseIteration(iteration, proteinContigs, proteinInfoMap);
        }

        parseAndExportProteins(proteinContigs, proteinInfoMap, stBuilder);

        stBuilder.append("</proteins>\n");


        return stBuilder.toString();
    }

    private static void parseAndExportProteins(HashMap<String, ArrayList<ContigXML>> proteinContigs,
            HashMap<String, ProteinXML> proteinInfoMap,
            StringBuilder stBuilder) {

        System.out.println("holaaa");

        for (String proteinKey : proteinInfoMap.keySet()) {
            //---calculating coverage and creating output xml----

            ProteinXML proteinXML = proteinInfoMap.get(proteinKey);

            ArrayList<ContigXML> contigs = proteinContigs.get(proteinKey);
            for (ContigXML contigXML : contigs) {
                proteinXML.addChild(contigXML);
            }

            proteinXML.setNumberOfIsotigs(contigs.size());

            int coveredPositions = 0;
            for (int i = 1; i <= proteinXML.getLength(); i++) {
                for (ContigXML contigXML : contigs) {
                    if (i >= contigXML.getBegin() && i <= contigXML.getEnd()) {
                        coveredPositions++;
                        break;
                    }
                }
            }

            proteinXML.setProteinCoverageAbsolute(coveredPositions);
            proteinXML.setProteinCoveragePercentage((coveredPositions * 100.0) / proteinXML.getLength());

            stBuilder.append((proteinXML.toString() + "\n"));

        }

    }

    private static void parseIteration(Iteration iteration,
            HashMap<String, ArrayList<ContigXML>> proteinContigs,
            HashMap<String, ProteinXML> proteinInfoMap) throws XMLElementException {

        //String contigNameSt = iteration.getUniprotIdFromQueryDef();
        String contigNameSt = iteration.getQueryDef();

        //---In the case where query def is too long it is truncated
        if(contigNameSt.length() > TRUNCATE_STRING_LENGTH){
            contigNameSt = contigNameSt.substring(0, TRUNCATE_STRING_LENGTH);
        }
        ContigXML contig = new ContigXML();
        contig.setId(contigNameSt);

        ArrayList<Hit> hits = iteration.getIterationHits();
        for (Hit hit : hits) {
            String proteinIdSt = hit.getHitDef().split("\\|")[1];

            ArrayList<ContigXML> contigsArray = proteinContigs.get(proteinIdSt);


            if (contigsArray == null) {
                //Creating contigs array
                contigsArray = new ArrayList<ContigXML>();
                proteinContigs.put(proteinIdSt, contigsArray);
                //Creating protein info
                ProteinXML proteinXML = new ProteinXML();
                proteinXML.setId(proteinIdSt);
                proteinXML.setLength(hit.getHitLen());
                proteinInfoMap.put(proteinIdSt, proteinXML);
            }

            ArrayList<Hsp> hsps = hit.getHitHsps();
            int hspMinHitFrom = 1000000000;
            int hspMaxHitTo = -1;

            //---Figuring out the isotig/contig positions
            for (Hsp hsp : hsps) {
                int hspFrom = hsp.getHitFrom();
                int hspTo = hsp.getHitTo();
//                            System.out.println("hsp = " + hsp);
//                            System.out.println("hsp.getHitFrame() = " + hsp.getHitFrame());
//                            if (hsp.getQueryFrame() < 0) {
//                                hspFrom = hsp.getHitTo();
//                                hspTo = hsp.getHitFrom();
//                            }

                if (hspFrom < hspMinHitFrom) {
                    hspMinHitFrom = hspFrom;
                }
                if (hspTo > hspMaxHitTo) {
                    hspMaxHitTo = hspTo;
                }

                //adding hsps to contig
                hsp.detach();
                contig.addHsp(hsp);
            }
            //-------------------

            contig.setBegin(hspMinHitFrom);
            contig.setEnd(hspMaxHitTo);
            if (contig.getBegin() > contig.getEnd()) {
                contig.setBegin(hspMaxHitTo);
                contig.setEnd(hspMinHitFrom);
            }

            contigsArray.add(contig);

        }

    }
}
