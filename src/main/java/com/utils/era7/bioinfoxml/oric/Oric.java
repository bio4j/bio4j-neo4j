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

package com.era7.bioinfoxml.oric;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Oric extends XMLElement{

    public static final String TAG_NAME = "oric";

    public static final String ID_TAG_NAME = "id";
    public static final String LOCATION_START_TAG_NAME = "location_start";
    public static final String LOCATION_END_TAG_NAME = "location_end";
    public static final String GENOME_ELEMENT_ID_TAG_NAME  = "genome_element_id";
    public static final String GENOME_SIZE_TAG_NAME = "genome_size";
    public static final String TERC_POSITION_TAG_NAME = "terc_position";
    public static final String TYPE_TAG_NAME = "type";
    public static final String ORGANISM_TAG_NAME = "organism";

    public Oric(){
        super(new Element(TAG_NAME));
    }
    public Oric(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Oric(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public String getId(){  return getNodeText(ID_TAG_NAME); }
    public int getLocationStart(){  return Integer.parseInt(getNodeText(LOCATION_START_TAG_NAME)); }
    public int getLocationEnd(){  return Integer.parseInt(getNodeText(LOCATION_END_TAG_NAME)); }
    public String getGenomeElementId(){    return getNodeText(GENOME_ELEMENT_ID_TAG_NAME);}
    public int getGenomeSize(){ return Integer.parseInt(getNodeText(GENOME_SIZE_TAG_NAME));}
    public int getTercPosition(){   return Integer.parseInt(getNodeText(TERC_POSITION_TAG_NAME));}
    public int getType(){    return Integer.parseInt(getNodeText(TYPE_TAG_NAME));}
    public String getOrganism(){    return getNodeText(ORGANISM_TAG_NAME);}

    //----------------SETTERS---------------------
    public void setId(String value){    setNodeText(ID_TAG_NAME,value);}
    public void setLocationStart(int value){    setNodeText(LOCATION_START_TAG_NAME,String.valueOf(value));}
    public void setLocationEnd(int value){    setNodeText(LOCATION_END_TAG_NAME,String.valueOf(value));}
    public void setGenomeElementId(String value){  setNodeText(GENOME_ELEMENT_ID_TAG_NAME, value);}
    public void setGenomeSize(int value){   setNodeText(GENOME_SIZE_TAG_NAME,String.valueOf(value));}
    public void setTercPosition(int value){ setNodeText(TERC_POSITION_TAG_NAME,String.valueOf(value));}
    public void setType(int value){  setNodeText(TYPE_TAG_NAME,String.valueOf(value));}
    public void setOrganism(String value){  setNodeText(ORGANISM_TAG_NAME, value);}

}
