/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.fasta;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class FastaUtil {


    public static String formatSequenceWithFastaFormat(String seq, int lineLength){

        StringBuilder stBuilder = new StringBuilder();

        for (int counter = 0; counter < seq.length(); counter += lineLength) {
            if (counter + lineLength > seq.length()) {
                stBuilder.append((seq.substring(counter, seq.length()) + "\n"));
                //outBuff.write(seq.substring(counter, seq.length()) + "\n");
            } else {
                stBuilder.append((seq.substring(counter, counter + lineLength) + "\n"));
                //outBuff.write(seq.substring(counter, counter + lineLength) + "\n");
            }
        }

        return stBuilder.toString();
    }

    public static void writeSequenceToFileInFastaFormat(String seq,
                                int lineLength,
                                BufferedWriter outBuff) throws IOException{

        for (int counter = 0; counter < seq.length(); counter += lineLength) {
            if (counter + lineLength > seq.length()) {
                outBuff.write(seq.substring(counter, seq.length()) + "\n");
            } else {
                outBuff.write(seq.substring(counter, counter + lineLength) + "\n");
            }
        }

    }
}
