/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ohnosequences.util.statistics;

/**
 *
 * @author ppareja
 */
public class StatisticalValues {


    /**
     * Para valores discretos, el valor del array en un indice
     * determinado describe el numero de ocurrencias del valor = 'indice' en la muestra
     * @param values
     * @return
     */
    public static double mean(double[] values){
        
        double total = 0;
        double sampleLength = 0;

        for (int i = 0; i < values.length; i++) {
            sampleLength += values[i];
            total += (i*values[i]);
        }

        return total/sampleLength;
    }

    /**
     * Para valores discretos, el valor del array en un indice
     * determinado describe el numero de ocurrencias del valor = 'indice' en la muestra
     * @param values
     * @return
     */
    public static double mode(double[] values){

        double mode = 0;
        double ocurrences = 0;

        for (int i = 0; i < values.length; i++) {
            if(values[i] > ocurrences){
                mode = i;
                ocurrences = values[i];
            }
        }

        return mode;
    }

    /**
     * Para valores discretos, el valor del array en un indice
     * determinado describe el numero de ocurrencias del valor = 'indice' en la muestra
     * @param values
     * @return
     */
    public static double medianHandMade(double[] values){
        
        //primero calculo la longitud de la muestra
        
        int total = 0;
        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }

        //System.out.println("total = " + total);

        int half = total/2;
        int currentElem = 0;

        boolean evenLength = (total%2 == 0);

        //System.out.println("evenLength = " + evenLength);
        

        for (int i = 0; i < values.length; i++) {
            currentElem += values[i];
            
            if(currentElem > half){
                if(evenLength){
                    int lastOcurrence = i;
                    boolean found = false;
                    for(int j=i-1;j>=0 && !found;j--){
                        if(values[j] != 0){
                            lastOcurrence = j;
                            found = true;
                        }
                    }
                    return ((i+lastOcurrence)/2.0);
                }else{
                    return i;
                }
            }
        }

        return 0;
    }

    /**
     * Para valores discretos, el valor del array en un indice
     * determinado describe el numero de ocurrencias del valor = 'indice' en la muestra
     * @param values
     * @return
     */
    public static double standardDeviationHandMade(double[] values){

        //Primero calculo la media
        double mean = mean(values);

        //Ahora calculo la desviacion tipica
        double result = 0;
        double total = 0;
        double ocurrences = 0;
        double diff = 0;

        for (int i = 0; i < values.length; i++) {
            ocurrences = values[i];
            //System.out.println("i = " + i);
            //.out.println("ocurrences = " + ocurrences);
            total += ocurrences;
            //System.out.println("total = " + total);
            diff = (i-mean) * (i-mean);
            //System.out.println("diff = " + diff);
            result += (ocurrences * diff);
            //System.out.println("result = " + result);
        }

        result = Math.sqrt(result/total);

        return result;
    }


    /**
     * Transforma los datos comprimidos a un array de ocurrencias
     * Para valores discretos, el valor del array en un indice
     * determinado describe el numero de ocurrencias del valor = 'indice' en la muestra
     * @param values
     * @return
     */
    public static double[] getValuesAsDoubleArray(double[] values){

        int total = 0;

        for (int i = 0; i < values.length; i++) {
            total += values[i];
        }

        double[] valuesArray = new double[total];

        int counter = 0;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i]; j++) {
                valuesArray[counter] = i;
                counter++;
            }
        }

        return valuesArray;
    }


    public static void main(String[] args){
        double[] values = {3,1,1,1,1,2,0,0,0};
        System.out.println("Median: " + medianHandMade(values));
        System.out.println("Mean: " + mean(values));
        System.out.println("Mode: " + mode(values));
        System.out.println("Standard deviation handmade: " + standardDeviationHandMade(values));
        double[] aa =  getValuesAsDoubleArray(values);

        for (int i = 0; i < aa.length; i++) {
            double d = aa[i];
            System.out.println(d + ",");
        }
    }

}
