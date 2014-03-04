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
public class WipPosition extends XMLElement{

    public static final String TAG_NAME = "wip_position";

    public static final String WORD_TEXT_TAG_NAME = "word_text";
    public static final String WORD_TEXT_PATTERN_TAG_NAME = "word_text_pattern";

    public static final String WORD_LENGTH_TAG_NAME = "word_length";
    public static final String PATTERN_POSITION_TAG_NAME = "pattern_position";    
    public static final String TEXT_POSTIION_TAG_NAME = "text_position";

    public WipPosition() {
        super(new Element(TAG_NAME));
    }

    public WipPosition(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public WipPosition(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public String getWordText( ){  return getNodeText(WORD_TEXT_TAG_NAME);}
    public String getWordTextPattern( ){  return getNodeText(WORD_TEXT_PATTERN_TAG_NAME);}
    public int getWordLength( ){  return Integer.parseInt(getNodeText(WORD_LENGTH_TAG_NAME));}
    public int getPatternPosition(){    return Integer.parseInt(getNodeText(PATTERN_POSITION_TAG_NAME));}
    public int getTextPosition(){   return Integer.parseInt(getNodeText(TEXT_POSTIION_TAG_NAME));}


    //----------------SETTERS---------------------
    public void setWordText(String value){  setNodeText(WORD_TEXT_TAG_NAME, value);}
    public void setWordTextPattern(String value){   setNodeText(WORD_TEXT_PATTERN_TAG_NAME, value);}
    public void setWordLength(int value){   setNodeText(WORD_LENGTH_TAG_NAME, String.valueOf(value));}
    public void setPatternPosition(int value){  setNodeText(PATTERN_POSITION_TAG_NAME, String.valueOf(value));}
    public void setTextPosition(int value){ setNodeText(TEXT_POSTIION_TAG_NAME, String.valueOf(value));}

}
