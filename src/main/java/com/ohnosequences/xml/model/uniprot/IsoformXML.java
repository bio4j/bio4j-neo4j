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
package com.ohnosequences.xml.model.uniprot;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class IsoformXML extends XMLElement{
        
    public static final String TAG_NAME = "isoform";
    
    public static final String ID_TAG_NAME = "id";
    public static final String NAME_TAG_NAME = "name";
    public static final String NOTE_TAG_NAME = "note";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    
    
    public IsoformXML(){
        super(new Element(TAG_NAME));
    }
    public IsoformXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public IsoformXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
    
    
    //----------------SETTERS-------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setIsoformName(String value){  setNodeText(NAME_TAG_NAME, value);}
    public void setNote(String value){  setNodeText(NOTE_TAG_NAME, value);}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME, value);}
    
    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public String getIsoformName( ){  return getNodeText(NAME_TAG_NAME);}
    public String getNote( ){  return getNodeText(NOTE_TAG_NAME);}
    public String getSequence( ){  return getNodeText(SEQUENCE_TAG_NAME);}
}
