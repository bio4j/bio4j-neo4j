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
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class GOSlimXML extends XMLElement{

    public static final String TAG_NAME = "go_slim";
    
    public static final String SAMPLE_GENE_NUMBER_TAG_NAME = "sample_gene_number";
    public static final String SAMPLE_ANNOTATED_GENE_NUMBER_TAG_NAME = "sample_annotated_gene_number";
    //public static final String SLIM_SET_TAG_NAME = "slim_set";
    public static final String GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET = "go_terms_lost_not_included_in_slim_set";

    public static final String PROTEINS_TAG_NAME = "proteins";

    public GOSlimXML() {
        super(new Element(TAG_NAME));
    }

    public GOSlimXML(String value) throws Exception {
        super(value);
    }

    public GOSlimXML(Element element) {
        super(element);
    }

     //----------------SETTERS-------------------
    
    public void setGoTermsLostNotIncludedInSlimSet(int value){
        setNodeText(GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET, String.valueOf(value));
    }
    public void setSampleGeneNumber(int value){
        setNodeText(SAMPLE_GENE_NUMBER_TAG_NAME, String.valueOf(value));
    }
    public void setSampleAnnotatedGeneNumber(int value){
        setNodeText(SAMPLE_ANNOTATED_GENE_NUMBER_TAG_NAME, String.valueOf(value));
    }
    public void setSlimSet(SlimSetXML set){
        root.removeChildren(SlimSetXML.TAG_NAME);
        addChild(set);
    }


    //----------------GETTERS---------------------
    public SlimSetXML getSlimSet(){
        SlimSetXML set = null;
        Element elem = root.getChild(SlimSetXML.TAG_NAME);
        if(elem != null){
            set = new SlimSetXML(elem);
        }
        return set;
    }
    public List<GoTermXML> getGoTermsLostNotIncludedInSlimSet(){
        
        List<GoTermXML> list = new ArrayList<GoTermXML>();
        
        Element elem = this.asJDomElement().getChild(GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET);
        if(elem != null){
            List<Element> tempList = elem.getChildren(GoTermXML.TAG_NAME);
            for (Element element : tempList) {
                list.add(new GoTermXML(element));
            }
        }
        
        return list;
    }
    public int getSampleGeneNumber() throws NumberFormatException{
        return Integer.parseInt(getNodeText(SAMPLE_GENE_NUMBER_TAG_NAME));
    }
    public int getSampleAnnotatedGeneNumber() throws NumberFormatException{
        return Integer.parseInt(getNodeText(SAMPLE_ANNOTATED_GENE_NUMBER_TAG_NAME));
    }
    
    
    public void addGoTermLostNotIncludedInSlimSet(GoTermXML term){
        Element elem = initGoTermLostNotIncludedInSlimSetTag();
        elem.addContent(term.asJDomElement());        
    }
    
    
    private Element initGoTermLostNotIncludedInSlimSetTag(){
        Element elem = this.asJDomElement().getChild(GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET);
        if(elem == null){
            this.asJDomElement().addContent(new Element(GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET));
            elem = this.asJDomElement().getChild(GO_TERMS_LOST_NOT_INCLUDED_IN_SLIM_SET);
        }
        return elem;
    }

}
