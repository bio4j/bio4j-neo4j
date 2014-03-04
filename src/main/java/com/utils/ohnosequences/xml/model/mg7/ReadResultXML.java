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
package com.ohnosequences.xml.model.mg7;

import com.ohnosequences.xml.model.Hit;
import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class ReadResultXML extends XMLElement{
    
    public static final String TAG_NAME = "read_result";   

    public static final String READ_ID_TAG_NAME = "read_id";
    public static final String QUERY_LENGTH_TAG_NAME = "query_length";
    public static final String SEQUENCE_TAG_NAME = "sequence";
        
    public static final String HITS_TAG_NAME = "hits";
    
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
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}
    
    
    public List<Hit> getHits() throws XMLElementException{
        
        Element elem = initHitsTag();
        List<Hit> resultList = new LinkedList<Hit>();
        List<Element> tempList = elem.getChildren(Hit.TAG_NAME);
        for (Element element : tempList) {
            resultList.add(new Hit(element));
        }
        return resultList;      
        
    } 
    
    public void addHit(Hit hit){
        Element hits = initHitsTag();
        hits.addContent(hit.getRoot());
    }

    //----------------SETTERS---------------------
    public void setReadId(String value){    setNodeText(READ_ID_TAG_NAME, value);}
    public void setQueryLength(int value){  setNodeText(QUERY_LENGTH_TAG_NAME, String.valueOf(value));}    
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME, value);}
    
    
    protected Element initHitsTag(){
        Element elem = this.root.getChild(HITS_TAG_NAME);
        if(elem == null){
            root.addContent(new Element(HITS_TAG_NAME));
            elem = this.root.getChild(HITS_TAG_NAME);
        }
        return elem;
    }
    
}
