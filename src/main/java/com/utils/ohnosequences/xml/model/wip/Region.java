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

package com.ohnosequences.xml.model.wip;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Region extends XMLElement {

    public static final String TAG_NAME = "region";

    public static final String ID_TAG_NAME = "id";
    public static final String END_TAG_NAME = "end";
    public static final String BEGIN_TAG_NAME = "begin";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String TYPE_TAG_NAME = "type";
    public static final String UNIPROT_ID_TAG_NAME = "uniprot_id";

    public Region() {
        super(new Element(TAG_NAME));
    }

    public Region(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public Region(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }


    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public String getSequence( ){  return getNodeText(SEQUENCE_TAG_NAME);}
    public String getType( ){  return getNodeText(TYPE_TAG_NAME);}
    public String getUniprotId( ){  return getNodeText(UNIPROT_ID_TAG_NAME);}
    public int getBegin( ){  return Integer.parseInt(getNodeText(BEGIN_TAG_NAME));}
    public int getEnd(){    return Integer.parseInt(getNodeText(END_TAG_NAME));}

    //----------------SETTERS---------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME, value);}
    public void setType(String value){  setNodeText(TYPE_TAG_NAME, value);}
    public void setUniprotId(String value){  setNodeText(UNIPROT_ID_TAG_NAME, value);}
    public void setBegin(int value){    setNodeText(BEGIN_TAG_NAME, String.valueOf(value));}
    public void setEnd(int value){    setNodeText(END_TAG_NAME, String.valueOf(value));}


}
