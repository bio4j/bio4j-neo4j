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

package com.era7.bioinfoxml.graphml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class KeyXML extends XMLElement{
    
    public static final String EDGE = "edge";
    public static final String NODE = "node";

    public static final String TAG_NAME = "key";

    public static final String ID_ATTRIBUTE = "id";
    public static final String FOR_ATTRIBUTE = "for";
    public static final String ATTR_NAME_ATTRIBUTE = "attr.name";
    public static final String ATTR_TYPE_ATTRIBUTE = "attr.type";

    public KeyXML(){
        super(new Element(TAG_NAME));
    }
    public KeyXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public KeyXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setId(String value){
        this.root.setAttribute(ID_ATTRIBUTE, value);
    }
    public void setFor(String value){
        this.root.setAttribute(FOR_ATTRIBUTE, value);
    }
    public void setAttrName(String value){
        this.root.setAttribute(ATTR_NAME_ATTRIBUTE, value);
    }
    public void setAttrType(String value){
        this.root.setAttribute(ATTR_TYPE_ATTRIBUTE, value);
    }

    //----------------GETTERS-------------------
    public String getId(){
        return this.root.getAttributeValue(ID_ATTRIBUTE);
    }
    public String getFor(){
        return this.root.getAttributeValue(FOR_ATTRIBUTE);
    }
    public String getAttrName(){
        return this.root.getAttributeValue(ATTR_NAME_ATTRIBUTE);
    }
    public String getAttrType(){
        return this.root.getAttributeValue(ATTR_TYPE_ATTRIBUTE);
    }
    

}
