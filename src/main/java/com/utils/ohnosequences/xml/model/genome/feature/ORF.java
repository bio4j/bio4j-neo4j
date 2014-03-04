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

package com.ohnosequences.xml.model.genome.feature;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class ORF extends Feature{

    public static final String TAG_NAME = "orf";

    public static final String SYNONYM_TAG_NAME = "synonym";
    public static final String GENE_TAG_NAME = "gene";

    public ORF() throws XMLElementException{
        super(new Element(TAG_NAME));
    }
    public ORF(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public ORF(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //-----------------------GETTERS------------------------
    public String getSynonym(){ return getNodeText(SYNONYM_TAG_NAME);}
    public String getGene(){    return getNodeText(GENE_TAG_NAME);}

    //-----------------------SETTERS------------------------
    public void setSynonym(String value){   setNodeText(SYNONYM_TAG_NAME, value);}
    public void setGene(String value){  setNodeText(GENE_TAG_NAME,value);}

}
