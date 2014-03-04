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

package com.era7.bioinfoxml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class ContigXML extends XMLElement{

    public static final String TAG_NAME = "contig";

    public static final String ID_TAG_NAME = "id";
    public static final String LENGTH_TAG_NAME = "length";
    public static final String BEGIN_TAG_NAME = "begin";
    public static final String END_TAG_NAME = "end";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String GAPS_PERCENTAGE_TAG_NAME = "gaps_percentage";
    public static final String ORGANISM_TAG_NAME = "organism";
    public static final String ORGANISM_COMPLETE_TAXONOMY_LINEAGE = "organism_complete_taxonomy_lineage";

    public static final String HSPS_TAG_NAME = "hsps";

    public ContigXML(){
        super(new Element(TAG_NAME));
    }
    public ContigXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public ContigXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    public void addPredictedGene(PredictedGene gene){
        root.addContent(gene.asJDomElement());
    }
    public void addPredictedRna(PredictedRna rna){
        root.addContent(rna.asJDomElement());
    }

    public void addGap(Gap gap){
        root.addContent(gap.asJDomElement());
    }

    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public int getLength(){ return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));}
    public int getBegin(){  return Integer.parseInt(getNodeText(BEGIN_TAG_NAME));}
    public int getEnd(){    return Integer.parseInt(getNodeText(END_TAG_NAME));}
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}
    public String getOrganism(){    return getNodeText(ORGANISM_TAG_NAME);}
    public String getOrganismCompleteTaxonomyLineage(){ return getNodeText(ORGANISM_COMPLETE_TAXONOMY_LINEAGE);}
    public double getGapsPercentage(){  return Double.parseDouble(getNodeText(GAPS_PERCENTAGE_TAG_NAME));}

    public List<Hsp> getHsps() throws XMLElementException{
        Element hsps = root.getChild(HSPS_TAG_NAME);
        if(hsps == null){
            return null;
        }else{
            ArrayList<Hsp> array = new ArrayList<Hsp>();
            for (Object hspElem : hsps.getChildren(Hsp.TAG_NAME)) {
                array.add(new Hsp((Element)hspElem));
            }
            return array;
        }
    }

    //----------------SETTERS-------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setLength(int value){    setNodeText(LENGTH_TAG_NAME, String.valueOf(value));}
    public void setBegin(int value){    setNodeText(BEGIN_TAG_NAME, String.valueOf(value));}
    public void setEnd(int value){    setNodeText(END_TAG_NAME, String.valueOf(value));}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME,value);}
    public void setOrganism(String value){  setNodeText(ORGANISM_TAG_NAME, value);}
    public void setOrganismCompleteTaxonomyLineage(String value){   setNodeText(ORGANISM_COMPLETE_TAXONOMY_LINEAGE, value);}
    public void setGapsPercentage(double value){    setNodeText(GAPS_PERCENTAGE_TAG_NAME, String.valueOf(value));}

    
    public void addHsp(Hsp hsp){
        initHspsTag();
        Element hsps = root.getChild(HSPS_TAG_NAME);
        hsps.addContent(hsp.asJDomElement());
    }


    private void initHspsTag(){
        Element hsps = root.getChild(HSPS_TAG_NAME);
        if(hsps == null){
            hsps = new Element(HSPS_TAG_NAME);
            root.addContent(hsps);
        }
    }

}
