/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoutil;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class CodonUtil {

    public static final String[] START_CODONS = {"ATG","CTG","GTG","TTG"};
    public static final String[] STOP_CODONS = {"TAA","TAG","TGA"};

    public static final String[] VIRUS_START_CODONS = {"ATG"};


    public static boolean isStartCodon(String sequence){

        if(sequence.length() < 3){
            return false;
        }else{
            String temp = sequence.substring(0, 3).toUpperCase();
            for(String st : START_CODONS){
                if(st.equals(temp)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isStartCodonVirus(String sequence){

        if(sequence.length() < 3){
            return false;
        }else{
            String temp = sequence.substring(0, 3).toUpperCase();
            for(String st : VIRUS_START_CODONS){
                if(st.equals(temp)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isStopCodon(String sequence){

        if(sequence.length() < 3){
            return false;
        }else{
            String temp = sequence.substring(0, 3).toUpperCase();
            for(String st : STOP_CODONS){
                if(st.equals(temp)){
                    return true;
                }
            }
        }

        return false;
    }

}
