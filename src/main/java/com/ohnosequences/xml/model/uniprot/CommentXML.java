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
public class CommentXML extends XMLElement{
        
    public static final String TAG_NAME = "comment";
    
    public static final String TYPE_TAG_NAME = "type";
    public static final String TEXT_TAG_NAME = "text";
    
    
    public CommentXML(){
        super(new Element(TAG_NAME));
    }
    public CommentXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public CommentXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
    
    
    //----------------SETTERS-------------------
    public void setType(String value){  setNodeText(TYPE_TAG_NAME, value);}
    public void setCommentText(String value){   setNodeText(TEXT_TAG_NAME, value);}
    
    //----------------GETTERS---------------------
    public String getType( ){  return getNodeText(TYPE_TAG_NAME);}
    public String getCommentText() {    return getNodeText(TEXT_TAG_NAME);    }
}
