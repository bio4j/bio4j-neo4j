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

package com.era7.bioinfoxml.gexf;

import com.era7.bioinfoxml.gexf.viz.VizColorXML;
import com.era7.bioinfoxml.gexf.viz.VizPositionXML;
import com.era7.bioinfoxml.gexf.viz.VizSizeXML;
import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class NodeXML extends XMLElement{

    public static final String TAG_NAME = "node";

    public static final String ID_ATTRIBUTE = "id";
    public static final String LABEL_ATTRIBUTE = "label";
    public static final String START_ATTRIBUTE = "start";
    public static final String END_ATTRIBUTE = "end";


    public NodeXML(){
        super(new Element(TAG_NAME));
    }
    public NodeXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public NodeXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setId(String value){
        this.root.setAttribute(ID_ATTRIBUTE, value);
    }
    public void setStart(String value){
        this.root.setAttribute(START_ATTRIBUTE, value);
    }
    public void setEnd(String value){
        this.root.setAttribute(END_ATTRIBUTE, value);
    }
    public void setLabel(String value){
        this.root.setAttribute(LABEL_ATTRIBUTE,value);
    }
    public void setColor(VizColorXML vizColor){
        this.root.removeChildren(VizColorXML.TAG_NAME);
        this.root.addContent(vizColor.asJDomElement());
    }
    public void setSize(VizSizeXML vizSize){
        this.root.removeChildren(VizSizeXML.TAG_NAME);
        this.root.addContent(vizSize.asJDomElement());
    }
    public void setPosition(VizPositionXML vizPosition){
        this.root.removeChildren(VizPositionXML.TAG_NAME);
        this.root.addContent(vizPosition.asJDomElement());
    }

    public void setAttvalues(AttValuesXML attValuesXML){
        this.root.removeChildren(AttValuesXML.TAG_NAME);
        this.root.addContent(attValuesXML.asJDomElement());
    }
    public void setSpells(SpellsXML spellsXML){
        this.root.removeChildren(SpellsXML.TAG_NAME);
        this.root.addContent(spellsXML.asJDomElement());
    }

    //----------------GETTERS-------------------

    public String getLabel(){
        return this.root.getAttributeValue(LABEL_ATTRIBUTE);
    }


}
