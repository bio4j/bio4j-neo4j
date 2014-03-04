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

package com.ohnosequences.xml.model;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Codon extends XMLElement{

    public static final String TAG_NAME = "codon";   

    public static final String START_CODON_TYPE = "start";
    public static final String STOP_CODON_TYPE = "stop";

    public static final String TYPE_TAG_NAME = "type";
    public static final String POSITION_TAG_NAME = "position";
    public static final String IS_CANONICAL_TAG_NAME = "is_canonical";
    public static final String SEQUENCE_TAG_NAME = "sequence";

    public Codon(){
        super(new Element(TAG_NAME));
    }
    public Codon(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Codon(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public String getType(){       return getNodeText(TYPE_TAG_NAME);  }
    public int getPosition(){   return Integer.parseInt(getNodeText(POSITION_TAG_NAME));}
    public boolean getIsCanonical(){    return Boolean.valueOf(getNodeText(IS_CANONICAL_TAG_NAME));}
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}

    //----------------SETTERS---------------------
    public void setType(String type){    setNodeText(TYPE_TAG_NAME, type);}
    public void setPosition(int pos){   setNodeText(POSITION_TAG_NAME, String.valueOf(pos));}
    public void setIsCanonical(boolean value){  setNodeText(IS_CANONICAL_TAG_NAME, String.valueOf(value));}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME, value);}

    

}
