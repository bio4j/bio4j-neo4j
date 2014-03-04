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
package com.era7.bioinfoxml.metagenomics;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class ReadResultXML extends XMLElement{
    
    public static final String TAG_NAME = "read_result";   

    public static final String READ_ID_TAG_NAME = "read_id";
    public static final String QUERY_LENGTH_TAG_NAME = "query_length";
    public static final String HIT_LENGTH_TAG_NAME = "hit_length";
    public static final String ALIGNMENT_LENGTH_TAG_NAME = "alignment_length";
    public static final String IDENTITY_TAG_NAME = "identity";
    public static final String EVALUE_TAG_NAME = "evalue";
    public static final String QUERY_SEQUENCE_TAG_NAME = "query_sequence";
    public static final String HIT_SEQUENCE_TAG_NAME = "hit_sequence";
    public static final String MIDLINE_TAG_NAME = "midline";
    public static final String GI_ID_TAG_NAME = "gi_id";
    
    public ReadResultXML(){
        super(new Element(TAG_NAME));
    }
    public ReadResultXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public ReadResultXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
    
    //----------------GETTERS---------------------
    public String getReadId(){       return getNodeText(READ_ID_TAG_NAME);  }
    public int getQueryLength(){       return Integer.parseInt(getNodeText(QUERY_LENGTH_TAG_NAME));  }
    public int getHitLength(){       return Integer.parseInt(getNodeText(HIT_LENGTH_TAG_NAME));  }
    public int getAlignmentLength(){       return Integer.parseInt(getNodeText(ALIGNMENT_LENGTH_TAG_NAME));  }
    public int getIdentity(){    return Integer.parseInt(getNodeText(IDENTITY_TAG_NAME));}
    public String getEvalue(){  return getNodeText(EVALUE_TAG_NAME);}
    public String getQuerySequence(){  return getNodeText(QUERY_SEQUENCE_TAG_NAME);}
    public String getHitSequence(){ return getNodeText(HIT_SEQUENCE_TAG_NAME);}
    public String getMidline(){ return getNodeText(MIDLINE_TAG_NAME);}
    public int getGiId(){    return Integer.parseInt(getNodeText(GI_ID_TAG_NAME));}

    //----------------SETTERS---------------------
    public void setReadId(String type){    setNodeText(READ_ID_TAG_NAME, type);}
    public void setQueryLength(int value){  setNodeText(QUERY_LENGTH_TAG_NAME, String.valueOf(value));}
    public void setHitLength(int value){  setNodeText(HIT_LENGTH_TAG_NAME, String.valueOf(value));}
    public void setAlignmentLength(int value){  setNodeText(ALIGNMENT_LENGTH_TAG_NAME, String.valueOf(value));}
    public void setIdentity(int value){  setNodeText(IDENTITY_TAG_NAME, String.valueOf(value));}
    public void setEvalue(String value){  setNodeText(EVALUE_TAG_NAME, value);}
    public void setQuerySequence(String type){    setNodeText(QUERY_SEQUENCE_TAG_NAME, type);}
    public void setHitSequence(String value){  setNodeText(HIT_SEQUENCE_TAG_NAME, value);}
    public void setMidline(String value){  setNodeText(MIDLINE_TAG_NAME, value);}
    public void setGiId(int value){  setNodeText(GI_ID_TAG_NAME, String.valueOf(value));}
    
}
