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
package com.ohnosequences.xml.model.bio4j;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class Bio4jNodeIndexXML extends XMLElement{
    
    public static final String TAG_NAME = "node_index";
    
    public static final String PROPERTY_INDEXED_TAG_NAME = "property_indexed";
    public static final String NAME_TAG_NAME = "name";
    public static final String NODE_NAME_TAG_NAME = "node_name";
    public static final String ITEM_TYPE_TAG_NAME = "item_type";
    public static final String INDEX_TYPE_TAG_NAME = "index_type";
    
    public static final String NODE_INDEX_ITEM_TYPE = "node_index";
    
    public Bio4jNodeIndexXML(){
        super(new Element(TAG_NAME));
    }
    public Bio4jNodeIndexXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Bio4jNodeIndexXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
        
    //----------------GETTERS---------------------
    public String getPropertyIndexed( ){  return getNodeText(PROPERTY_INDEXED_TAG_NAME);}
    public String getNodeName( ){  return getNodeText(NODE_NAME_TAG_NAME);}
    public String getIndexName( ){  return getNodeText(NAME_TAG_NAME);}
    public String getItemType(){    return getNodeText(ITEM_TYPE_TAG_NAME);}
    public String getIndexType(){    return getNodeText(INDEX_TYPE_TAG_NAME);}
    
    
    //----------------SETTERS-------------------
    public void setPropertyIndexed(String value){  setNodeText(PROPERTY_INDEXED_TAG_NAME, value);}
    public void setNodeName(String value){  setNodeText(NODE_NAME_TAG_NAME, value);}
    public void setIndexName(String value){  setNodeText(NAME_TAG_NAME, value);}
    public void setItemType(String value){  setNodeText(ITEM_TYPE_TAG_NAME, value);}
    public void setIndexType(String value){    setNodeText(INDEX_TYPE_TAG_NAME, value);}
    
}
