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

package com.era7.bioinfoxml.go;

import com.era7.era7xmlapi.model.XMLElement;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class SlimSetXML extends XMLElement{

    public static final String TAG_NAME = "slim_set";

    public static final String NAME_TAG_NAME = "name";

    public SlimSetXML() {
        super(new Element(TAG_NAME));
    }

    public SlimSetXML(String value) throws Exception {
        super(value);
    }

    public SlimSetXML(Element element) {
        super(element);
    }


    public void addGoTerm(GoTermXML term){
        root.addContent(term.getRoot());
    }


    //----------------SETTERS-------------------
    public void setSlimSetName(String value) {    setNodeText(NAME_TAG_NAME, value);  }

    //----------------GETTERS---------------------
    public String getGoSlimName() {   return getNodeText(NAME_TAG_NAME);   }
    

}
