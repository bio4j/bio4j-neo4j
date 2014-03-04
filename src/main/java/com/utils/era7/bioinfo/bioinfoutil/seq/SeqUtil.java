/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.era7.bioinfo.bioinfoutil.seq;

import java.io.*;
import java.util.HashMap;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class SeqUtil {

    private static HashMap<String, String> translationMap = null;

//    public static void main(String args[]) throws Exception{
//        
//        String seq = "TTATTATCTAGTTTTGAAGGAACAAGAAAATTGTTCTTGAAAAATCCATAAAAAGATATTATAATAATAAATGTCTTTGATAAATGAATAATGTCTGGTAATGATGGCGAAGAGGCCACACCCGTTCCCATTCCGAACACGGAAGTTAAGCTCTTCAGCGCCGATGGTAGTTGGGGGTCTCCCCCTGTGAGAGTAGGACATTGCCAGGCAGATTCACTTTTAATTTTTTtCTATCGAAGAGTTTTAGGAAACATTATACCATTCCGCAGTAGCTCAGTGGTAGAGCATTCGGCTGTTAACCGAACGGTCGTAGGTTCGAGTCCTACCTGCGGAGCCATGCTTCCATAGCTCAGTAGGTAGAGCACTTCCATGGTAAGGAAGAGGTCAGCGGTTCGAATCCGCTTGGGAGCTTACTAGTTTACTTGGAATTGAAATGAAAATTAGTTAGGCCCCTTGGTCAAGCGGTTAAGACACCGCCCTTTCACGGCGGTAACACGGGTTCGAATCCCGTAGGGGTCACCACTTTATATATGGAGGATTAGCTCAGCTGGGAGAGCATCTGCCTTACAAGCAGAGGGTCGGCGGTTCGATCCCGTCATCCTCCACCATGTTTTATTTTACACTTGCCGGTGTAGCTCAACTGGTAGAGCAACTGACTTGTAATCAGTAGGTTGGGGGTTCAAGTCCTCTTGCCGGCACTGTTTTTCTGGAGGGGTAGCGAAGTGGCTAAACGCGGCGGACTGTAAATCCGCTCCTTAGGGTTCGGCAGTTCGAATCTGCCCCCCTCCACCATTTATAAAATCAATTAGTGGACAGTTATATATTTTCTCTTTAAGGGAA";
//        
//        System.out.println("N found in complementary inverted: " + getComplementaryInverted(seq).indexOf("N"));
//        
//        System.out.println("Seq == complInv(complInv(Seq)) ?? --> " + seq.equals(getComplementaryInverted(getComplementaryInverted(seq))));
//        
//        String translatedSeq = translateDNAtoProtein(seq.toLowerCase(), new File("genetic_code.txt"));
//        System.out.println("Translation: \n" + translatedSeq);
//        
//        System.out.println("nulls found: " + (translatedSeq.indexOf("null")>= 0));
//    
//    }
    public static String getComplementarySequence(String sequence) {
        StringBuilder stBuilder = new StringBuilder();
        for (int i = 0; i < sequence.length(); i++) {
            char currentChar = sequence.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (currentChar == 'A') {
                    stBuilder.append('T');
                } else if (currentChar == 'T') {
                    stBuilder.append('A');
                } else if (currentChar == 'C') {
                    stBuilder.append('G');
                } else if (currentChar == 'G') {
                    stBuilder.append('C');
                } else {
                    stBuilder.append('N');
                }
            } else {
                if (currentChar == 'a') {
                    stBuilder.append('t');
                } else if (currentChar == 't') {
                    stBuilder.append('a');
                } else if (currentChar == 'c') {
                    stBuilder.append('g');
                } else if (currentChar == 'g') {
                    stBuilder.append('c');
                } else {
                    stBuilder.append('n');
                }
            }
        }
        return stBuilder.toString();
    }

    public static String getComplementaryInverted(String sequence) {

        StringBuilder result = new StringBuilder();

        for (int i = sequence.length() - 1; i >= 0; i--) {

            char currentChar = sequence.charAt(i);
            char valueToAppend;

            if (Character.isUpperCase(currentChar)) {

                if (currentChar == 'A') {
                    valueToAppend = 'T';
                } else if (currentChar == 'T') {
                    valueToAppend = 'A';
                } else if (currentChar == 'C') {
                    valueToAppend = 'G';
                } else if (currentChar == 'G') {
                    valueToAppend = 'C';
                } else {
                    valueToAppend = 'N';
                }

            } else {

                if (currentChar == 'a') {
                    valueToAppend = 't';
                } else if (currentChar == 't') {
                    valueToAppend = 'a';
                } else if (currentChar == 'c') {
                    valueToAppend = 'g';
                } else if (currentChar == 'g') {
                    valueToAppend = 'c';
                } else {
                    valueToAppend = 'n';
                }

            }

            result.append(valueToAppend);

        }


        return result.toString();
    }

    public static String translateDNAtoProtein(String sequence, File geneticCodeFile) throws FileNotFoundException, IOException {

        initTranslationMap(geneticCodeFile);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i <= sequence.length() - 3; i += 3) {
            String tempSt = translationMap.get(sequence.substring(i, i + 3).toUpperCase());
            if (tempSt == null) {
                tempSt = "X";
            }
            result.append(tempSt);
        }

        return result.toString();

    }

    private synchronized static void initTranslationMap(File geneticCodeFile) throws FileNotFoundException, IOException {
        if (translationMap == null) {

            translationMap = new HashMap<String, String>();

            BufferedReader reader = new BufferedReader(new FileReader(geneticCodeFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(" ");
                translationMap.put(columns[0], columns[1]);
            }
            reader.close();
        }
    }
}
