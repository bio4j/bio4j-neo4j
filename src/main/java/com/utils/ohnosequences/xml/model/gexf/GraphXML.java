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

package com.ohnosequences.xml.model.gexf;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GraphXML extends XMLElement{

    public static final String TAG_NAME = "graph";

    public static final String DEFAULT_EDGE_TYPE_ATTRIBUTE = "defaultedgetype";
    public static final String MODE_ATTRIBUTE = "mode";
    public static final String TIME_FORMAT_ATTRIBUTE = "timeformat";

    public static final String DIRECTED_EDGE_TYPE = "directed";
    public static final String DYNAMIC_MODE = "dynamic";
    public static final String DATE_TIME_FORMAT = "date";


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
        this.root.setAttribute(MODE_ATTRIBUTE, value);
    }
    public void setMode(String value){
        this.root.setAttribute(DEFAULT_EDGE_TYPE_ATTRIBUTE, value);
    }
    public void setTimeformat(String value){
        this.root.setAttribute(TIME_FORMAT_ATTRIBUTE, value);
    }
    public void setNodes(NodesXML value){
        this.root.removeChildren(NodesXML.TAG_NAME);
        this.root.addContent(value.getRoot());
    }
    public void setEdges(EdgesXML value){
        this.root.removeChildren(EdgesXML.TAG_NAME);
        this.root.addContent(value.getRoot());
    }

    public void addAttributes(AttributesXML attributesXML){
        this.root.addContent(attributesXML.asJDomElement());
    }

}
