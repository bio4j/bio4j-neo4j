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
package com.era7.bioinfoxml.pg;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

public class Primer extends XMLElement {

    public static final String TAG_NAME = "primer";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String SEQUENCE_COMPLEMENTARY_INVERTED_TAG_NAME = "sequence_complementary_inverted";
    public static final String LENGTH_TAG_NAME = "length";

    public Primer() {
        super(new Element(TAG_NAME));

    }

    public Primer(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public Primer(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }

//----------------GETTERS-------------------
    public String getSequenceComplementaryInverted() {
        return getNodeText(SEQUENCE_COMPLEMENTARY_INVERTED_TAG_NAME);
    }

    public String getSequence() {
        return getNodeText(SEQUENCE_TAG_NAME);
    }

    public int getLength() {
        return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));
    }

//----------------SETTERS-------------------
    public void setSequenceComplementaryInverted(String value) {
        setNodeText(SEQUENCE_COMPLEMENTARY_INVERTED_TAG_NAME, value);
    }

    public void setSequence(String value) {
        setNodeText(SEQUENCE_TAG_NAME, value);
    }

    public void setLength(int value) {
        setNodeText(LENGTH_TAG_NAME, String.valueOf(value));
    }
}
