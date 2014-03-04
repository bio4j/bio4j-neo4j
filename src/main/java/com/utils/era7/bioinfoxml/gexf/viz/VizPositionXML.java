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
package com.era7.bioinfoxml.gexf.viz;

import com.era7.era7xmlapi.model.NameSpace;
import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;


/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class VizPositionXML extends XMLElement{

    public static final String TAG_NAME = "position";

    public static final String VIZ_NAMESPACE = "viz";

    public static final String X_ATTRIBUTE = "x";
    public static final String Y_ATTRIBUTE = "y";
    public static final String Z_ATTRIBUTE = "z";

    public VizPositionXML(){
        super(new Element(TAG_NAME));
        this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
    }
    public VizPositionXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }else{
            this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
        }
    }
    public VizPositionXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }else{
            this.setNameSpace(new NameSpace(VIZ_NAMESPACE, VIZ_NAMESPACE));
        }
    }

    public VizPositionXML(double x, double y, double z){
        super(new Element(TAG_NAME));
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    //----------------SETTERS-------------------
    public final void setX(double value){
        this.root.setAttribute(X_ATTRIBUTE, String.valueOf(value));
    }
    public final void setY(double value){
        this.root.setAttribute(Y_ATTRIBUTE, String.valueOf(value));
    }
    public final void setZ(double value){
        this.root.setAttribute(Z_ATTRIBUTE, String.valueOf(value));
    }

}
