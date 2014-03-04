/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.era7.bioinfo.bioinfoutil.file;

import com.era7.bioinfoxml.genome.feature.ORF;
import com.era7.bioinfoxml.genome.feature.RNA;
import com.era7.bioinfoxml.genome.GenomeElement;
import com.era7.era7xmlapi.model.XMLElementException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ppareja
 */
public class GenomeFilesParser {

    public static List<ORF> parsePttFile(InputStream in,
            boolean closeInputStream)
            throws IOException, XMLElementException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        ArrayList<ORF> orfs = new ArrayList<ORF>();

        String tempSt = null;
        boolean locationFound = false;

        while ((tempSt = reader.readLine()) != null) {
            if (locationFound) {
                ORF tempORF = new ORF();

                String[] columns = tempSt.split("\t");
                String locationSt = columns[0];
                String[] positions = locationSt.split("\\.\\.");
                int tempBegin = Integer.parseInt(positions[0]);
                int tempEnd = Integer.parseInt(positions[1]);
                tempORF.setBegin(tempBegin);
                tempORF.setEnd(tempEnd);

                tempORF.setStrand(columns[1]);

                tempORF.setLength((Integer.parseInt(columns[2]) + 1) * 3);

                tempORF.setFeatureName(columns[8]);
                //tempORF.setGene(columns[4]);
                //tempORF.setSynonym(columns[5]);

                orfs.add(tempORF);

            } else {
                if (tempSt.split("\t")[0].equals("Location")) {
                    locationFound = true;
                }
            }
        }

        reader.close();
        if (closeInputStream) {
            in.close();
        }

        return orfs;
    }

    public static List<RNA> parseRntFile(InputStream in,
            boolean closeInputStream)
            throws IOException, XMLElementException {

        ArrayList<RNA> rnas = new ArrayList<RNA>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        boolean locationFound = false;
        String tempSt = null;

        while ((tempSt = reader.readLine()) != null) {
            if (locationFound) {
                RNA tempRNA = new RNA();

                String[] columns = tempSt.split("\t");
                String locationSt = columns[0];
                int tempBegin = Integer.parseInt(locationSt.split("\\.\\.")[0]);
                int tempEnd = Integer.parseInt(locationSt.split("\\.\\.")[1]);
                tempRNA.setBegin(tempBegin);
                tempRNA.setEnd(tempEnd);

                tempRNA.setLength(Integer.parseInt(columns[2]));

                tempRNA.setStrand(columns[1]);
                tempRNA.setFeatureName(columns[8]);
                //tempRNA.setGene(columns[4]);
                //tempRNA.setSynonym(columns[5]);

                rnas.add(tempRNA);

            } else {
                if (tempSt.split("\t")[0].equals("Location")) {
                    locationFound = true;
                }
            }
        }

        reader.close();
        if (closeInputStream) {
            in.close();
        }

        return rnas;
    }

    public static GenomeElement parseFnaFile(InputStream in,
            boolean closeInputStream)
            throws IOException {

        GenomeElement genomeElement = new GenomeElement();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder stringBuilder = new StringBuilder();
        String tempSt = null;

        while ((tempSt = reader.readLine()) != null) {
            if (tempSt.charAt(0) != '>') {
                stringBuilder.append(tempSt.trim());
            }
        }
        String sequence = stringBuilder.toString();
        genomeElement.setSequence(sequence);
        genomeElement.setLength(sequence.length());

        reader.close();
        if (closeInputStream) {
            in.close();
        }

        return genomeElement;
    }
}
