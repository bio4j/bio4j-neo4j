/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.gephi;

import com.ohnosequences.xml.model.gexf.EdgeXML;
import com.ohnosequences.xml.model.gexf.GexfXML;
import com.ohnosequences.xml.model.gexf.GraphXML;
import com.ohnosequences.xml.model.gexf.NodeXML;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GexfToDotExporter {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("This program expects the following parameters: \n"
                    + "1. Input GEXF file name \n"
                    + "2. Output DOT file name");
        } else {


            File inFile = new File(args[0]);
            File outFile = new File(args[1]);

            try {

                BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

                BufferedReader reader = new BufferedReader(new FileReader(inFile));

                String line = null;
                StringBuilder stBuilder = new StringBuilder();
                while((line = reader.readLine()) != null){
                    stBuilder.append(line);
                }                   
                reader.close();
                
                GexfXML gexfXML = new GexfXML(stBuilder.toString());
                GraphXML graphXML = new GraphXML(gexfXML.asJDomElement().getChild(GraphXML.TAG_NAME));
                stBuilder.delete(0, stBuilder.length());
                                
                List<Element> nodes = graphXML.asJDomElement().getChild("nodes").getChildren("node");
                for (Element node : nodes) {
                    NodeXML nodeXML = new NodeXML(node);
                    String nodeId = nodeXML.asJDomElement().getAttributeValue("id");
                    String label = nodeXML.getLabel().replaceAll(" ", "");//taking out white spaces
                    
                    writer.write(nodeId + " " + "[label=" + label + "]\n");
                }
                
                List<Element> edges = graphXML.asJDomElement().getChild("edges").getChildren("edge");
                for (Element edge : edges) {
                    EdgeXML edgeXML = new EdgeXML(edge);
                    writer.write(edgeXML.getSource() + " -> " + edgeXML.getTarget() + "\n");
                }
                
                writer.close();

            } catch (Exception ex) {
                Logger.getLogger(GexfToDotExporter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            System.out.println("Done! :)");


        }
    }
}
