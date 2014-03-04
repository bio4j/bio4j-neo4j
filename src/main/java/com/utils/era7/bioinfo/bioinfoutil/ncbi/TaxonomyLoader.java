/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.era7.bioinfo.bioinfoutil.ncbi;

import com.era7.bioinfoxml.ncbi.NCBITaxonomyNodeXML;
import java.io.*;
import java.util.HashMap;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class TaxonomyLoader {
    

    private HashMap<Integer, NCBITaxonomyNodeXML> nodesMap = new HashMap<Integer, NCBITaxonomyNodeXML>();

    public TaxonomyLoader(File nodesDumpFile, File namesDumpFile) throws FileNotFoundException, IOException {


        BufferedReader reader = new BufferedReader(new FileReader(nodesDumpFile));
        String line = null;

        System.out.println("reading nodes file...");
        int nodesCounter = 0;
        //---------------reading nodes file------------------
        while ((line = reader.readLine()) != null) {

            if (line.trim().length() > 0) {
                
                NCBITaxonomyNodeXML node = new NCBITaxonomyNodeXML();

                String[] columns = line.split("\\|");
                
                node.setTaxId(Integer.parseInt(columns[0].trim()));
                node.setParentTaxId(columns[1].trim());
                node.setRank(columns[2].trim());
                node.setEmblCode(columns[3].trim());
                
                nodesMap.put(node.getTaxId(), node);
                nodesCounter++;
                
                if(nodesCounter % 10000 == 0){
                    System.out.println(nodesCounter + " nodes stored...");
                }
                
            }

        }

        reader.close();
        System.out.println("done!");
        
        
        System.out.println("reading names file...");
        //------------reading names file-----------------
        reader = new BufferedReader(new FileReader(namesDumpFile));
        while ((line = reader.readLine()) != null) {

            String[] columns = line.split("\\|");
            
            if(columns[columns.length - 1].trim().equals("scientific name")){
                Integer taxId = Integer.parseInt(columns[0].trim());
                String nameSt = columns[1].trim();
                nodesMap.get(taxId).setScientificName(nameSt);
            }

        }
        reader.close();
        
        System.out.println("done!");


    }
    
    public HashMap<Integer, NCBITaxonomyNodeXML> getNodesMap(){
        return nodesMap;
    }
}
