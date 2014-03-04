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

package com.era7.bioinfoxml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class BlastOutput extends XMLElement{

    public static final String TAG_NAME = "BlastOutput";

    public static final String PROGRAM_TAG_NAME = "program";
    public static final String VERSION_TAG_NAME = "version";
    public static final String REFERENCE_TAG_NAME = "reference";
    public static final String DB_TAG_NAME = "db";
    public static final String QUERY_ID_TAG_NAME = "query_id";
    public static final String QUERY_DEF_TAG_NAME = "query_def";
    public static final String QUERY_LEN_TAG_NAME = "query_len";

    public static final String BLAST_OUTPUT_ITERATIONS_TAG_NAME = "BlastOutput_iterations";

    public BlastOutput(){
        super(new Element(TAG_NAME));
    }
    public BlastOutput(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public BlastOutput(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setType(String type){    setNodeText(PROGRAM_TAG_NAME, type);}
    public void setVersion(String type){    setNodeText(VERSION_TAG_NAME, type);}
    public void setReference(String type){    setNodeText(REFERENCE_TAG_NAME, type);}
    public void setDb(String type){    setNodeText(DB_TAG_NAME, type);}
    public void setQueryId(String type){    setNodeText(QUERY_ID_TAG_NAME, type);}
    public void setQueryDef(String type){    setNodeText(QUERY_DEF_TAG_NAME, type);}
    public void setQueryLen(String type){    setNodeText(QUERY_LEN_TAG_NAME, type);}

    //----------------GETTERS---------------------
    public String getType(){       return getNodeText(PROGRAM_TAG_NAME);  }
    public String getVersion(){ return getNodeText(VERSION_TAG_NAME);}
    public String getReference(){   return getNodeText(REFERENCE_TAG_NAME);}
    public String getDb(){  return getNodeText(DB_TAG_NAME);}
    public String getQueryId(){ return getNodeText(QUERY_ID_TAG_NAME);}
    public String getQueryDef(){    return getNodeText(QUERY_DEF_TAG_NAME);}
    public String getQueryLen(){    return getNodeText(QUERY_LEN_TAG_NAME);}


    public ArrayList<Iteration> getBlastOutputIterations( ) throws XMLElementException{
        ArrayList<Iteration> array = new ArrayList<Iteration>();
        List<Element> tempList = root.getChild(BLAST_OUTPUT_ITERATIONS_TAG_NAME).getChildren();
        for(Element elem : tempList){
            array.add(new Iteration(elem));
        }
        return array;

    }

}
