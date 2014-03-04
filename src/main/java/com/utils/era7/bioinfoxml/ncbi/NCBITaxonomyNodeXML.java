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
package com.era7.bioinfoxml.ncbi;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class NCBITaxonomyNodeXML extends XMLElement{
    
    public static final String TAG_NAME = "ncbi_taxonomy_node";
    
    public static final String TAX_ID_TAG_NAME = "tax_id";
    public static final String PARENT_TAX_ID_TAG_NAME = "parent_tax_id";
    public static final String SCIENTIFIC_NAME_TAG_NAME = "scientific_name";
    public static final String RANK_TAG_NAME = "rank";
    public static final String EMBL_CODE_TAG_NAME = "embl_code";
    public static final String COMMENTS_TAG_NAME = "comments";
    public static final String ABSOLUTE_FREQUENCY_TAG_NAME = "absolute_frequency";
    public static final String ACCUMULATED_ABSOLUTE_FREQUENCY_TAG_NAME = "accumulated_absolute_frequency";
    
    public NCBITaxonomyNodeXML(){
        super(new Element(TAG_NAME));
    }
    public NCBITaxonomyNodeXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public NCBITaxonomyNodeXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
        
    //----------------GETTERS---------------------
    public int getTaxId( ){  return Integer.parseInt(getNodeText(TAX_ID_TAG_NAME));}
    public String getParentTaxId( ){  return getNodeText(PARENT_TAX_ID_TAG_NAME);}
    public String getRank(){    return getNodeText(RANK_TAG_NAME);}
    public String getEmblCode(){    return getNodeText(EMBL_CODE_TAG_NAME);}
    public String getComments(){    return getNodeText(COMMENTS_TAG_NAME);}
    public String getScientificName(){    return getNodeText(SCIENTIFIC_NAME_TAG_NAME);}
    public int getAbsoluteFrequency(){  return Integer.parseInt(getNodeText(ABSOLUTE_FREQUENCY_TAG_NAME));}
    public int getAccumulatedAbsoluteFrequency(){   return Integer.parseInt(getNodeText(ACCUMULATED_ABSOLUTE_FREQUENCY_TAG_NAME));}
    
    //----------------SETTERS-------------------
    public void setTaxId(int value){  setNodeText(TAX_ID_TAG_NAME, String.valueOf(value));}
    public void setParentTaxId(String value){  setNodeText(PARENT_TAX_ID_TAG_NAME, value);}
    public void setRank(String value){  setNodeText(RANK_TAG_NAME, value);}
    public void setEmblCode(String value){  setNodeText(EMBL_CODE_TAG_NAME, value);}
    public void setComments(String value){  setNodeText(COMMENTS_TAG_NAME, value);}
    public void setScientificName(String value){  setNodeText(SCIENTIFIC_NAME_TAG_NAME, value);}
    public void setAbsoluteFrequency(int value){  setNodeText(ABSOLUTE_FREQUENCY_TAG_NAME, String.valueOf(value));}
    public void setAccumulatedAbsoluteFrequency(int value){  setNodeText(ACCUMULATED_ABSOLUTE_FREQUENCY_TAG_NAME, String.valueOf(value));}
    
    
}
