/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ohnosequences.util;

/**
 *
 * @author ppareja
 */
public class BitOperations {


    public static int convertToEquivalentInt(boolean[] bits){
        int counter = 0;
        for (int i = 0; i < bits.length; i++) {
            boolean b = bits[bits.length-1-i];
            if(b){
                counter += Math.pow(2, i);
            }
        }
        return counter;
    }
    


}
