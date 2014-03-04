/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.blast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class BlastSubset {
 
    
    public static void getBlastSubset(double percentage, File blastFile, File resultFile) throws IOException{
        
        double filterValue = percentage / 100.0;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));
        BufferedReader reader = new BufferedReader(new FileReader(blastFile));
        String line = null;
        
        boolean writeLine = true;
        int iterationCounter = 0;
        int selectedIterationsCounter = 0;
        
        while((line = reader.readLine()) != null){
            
            if(line.trim().startsWith("<Iteration>")){  
                
                writeLine = false;
                iterationCounter++;
                if( Math.random() < filterValue ){
                    writeLine = true;
                    selectedIterationsCounter++;
                }
                
            }
            
            if(writeLine){
                writer.write(line + "\n");
            }
            
            
            if(line.trim().startsWith("</Iteration>")){
                writeLine = true;
            }
            
            
            if(iterationCounter % 10000 == 0){
                System.out.println("selectedIterationsCounter = " + selectedIterationsCounter);
                System.out.println("iterationCounter = " + iterationCounter);
            }
            
        }
        
        reader.close();             
        writer.close();
        
        System.out.println("Closing readers...");
        
        System.out.println("Done! :)");
        
        
    }
    
    
}
