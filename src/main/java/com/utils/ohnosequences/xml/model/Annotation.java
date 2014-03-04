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
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Annotation extends XMLElement {

    public static final String TAG_NAME = "annotation";

    public Annotation() {
        super(new Element(TAG_NAME));
    }

    public Annotation(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public Annotation(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }

    public void setPredictedGenes(PredictedGenes value) {
        root.removeChildren(PredictedGenes.TAG_NAME);
        root.addContent(value.asJDomElement());
    }

    public void setPredictedRnas(PredictedRnas value) {
        root.removeChildren(PredictedRnas.TAG_NAME);
        root.addContent(value.asJDomElement());
    }

    public PredictedGenes getPredictedGenes() throws XMLElementException {

        PredictedGenes pGenes = null;
        Element elem = root.getChild(PredictedGenes.TAG_NAME);
        if (elem != null) {
            pGenes = new PredictedGenes(elem);
        }

        return pGenes;
    }

    public PredictedRnas getPredictedRnas() throws XMLElementException {
        
        PredictedRnas pRnas = null;
        Element elem = root.getChild(PredictedRnas.TAG_NAME);
        if (elem != null) {
            pRnas = new PredictedRnas(elem);
        }

        return pRnas;
    }
}
