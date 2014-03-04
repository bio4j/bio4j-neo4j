/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.era7.bioinfo.bioinfoutil.go;

import com.era7.bioinfoxml.go.GoAnnotationXML;
import com.era7.bioinfoxml.go.GoTermXML;
import com.era7.bioinfoxml.uniprot.ProteinXML;
import com.era7.era7xmlapi.model.XMLElementException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GOExporter {

    public static String SEPARATOR = "\t";

    public static List<File> calculateFrequenciesAndExportToFiles(GoAnnotationXML goAnnotationXML,
            String baseFileName) throws IOException, XMLElementException {


        File molFuncFile = new File(baseFileName + "MolFunc" + ".txt");
        File molFuncFreqFile = new File(baseFileName + "MolFuncFreq" + ".txt");
        File cellCompFile = new File(baseFileName + "CellComp" + ".txt");
        File cellCompFreqFile = new File(baseFileName + "CellCompFreq" + ".txt");
        File bioProcFile = new File(baseFileName + "BioProc" + ".txt");
        File bioProcFreqFile = new File(baseFileName + "BioProcFreq" + ".txt");
        
        List<File> files = new LinkedList<File>();
        files.add(molFuncFile);
        files.add(molFuncFreqFile);
        files.add(cellCompFile);
        files.add(cellCompFreqFile);
        files.add(bioProcFile);
        files.add(bioProcFreqFile);

        BufferedWriter molFuncBuff = new BufferedWriter(new FileWriter(molFuncFile));
        BufferedWriter molFuncFreqBuff = new BufferedWriter(new FileWriter(molFuncFreqFile));
        BufferedWriter cellCompBuff = new BufferedWriter(new FileWriter(cellCompFile));
        BufferedWriter cellCompFreqBuff = new BufferedWriter(new FileWriter(cellCompFreqFile));
        BufferedWriter bioProcBuff = new BufferedWriter(new FileWriter(bioProcFile));
        BufferedWriter bioProcFreqBuff = new BufferedWriter(new FileWriter(bioProcFreqFile));


        //Writing headers
        molFuncBuff.write("Uniprot template protein" + SEPARATOR + "GO ID" + SEPARATOR + "Molecular function GO term" + "\n");
        molFuncFreqBuff.write("GO ID" + SEPARATOR + "Molecular function GO term" + SEPARATOR + "Number of proteins" + SEPARATOR + "Proteins" + "\n");

        cellCompBuff.write("Uniprot template protein" + SEPARATOR + "GO ID" + SEPARATOR + "Cellular component GO term" + "\n");
        cellCompFreqBuff.write("GO ID" + SEPARATOR + "Cellular component GO term" + SEPARATOR + "Number of proteins" + SEPARATOR + "Proteins" + "\n");

        bioProcBuff.write("Uniprot template protein" + SEPARATOR + "GO ID" + SEPARATOR + "Biological process GO term" + "\n");
        bioProcFreqBuff.write("GO ID" + SEPARATOR + "Biological process GO term" + SEPARATOR + "Number of proteins" + SEPARATOR + "Proteins" + "\n");

        //Now it's time to group the terms by their aspect/sub-ontology
        List<GoTermXML> goAnnotators = goAnnotationXML.getAnnotatorGoTerms();

        //System.out.println("goAnnotators.size() = " + goAnnotators.size());

        TreeSet<GoTermXML> molFuncAnnotators = new TreeSet<GoTermXML>();
        TreeSet<GoTermXML> bioProcAnnotators = new TreeSet<GoTermXML>();
        TreeSet<GoTermXML> cellCompAnnotators = new TreeSet<GoTermXML>();

        //int counter = 0;


        for (GoTermXML goTerm : goAnnotators) {

            if (goTerm.getAspect().equals(GoTermXML.ASPECT_COMPONENT)) {
                cellCompAnnotators.add(goTerm);
            } else if (goTerm.getAspect().equals(GoTermXML.ASPECT_FUNCTION)) {
                molFuncAnnotators.add(goTerm);
            } else if (goTerm.getAspect().equals(GoTermXML.ASPECT_PROCESS)) {
                bioProcAnnotators.add(goTerm);
            }
        }

        System.out.println("cellCompAnnotators.size() = " + cellCompAnnotators.size());
        System.out.println("molFuncAnnotators.size() = " + molFuncAnnotators.size());
        System.out.println("bioProcAnnotators.size() = " + bioProcAnnotators.size());

        HashMap<String, LinkedList<String>> termProteinAssociationsMap = new HashMap<String, LinkedList<String>>();
        for (GoTermXML term : goAnnotators) {
            termProteinAssociationsMap.put(term.getId(), new LinkedList<String>());
        }

        List<Element> proteins = goAnnotationXML.getProteinAnnotations().getChildren(ProteinXML.TAG_NAME);
        for (Element element : proteins) {

            ProteinXML tempProtein = new ProteinXML(element);
            List<GoTermXML> molFuncGos = tempProtein.getMolecularFunctionGoTerms();
            if (molFuncGos != null) {
                for (GoTermXML goTerm : molFuncGos) {
                    molFuncBuff.write(tempProtein.getId() + SEPARATOR + goTerm.getId() + SEPARATOR + goTerm.getGoName() + "\n");
                    termProteinAssociationsMap.get(goTerm.getId()).add(tempProtein.getId());
                }
            }

            List<GoTermXML> bioProcGos = tempProtein.getBiologicalProcessGoTerms();
            if (bioProcGos != null) {
                for (GoTermXML goTerm : bioProcGos) {
                    bioProcBuff.write(tempProtein.getId() + SEPARATOR + goTerm.getId() + SEPARATOR + goTerm.getGoName() + "\n");
                    termProteinAssociationsMap.get(goTerm.getId()).add(tempProtein.getId());
                }
            }

            List<GoTermXML> cellCompGos = tempProtein.getCellularComponentGoTerms();
            if (cellCompGos != null) {
                for (GoTermXML goTerm : cellCompGos) {
                    cellCompBuff.write(tempProtein.getId() + SEPARATOR + goTerm.getId() + SEPARATOR + goTerm.getGoName() + "\n");
                    termProteinAssociationsMap.get(goTerm.getId()).add(tempProtein.getId());
                }
            }
        }

        //Writing stuff in the files
        for (GoTermXML goTerm : cellCompAnnotators) {
            String protSt = "";
            for (String protAcc : termProteinAssociationsMap.get(goTerm.getId())) {
                protSt += protAcc + ",";
            }
            protSt = protSt.substring(0, protSt.length() - 1);

            cellCompFreqBuff.write(goTerm.getId() + SEPARATOR + goTerm.getGoName() + SEPARATOR + goTerm.getAnnotationsCount() + SEPARATOR + protSt + "\n");
        }
        for (GoTermXML goTerm : molFuncAnnotators) {
            String protSt = "";
            for (String protAcc : termProteinAssociationsMap.get(goTerm.getId())) {
                protSt += protAcc + ",";
            }
            protSt = protSt.substring(0, protSt.length() - 1);
            molFuncFreqBuff.write(goTerm.getId() + SEPARATOR + goTerm.getGoName() + SEPARATOR + goTerm.getAnnotationsCount() + SEPARATOR + protSt + "\n");
        }
        for (GoTermXML goTerm : bioProcAnnotators) {
            String protSt = "";
            for (String protAcc : termProteinAssociationsMap.get(goTerm.getId())) {
                protSt += protAcc + ",";
            }
            protSt = protSt.substring(0, protSt.length() - 1);
            bioProcFreqBuff.write(goTerm.getId() + SEPARATOR + goTerm.getGoName() + SEPARATOR + goTerm.getAnnotationsCount() + SEPARATOR + protSt + "\n");
        }


        molFuncBuff.close();
        molFuncFreqBuff.close();
        cellCompBuff.close();
        cellCompFreqBuff.close();
        bioProcBuff.close();
        bioProcFreqBuff.close();

        
        return files;

    }
}
