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
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class Bio4jRelationshipXML extends XMLElement{
    
    public static final String TAG_NAME = "relationship";
    
    public static final String NAME_TAG_NAME = "name";
    public static final String ITEM_TYPE_TAG_NAME = "item_type";
    public static final String DESCRIPTION_TAG_NAME = "description";
    public static final String START_NODES_TAG_NAME = "start_nodes";
    public static final String END_NODES_TAG_NAME = "end_nodes";
    public static final String INDEXES_TAG_NAME = "indexes";
    public static final String PROPERTIES_TAG_NAME = "properties";
    public static final String JAVADOC_URL_TAG_NAME = "javadoc_url";
    public static final String NAME_PROPERTY_TAG_NAME = "name_property";    
    public static final String DATA_SOURCE_TAG_NAME = "data_source";
    
    public static final String RELATIONSHIP_ITEM_TYPE = "relationship";
    
    public Bio4jRelationshipXML(){
        super(new Element(TAG_NAME));
    }
    public Bio4jRelationshipXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Bio4jRelationshipXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
        
    //----------------GETTERS---------------------
    public String getDescription( ){  return getNodeText(DESCRIPTION_TAG_NAME);}
    public String getItemType(){    return getNodeText(ITEM_TYPE_TAG_NAME);}
    public String getRelationshipName( ){  return getNodeText(NAME_TAG_NAME);}
    public String getJavadocUrl(){    return getNodeText(JAVADOC_URL_TAG_NAME);}
    public String getNameProperty(){    return getNodeText(NAME_PROPERTY_TAG_NAME);}
    public String getDataSource(){  return getNodeText(DATA_SOURCE_TAG_NAME);}
        
    public List<Bio4jNodeXML> getStartNodes() throws XMLElementException{
        LinkedList<Bio4jNodeXML> list = new LinkedList<Bio4jNodeXML>();
        Element elem = root.getChild(START_NODES_TAG_NAME);
        if(elem != null){
            for (Object e : elem.getChildren(Bio4jNodeXML.TAG_NAME)) {
                list.add(new Bio4jNodeXML((Element)e));
            }            
        }
        return list;
    }
    public List<Bio4jNodeXML> getEndNodes() throws XMLElementException{
        LinkedList<Bio4jNodeXML> list = new LinkedList<Bio4jNodeXML>();
        Element elem = root.getChild(END_NODES_TAG_NAME);
        if(elem != null){
            for (Object e : elem.getChildren(Bio4jNodeXML.TAG_NAME)) {
                list.add(new Bio4jNodeXML((Element)e));
            }            
        }
        return list;
    }
    public List<Bio4jRelationshipIndexXML> getIndexes() throws XMLElementException{
        LinkedList<Bio4jRelationshipIndexXML> list = new LinkedList<Bio4jRelationshipIndexXML>();
        Element elem = root.getChild(INDEXES_TAG_NAME);
        if(elem != null){
            for (Object e : elem.getChildren(Bio4jRelationshipIndexXML.TAG_NAME)) {
                list.add(new Bio4jRelationshipIndexXML((Element)e));
            }            
        }        
        return list;
    }
    
    //----------------SETTERS-------------------
    public void setDescription(String value){  setNodeText(DESCRIPTION_TAG_NAME, value);}
    public void setRelationshipName(String value){  setNodeText(NAME_TAG_NAME, value);}
    public void setItemType(String value){  setNodeText(ITEM_TYPE_TAG_NAME, value);}
    public void setJavadocUrl(String value){    setNodeText(JAVADOC_URL_TAG_NAME, value);}
    public void setNameProperty(String value){  setNodeText(NAME_PROPERTY_TAG_NAME, value);}
    public void setDataSource(String value){    setNodeText(DATA_SOURCE_TAG_NAME, value);}
    
    public void addStartNode(Bio4jNodeXML node){
        Element elem = initStartNodesTag();
        elem.addContent(node.getRoot());                
    }
    public void addEndNode(Bio4jNodeXML node){
        Element elem = initEndNodesTag();
        elem.addContent(node.getRoot());                
    }
    public void addIndex(Bio4jRelationshipIndexXML index){
        Element elem = initIndexesTag();
        elem.addContent(index.getRoot());     
    }
    public void addProperty(Bio4jPropertyXML property){
        Element elem = initPropertiesTag();
        elem.addContent(property.getRoot());  
    }
    
    private Element initStartNodesTag(){
        Element elem = root.getChild(START_NODES_TAG_NAME);
        if(elem == null){
            elem = new Element(START_NODES_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    private Element initEndNodesTag(){
        Element elem = root.getChild(END_NODES_TAG_NAME);
        if(elem == null){
            elem = new Element(END_NODES_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    private Element initIndexesTag(){
        Element elem = root.getChild(INDEXES_TAG_NAME);
        if(elem == null){
            elem = new Element(INDEXES_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    private Element initPropertiesTag(){
        Element elem = root.getChild(PROPERTIES_TAG_NAME);
        if(elem == null){
            elem = new Element(PROPERTIES_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    
}
