/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.gephi;

import com.ohnosequences.xml.model.uniprot.ProteinXML;
import com.ohnosequences.xml.model.gexf.AttValueXML;
import com.ohnosequences.xml.model.gexf.AttValuesXML;
import com.ohnosequences.xml.model.gexf.AttributeXML;
import com.ohnosequences.xml.model.gexf.AttributesXML;
import com.ohnosequences.xml.model.gexf.EdgeXML;
import com.ohnosequences.xml.model.gexf.GexfXML;
import com.ohnosequences.xml.model.gexf.GraphXML;
import com.ohnosequences.xml.model.gexf.NodeXML;
import com.ohnosequences.xml.model.gexf.viz.VizColorXML;
import com.ohnosequences.xml.model.gexf.viz.VizPositionXML;
import com.ohnosequences.xml.model.gexf.viz.VizSizeXML;
import com.ohnosequences.xml.model.go.GoAnnotationXML;
import com.ohnosequences.xml.model.go.GoTermXML;
import com.ohnosequences.xml.api.model.XMLElementException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GephiExporter {

    public static double DEFAULT_GO_SIZE = 5.0;
    public static double DEFAULT_PROTEIN_SIZE = 15.0;
    public static final String ALL_SUB_ONTOLOGIES = "all";
    public static final String MOLECULAR_FUNCTION_SUB_ONTOLOGY = GoTermXML.ASPECT_FUNCTION;
    public static final String BIOLOGICAL_PROCESS_SUB_ONTOLOGY = GoTermXML.ASPECT_PROCESS;
    public static final String CELLULAR_COMPONENT_SUB_ONTOLOGY = GoTermXML.ASPECT_COMPONENT;

    public static String exportGoAnnotationToGexf(GoAnnotationXML goAnnotationXML,
            VizColorXML proteinColor,
            VizColorXML goColor,
            Boolean proportionalSize,
            Boolean proteinsWithoutConnectionsIncluded,
            String subOntologyIncluded) throws XMLElementException {


        StringBuilder stBuilder = new StringBuilder();

        StringBuilder nodesXMLStBuilder = new StringBuilder("<nodes>\n");
        StringBuilder edgesXMLStBuilder = new StringBuilder("<edges>\n");

        int edgesIdCounter = 1;

        stBuilder.append("<" + GexfXML.TAG_NAME + ">\n");
        stBuilder.append("<" + GraphXML.TAG_NAME + " defaultedgetype=\"directed\">\n");

        AttributesXML attributesXML = new AttributesXML();
        attributesXML.setClass(AttributesXML.NODE_CLASS);
        AttributeXML idAttributeXML = new AttributeXML();
        idAttributeXML.setId("0");
        idAttributeXML.setTitle("ID");
        idAttributeXML.setType("string");
        attributesXML.addAttribute(idAttributeXML);
        AttributeXML nameAttributeXML = new AttributeXML();
        nameAttributeXML.setId("1");
        nameAttributeXML.setTitle("Name");
        nameAttributeXML.setType("string");
        attributesXML.addAttribute(nameAttributeXML);
        AttributeXML nodeTypeAttributeXML = new AttributeXML();
        nodeTypeAttributeXML.setId("2");
        nodeTypeAttributeXML.setTitle("Node type");
        nodeTypeAttributeXML.setType("string");
        attributesXML.addAttribute(nodeTypeAttributeXML);

        stBuilder.append((attributesXML.toString() + "\n"));


        List<GoTermXML> goTerms = goAnnotationXML.getAnnotatorGoTerms();
        Element proteinAnnotations = goAnnotationXML.getProteinAnnotations();
        List<Element> proteins = proteinAnnotations.getChildren(ProteinXML.TAG_NAME);


        //-----go terms-------------
        for (GoTermXML goTerm : goTerms) {

            boolean includeTerm = false;
            String termAspect = goTerm.getAspect();

            if (subOntologyIncluded.equals(GephiExporter.ALL_SUB_ONTOLOGIES)
                    || termAspect.equals(subOntologyIncluded)) {
                includeTerm = true;
            }

            if (includeTerm) {

                NodeXML nodeXML = new NodeXML();
                nodeXML.setId(goTerm.getId());
                nodeXML.setLabel(goTerm.getGoName());
                nodeXML.setColor(new VizColorXML((Element) goColor.asJDomElement().clone()));
                //nodeXML.setSize(new VizSizeXML((Element) goSize.asJDomElement().clone()));

                //---------size---------------------
                if (proportionalSize) {
                    nodeXML.setSize(new VizSizeXML(goTerm.getAnnotationsCount() * 5.0));
                } else {
                    nodeXML.setSize(new VizSizeXML(DEFAULT_GO_SIZE));
                }

                //---------position--------------------
                nodeXML.setPosition(new VizPositionXML(0, 0, 0));

                AttValuesXML attValuesXML = new AttValuesXML();
                AttValueXML nameAttValue = new AttValueXML();
                nameAttValue.setFor(1);
                nameAttValue.setValue(goTerm.getGoName());
                attValuesXML.addAttValue(nameAttValue);
                AttValueXML nodeTypeAttValue = new AttValueXML();
                nodeTypeAttValue.setFor(2);
                nodeTypeAttValue.setValue("GOTerm");
                attValuesXML.addAttValue(nodeTypeAttValue);
                nodeXML.setAttvalues(attValuesXML);


                nodesXMLStBuilder.append((nodeXML.toString() + "\n"));

            }



        }

        //-----------proteins-------------
        for (Element protElem : proteins) {

            ProteinXML proteinXML = new ProteinXML(protElem);
            NodeXML nodeXML = new NodeXML();
            nodeXML.setId(proteinXML.getId());
            nodeXML.setLabel(proteinXML.getId());
            nodeXML.setColor(new VizColorXML((Element) proteinColor.asJDomElement().clone()));
            nodeXML.setSize(new VizSizeXML(DEFAULT_PROTEIN_SIZE));
            //---------position--------------------
            nodeXML.setPosition(new VizPositionXML(0, 0, 0));

            AttValuesXML attValuesXML = new AttValuesXML();
            AttValueXML nameAttValue = new AttValueXML();
            nameAttValue.setFor(1);
            nameAttValue.setValue(proteinXML.getId());
            attValuesXML.addAttValue(nameAttValue);
            AttValueXML nodeTypeAttValue = new AttValueXML();
            nodeTypeAttValue.setFor(2);
            nodeTypeAttValue.setValue("Protein");
            attValuesXML.addAttValue(nodeTypeAttValue);
            nodeXML.setAttvalues(attValuesXML);


            //----edges----
            List<GoTermXML> proteinTerms = new ArrayList<GoTermXML>();
            List<GoTermXML> bioTerms = proteinXML.getBiologicalProcessGoTerms();
            List<GoTermXML> cellTerms = proteinXML.getCellularComponentGoTerms();
            List<GoTermXML> molTerms = proteinXML.getMolecularFunctionGoTerms();

            if (subOntologyIncluded.equals(GephiExporter.ALL_SUB_ONTOLOGIES)) {
                if (bioTerms != null) {
                    proteinTerms.addAll(bioTerms);
                }
                if (cellTerms != null) {
                    proteinTerms.addAll(cellTerms);
                }
                if (molTerms != null) {
                    proteinTerms.addAll(molTerms);
                }
            } else if (subOntologyIncluded.equals(GephiExporter.MOLECULAR_FUNCTION_SUB_ONTOLOGY)) {
                if (molTerms != null) {
                    proteinTerms.addAll(molTerms);
                }
            } else if (subOntologyIncluded.equals(GephiExporter.BIOLOGICAL_PROCESS_SUB_ONTOLOGY)) {
                if (bioTerms != null) {
                    proteinTerms.addAll(bioTerms);
                }
            } else if (subOntologyIncluded.equals(GephiExporter.CELLULAR_COMPONENT_SUB_ONTOLOGY)) {
                if (cellTerms != null) {
                    proteinTerms.addAll(cellTerms);
                }
            }



            for (GoTermXML goTermXML : proteinTerms) {
                EdgeXML edge = new EdgeXML();
                edge.setId(String.valueOf(edgesIdCounter++));
                edge.setTarget(proteinXML.getId());
                edge.setSource(goTermXML.getId());
                edge.setType(EdgeXML.DIRECTED_TYPE);

                edgesXMLStBuilder.append((edge.toString() + "\n"));
            }

            if (proteinsWithoutConnectionsIncluded || proteinTerms.size() > 0) {
                nodesXMLStBuilder.append((nodeXML.toString() + "\n"));
            }

        }


        stBuilder.append((nodesXMLStBuilder.toString() + "</nodes>\n"));
        stBuilder.append((edgesXMLStBuilder.toString() + "</edges>\n"));

        stBuilder.append("</" + GraphXML.TAG_NAME + ">\n");
        stBuilder.append("</" + GexfXML.TAG_NAME + ">\n");

        return stBuilder.toString();
    }
}
