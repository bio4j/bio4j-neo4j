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

package com.ohnosequences.xml.model.go;

import com.ohnosequences.xml.model.uniprot.ProteinXML;
import com.ohnosequences.xml.api.model.XMLElement;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class GoAnnotationXML extends XMLElement{

    public static final String TAG_NAME = "go_annotation";
    public static final String SAMPLE_GENE_NUMBER_TAG_NAME = "sample_gene_number";

    public static final String ANNOTATOR_GO_TERMS_TAG_NAME = "annotator_go_terms";
    public static final String PROTEIN_ANNOTATIONS_TAG_NAME = "protein_annotations";

    public GoAnnotationXML() {
        super(new Element(TAG_NAME));
    }

    public GoAnnotationXML(String value) throws Exception {
        super(value);
    }

    public GoAnnotationXML(Element element) {
        super(element);
    }

    public void addAnnotatorGoTerm(GoTermXML term){
        initAnnotatorGoTermsTag();
        Element annotators = root.getChild(ANNOTATOR_GO_TERMS_TAG_NAME);
        annotators.addContent(term.asJDomElement());
    }

    public void addProteinAnnotation(ProteinXML protein){
        initProteinAnnotatiosTag();
        Element proteins = root.getChild(PROTEIN_ANNOTATIONS_TAG_NAME);
        proteins.addContent(protein.asJDomElement());
    }

    private void initProteinAnnotatiosTag(){
        Element proteins = root.getChild(PROTEIN_ANNOTATIONS_TAG_NAME);
        if(proteins == null){
            proteins = new Element(PROTEIN_ANNOTATIONS_TAG_NAME);
            root.addContent(proteins);
        }
    }

    private void initAnnotatorGoTermsTag(){
        Element annotators = root.getChild(ANNOTATOR_GO_TERMS_TAG_NAME);
        if(annotators == null){
            annotators = new Element(ANNOTATOR_GO_TERMS_TAG_NAME);
            root.addContent(annotators);
        }
    }

    //----------------SETTERS-------------------    
    public void setSampleGeneNumber(int value){
        setNodeText(SAMPLE_GENE_NUMBER_TAG_NAME, String.valueOf(value));
    }


    //----------------GETTERS---------------------
    public int getSampleGeneNumber() throws NumberFormatException{
        return Integer.parseInt(getNodeText(SAMPLE_GENE_NUMBER_TAG_NAME));
    }
    public Element getProteinAnnotations(){
        return root.getChild(PROTEIN_ANNOTATIONS_TAG_NAME);
    }
    public List<GoTermXML> getAnnotatorGoTerms(){
        Element tempElem = root.getChild(ANNOTATOR_GO_TERMS_TAG_NAME);
        if(tempElem != null){
            List<GoTermXML> result = new ArrayList<GoTermXML>();
            List<Element> tempList = tempElem.getChildren(GoTermXML.TAG_NAME);
            for (Element element : tempList) {
                result.add(new GoTermXML(element));
            }
            return result;
        }else{
            return null;
        }
    }

}
