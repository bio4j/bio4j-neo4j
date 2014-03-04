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
package com.era7.bioinfoxml.bio4j;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class Bio4jRelationshipIndexXML extends XMLElement{
    
    public static final String TAG_NAME = "relationship_index";
    
    public static final String NAME_TAG_NAME = "name";
    public static final String RELATIONSHIP_NAME_TAG_NAME = "relationship_name";
    public static final String ITEM_TYPE_TAG_NAME = "item_type";
    
    public static final String RELATIONSHIP_INDEX_ITEM_TYPE = "relationship_index";
    
    public Bio4jRelationshipIndexXML(){
        super(new Element(TAG_NAME));
    }
    public Bio4jRelationshipIndexXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Bio4jRelationshipIndexXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
        
    //----------------GETTERS---------------------
    public String getRelationshipName( ){  return getNodeText(RELATIONSHIP_NAME_TAG_NAME);}
    public String getIndexName( ){  return getNodeText(NAME_TAG_NAME);}
    public String getItemType(){    return getNodeText(ITEM_TYPE_TAG_NAME);}
    
    
    //----------------SETTERS-------------------
    public void setRelationshipName(String value){  setNodeText(RELATIONSHIP_NAME_TAG_NAME, value);}
    public void setIndexName(String value){  setNodeText(NAME_TAG_NAME, value);}
    public void setItemType(String value){  setNodeText(ITEM_TYPE_TAG_NAME, value);}
    
}
