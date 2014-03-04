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

package com.ohnosequences.xml.model.gexf.viz;

import com.ohnosequences.xml.api.model.NameSpace;
import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;


/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class VizSizeXML extends XMLElement{

    public static final String TAG_NAME = "size";

    public static final String VIZ_NAMESPACE = "viz";

    public static final String VALUE_ATTRIBUTE = "value";

    public VizSizeXML(){
        super(new Element(TAG_NAME));
        this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
    }
    public VizSizeXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }else{
            this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
        }
    }
    public VizSizeXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }else{
            this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
        }
    }

    public VizSizeXML(double value){
        super(new Element(TAG_NAME));
        this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
        setValue(value);
    }

    //----------------SETTERS-------------------
    public final void setValue(double value){
        this.root.setAttribute(VALUE_ATTRIBUTE, String.valueOf(value));
    }

}
