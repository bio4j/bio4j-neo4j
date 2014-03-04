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
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Iteration extends XMLElement {

    public static final String TAG_NAME = "Iteration";
    public static final String ITERATION_HITS_TAG_NAME = "Iteration_hits";
    public static final String ITERATION_QUERY_DEF_TAG_NAME = "Iteration_query-def";
    public static final String ITERATION_QUERY_LEN_TAG_NAME = "Iteration_query-len";
    public static final String ITERATION_QUERY_ID_TAG_NAME = "Iteration_query-ID";

    public Iteration() {
        super(new Element(TAG_NAME));
    }

    public Iteration(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public Iteration(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }

    //----------------GETTERS---------------------
    public String getQueryDef() {        return getNodeText(ITERATION_QUERY_DEF_TAG_NAME);    }
    public String getQueryLen(){    return getNodeText(ITERATION_QUERY_LEN_TAG_NAME);}
    public String getQueryId(){ return getNodeText(ITERATION_QUERY_ID_TAG_NAME);}

    //----------------SETTERS---------------------
    public void setQueryDef(String value){  setNodeText(ITERATION_QUERY_DEF_TAG_NAME, value);}
    public void setQueryLen(String value){  setNodeText(ITERATION_QUERY_LEN_TAG_NAME, value);}
    public void setQueryId(String value){  setNodeText(ITERATION_QUERY_ID_TAG_NAME, value);}



    public String getUniprotIdFromQueryDef() {
        String text = getNodeText(ITERATION_QUERY_DEF_TAG_NAME);
        if (text != null) {
            return text.split("\\|")[1].trim();
        } else {
            return null;
        }

    }

    public ArrayList<Hit> getIterationHits() throws XMLElementException {
        ArrayList<Hit> array = new ArrayList<Hit>();

        Element itHits = root.getChild(ITERATION_HITS_TAG_NAME);
        if (itHits != null) {
            List<Element> tempList = itHits.getChildren();
            for (Element elem : tempList) {
                array.add(new Hit(elem));
            }
        }

        return array;

    }
}
