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

package com.era7.bioinfoxml.graphml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GraphXML extends XMLElement{

    public static final String TAG_NAME = "graph";

    public static final String EDGE_DEFAULT_ATTRIBUTE = "edgedefault";
    public static final String ID_ATTRIBUTE = "id";

    public static final String DIRECTED_EDGE_TYPE = "directed";
    public static final String UNDIRECTED_EDGE_TYPE = "undirected";


    public GraphXML(){
        super(new Element(TAG_NAME));
    }
    public GraphXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public GraphXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setDefaultEdgeType(String value){
        this.root.setAttribute(EDGE_DEFAULT_ATTRIBUTE, value);
    }
    public void setId(String value){
        this.root.setAttribute(ID_ATTRIBUTE, value);
    }
    
    
    public void addNode(NodeXML node){
        addChild(node);
    }
    public void addEdge(EdgeXML edge){
        addChild(edge);
    }
    
}
