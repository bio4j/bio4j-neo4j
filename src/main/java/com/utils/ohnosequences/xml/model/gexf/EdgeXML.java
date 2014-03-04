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

package com.ohnosequences.xml.model.gexf;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class EdgeXML extends XMLElement{

    public static final String TAG_NAME = "edge";

    public static final String ID_ATTRIBUTE = "id";
    public static final String SOURCE_ATTRIBUTE = "source";
    public static final String TARGET_ATTRIBUTE = "target";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String START_ATTRIBUTE = "start";
    public static final String END_ATTRIBUTE = "end";
    public static final String WEIGHT_ATTRIBUTE = "weight";

    public static final String DIRECTED_TYPE = "directed";
    public static final String UNDIRECTED_TYPE = "undirected";

    public EdgeXML(){
        super(new Element(TAG_NAME));
    }
    public EdgeXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public EdgeXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setId(String value){
        this.root.setAttribute(ID_ATTRIBUTE, value);
    }
    public void setSource(String value){
        this.root.setAttribute(SOURCE_ATTRIBUTE, value);
    }
    public void setTarget(String value){
        this.root.setAttribute(TARGET_ATTRIBUTE, value);
    }
    public void setType(String value){
        this.root.setAttribute(TYPE_ATTRIBUTE,value);
    }
    public void setStart(String value){
        this.root.setAttribute(START_ATTRIBUTE, value);
    }
    public void setEnd(String value){
        this.root.setAttribute(END_ATTRIBUTE, value);
    }
    public void setWeight(double value){
        this.root.setAttribute(WEIGHT_ATTRIBUTE, String.valueOf(value));
    }

    //----------------GETTERS-------------------
    public String getId(){
        return this.root.getAttributeValue(ID_ATTRIBUTE);
    }
    public String getSource(){
        return this.root.getAttributeValue(SOURCE_ATTRIBUTE);
    }
    public String getTarget(){
        return this.root.getAttributeValue(TARGET_ATTRIBUTE);
    }
    public String getType(){
        return this.root.getAttributeValue(TYPE_ATTRIBUTE);
    }
    public String getStart(){
        return this.root.getAttributeValue(START_ATTRIBUTE);
    }
    public String getEnd(){
        return this.root.getAttributeValue(END_ATTRIBUTE);
    }
    public double getWeight(){
        return Double.parseDouble(this.root.getAttributeValue(WEIGHT_ATTRIBUTE));
    }



    public void setAttvalues(AttValuesXML attValuesXML){
        this.root.removeChildren(AttValuesXML.TAG_NAME);
        this.root.addContent(attValuesXML.asJDomElement());
    }

}
