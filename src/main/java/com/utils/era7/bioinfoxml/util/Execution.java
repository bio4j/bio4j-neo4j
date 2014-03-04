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

package com.era7.bioinfoxml.util;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Execution extends XMLElement{

    public static final String TAG_NAME = "execution";

    public static final String CLASS_FULL_NAME_TAG_NAME = "class_full_name";

    public Execution(){
        super(new Element(TAG_NAME));
    }
    public Execution(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Execution(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    

    //----------------GETTERS---------------------
    public String getClassFullName(){   return getNodeText(CLASS_FULL_NAME_TAG_NAME);}

    public Arguments getArguments() throws XMLElementException{
        Arguments args = null;
        Element elem = root.getChild(Arguments.TAG_NAME);
        if(elem != null){
            args = new Arguments(elem);
        }
        return args;
    }


    //----------------SETTERS---------------------
    public void setClassFullName(String value){ setNodeText(CLASS_FULL_NAME_TAG_NAME, value);}
    
    public void setArguments(Arguments args){
        root.removeChildren(Argument.TAG_NAME);
        addChild(args);
    }
}

