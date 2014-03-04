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

package com.ohnosequences.xml.model.logs;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class LogRecordXML extends XMLElement{
    
    public static final String TAG_NAME = "log_record";

    public static final String SOURCE_TAG_NAME = "source";
    public static final String DATE_TAG_NAME = "date";
    public static final String DESCRIPTION_TAG_NAME = "description";
    public static final String SOURCE_IP_TAG_NAME = "source_ip";

    public LogRecordXML(){
        super(new Element(TAG_NAME));
    }
    public LogRecordXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public LogRecordXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }


    public void setSource(String value){  setNodeText(SOURCE_TAG_NAME, value);}
    public void setDescription(String value){  setNodeText(DESCRIPTION_TAG_NAME, value);}
    public void setSourceIP(String value){  setNodeText(SOURCE_IP_TAG_NAME,value);}
    public void setDate(String value){ setNodeText(DATE_TAG_NAME,value);}

    public String getSource(){     return getNodeText(SOURCE_TAG_NAME);   }
    public String getDescription(){     return getNodeText(DESCRIPTION_TAG_NAME);   }
    public String getSourceIP(){     return getNodeText(SOURCE_IP_TAG_NAME);   }
    public String getDate(){    return getNodeText(DATE_TAG_NAME);   }


}
