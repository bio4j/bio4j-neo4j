/*
 * Copyright (C) 2010-2012  "Oh no sequences!"
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.era7.bioinfoxml.go;

import com.era7.era7xmlapi.model.XMLElement;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Pablo Pareja Tobes
 */
public class GoTermXML extends XMLElement implements Comparable<GoTermXML> {

    public static String GO_TERM_SERVICE_URL = "http://www.ebi.ac.uk/QuickGO/GTerm?format=oboxml&id=";

    public static final String TAG_NAME = "go_term";

    public static final String ASPECT_FUNCTION = "molecular_function";
    public static final String ASPECT_FUNCTION_GO_TERM = "GO:0003674";
    public static final String ASPECT_COMPONENT = "cellular_component";
    public static final String ASPECT_COMPONENT_GO_TERM = "GO:0005575";
    public static final String ASPECT_PROCESS = "biological_process";
    public static final String ASPECT_PROCESS_GO_TERM = "GO:0008150";

    public static final String DATE_TAG_NAME = "date";
    public static final String ID_TAG_NAME = "id";
    public static final String DEFINITION_TAG_NAME = "definition";
    public static final String NAME_TAG_NAME = "name";
    public static final String REFERENCE_TAG_NAME = "reference";
    public static final String EVIDENCE_TAG_NAME = "evidence";
    public static final String WITH_TAG_NAME = "with";
    public static final String ASPECT_TAG_NAME = "aspect";
    public static final String SOURCE_TAG_NAME = "source";
    public static final String ANNOTATIONS_COUNT_TAG_NAME = "annotations_count";

    public static final String FREQUENCY_PERCENTAGE_TAG_NAME = "frequency_percentage";

    public static final String PROTEIN_ANNOTATION_LEADING_TO_SLIM_TERM = "protein_annotation_leading_to_slim_term";

    public GoTermXML() {
        super(new Element(TAG_NAME));
    }

    public GoTermXML(String value) throws Exception {
        super(value);
    }

    public GoTermXML(Element element) {
        super(element);
    }

    public static GoTermXML getGoTerm(String id) throws Exception{

        GoTermXML temp = new GoTermXML();

        URL url = new URL(GO_TERM_SERVICE_URL + id);
        // Connect
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // Get data
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(urlConnection.getInputStream());

        Element termElement = doc.getRootElement().getChild("term");

        String idString,nameString,defString;
        idString = termElement.getChildText("id");
        nameString = termElement.getChildText("name");
        defString = termElement.getChild("def").getChildText("defstr");

        if(!id.equals(idString)){
            throw new Exception("El id proporcionado y el encontrado en el xml proporcionado por el servicio no son el mismo");
        }else{
            temp.setId(idString);
            temp.setGoName(nameString);
            temp.setDefinition(defString);
        }
        
        return temp;
    }

    //----------------SETTERS-------------------
    public void setAnnotationsCount(int value){
        setNodeText(ANNOTATIONS_COUNT_TAG_NAME, String.valueOf(value));
    }
    public void setDate(String value) {
        setNodeText(DATE_TAG_NAME, value);
    }
    public void setFrequencyPercentage(double value){
        setNodeText(FREQUENCY_PERCENTAGE_TAG_NAME, String.valueOf(value));
    }
    public void setDefinition(String value){
        setNodeText(DEFINITION_TAG_NAME, value);
    }
    public void setId(String value) {
        setNodeText(ID_TAG_NAME, value);
    }

    public void setGoName(String value) {
        setNodeText(NAME_TAG_NAME, value);
    }

    public void setReference(String value) {
        setNodeText(REFERENCE_TAG_NAME, value);
    }

    public void setEvidence(String value) {
        setNodeText(EVIDENCE_TAG_NAME, value);
    }

    public void setWith(String value) {
        setNodeText(WITH_TAG_NAME, value);
    }

    public void setAspect(String value) {
        setNodeText(ASPECT_TAG_NAME, value);
    }

    public void setSource(String value) {
        setNodeText(SOURCE_TAG_NAME, value);
    }

    public void setProteinAnnotationLeadingToSlimTerm(GoTermXML goTerm){
        initProteinAnnotationLeadingToSlimTermTag();
        Element elem = this.root.getChild(PROTEIN_ANNOTATION_LEADING_TO_SLIM_TERM);
        elem.removeChildren(GoTermXML.TAG_NAME);
        elem.addContent(goTerm.asJDomElement());
    }

    //----------------GETTERS---------------------
    public int getAnnotationsCount(){
        return Integer.parseInt(getNodeText(ANNOTATIONS_COUNT_TAG_NAME));
    }
    public String getDate() {
        return getNodeText(DATE_TAG_NAME);
    }

    public double getFrequencyPercentage(){
        return Double.parseDouble(getNodeText(FREQUENCY_PERCENTAGE_TAG_NAME));
    }

    public String getDefinition(){
        return getNodeText(DEFINITION_TAG_NAME);
    }

    public String getId() {
        return getNodeText(ID_TAG_NAME);
    }

    public String getGoName() {
        return getNodeText(NAME_TAG_NAME);
    }

    public String getReference() {
        return getNodeText(REFERENCE_TAG_NAME);
    }

    public String getEvidence() {
        return getNodeText(EVIDENCE_TAG_NAME);
    }

    public String getWith() {
        return getNodeText(WITH_TAG_NAME);
    }

    public String getAspect() {
        return getNodeText(ASPECT_TAG_NAME);
    }

    public String getSource() {
        return getNodeText(SOURCE_TAG_NAME);
    }

    public GoTermXML getProteinAnnotationLeadingToSlimTerm(){
        GoTermXML result = null;
        Element elem = root.getChild(PROTEIN_ANNOTATION_LEADING_TO_SLIM_TERM);
        if(elem != null){
            Element goElem = elem.getChild(GoTermXML.TAG_NAME);
            result = new GoTermXML(goElem);
        }
        return result;
    }


    private void initProteinAnnotationLeadingToSlimTermTag(){
        Element elem = root.getChild(PROTEIN_ANNOTATION_LEADING_TO_SLIM_TERM);
        if(elem == null){
            elem = new Element(PROTEIN_ANNOTATION_LEADING_TO_SLIM_TERM);
            root.addContent(elem);
        }
    }

    @Override
    public int compareTo(GoTermXML o) {
        if(this.getAnnotationsCount() < o.getAnnotationsCount()){
            return -1;
        }else if(this.getAnnotationsCount() == o.getAnnotationsCount()){
            if(this.getId().equals(o.getId())){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 1;
        }
    }
}
