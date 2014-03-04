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

package com.ohnosequences.xml.model.util;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class XMLWrapperClass extends XMLElement{

    public static final String TAG_NAME = "XMLWrapperClass";

    public static final String NAME_TAG_NAME = "name";
    public static final String PACKAGE_TAG_NAME = "package";
    public static final String VARS_TAG_NAME = "vars";
    public static final String TAG_NAME_TAG_NAME = "tag_name";

    public XMLWrapperClass(){
        super(new Element(TAG_NAME));
    }
    public XMLWrapperClass(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public XMLWrapperClass(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public String getClassName(){       return getNodeText(NAME_TAG_NAME);  }
    public String getTagName(){       return getNodeText(TAG_NAME_TAG_NAME);  }
    public String getPackage(){       return getNodeText(PACKAGE_TAG_NAME);  }
    public Element getVars(){   return root.getChild(VARS_TAG_NAME);}

    //----------------SETTERS---------------------
    public void setClassName(String s){    setNodeText(NAME_TAG_NAME, s);}
    public void setTagName(String s){   setNodeText(TAG_NAME_TAG_NAME, s);}
    public void setPackage(String s){    setNodeText(PACKAGE_TAG_NAME, s);}

}
