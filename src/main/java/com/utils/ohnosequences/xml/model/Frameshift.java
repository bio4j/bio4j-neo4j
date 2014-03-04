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

package com.ohnosequences.xml.model;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Frameshift extends XMLElement{

    public static final String TAG_NAME = "frameshift";

    public static final String POSITION_TAG_NAME = "position";
    public static final String READING_FRAME_FROM_TAG_NAME = "reading_frame_from";
    public static final String READING_FRAME_TO_TAG_NAME = "reading_frame_to";

     public Frameshift(){
        super(new Element(TAG_NAME));
    }
    public Frameshift(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Frameshift(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public int getPosition(){  return Integer.parseInt(getNodeText(POSITION_TAG_NAME)); }
    public int getReadingFrameFrom(){  return Integer.parseInt(getNodeText(READING_FRAME_FROM_TAG_NAME)); }
    public int getReadingFrameTo(){  return Integer.parseInt(getNodeText(READING_FRAME_TO_TAG_NAME)); }

    //----------------SETTERS---------------------
    public void setPosition(int value){    setNodeText(POSITION_TAG_NAME,String.valueOf(value));}
    public void setReadingFrameFrom(int value){    setNodeText(READING_FRAME_FROM_TAG_NAME,String.valueOf(value));}
    public void setReadingFrameTo(int value){    setNodeText(READING_FRAME_TO_TAG_NAME,String.valueOf(value));}

}
