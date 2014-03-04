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

package com.ohnosequences.xml.model.util;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class ScheduledExecutions extends XMLElement{

    public static final String TAG_NAME = "scheduled_executions";

    public ScheduledExecutions(){
        super(new Element(TAG_NAME));
    }
    public ScheduledExecutions(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public ScheduledExecutions(String value) throws Exception{
        super(value);

        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }


    public void addExecution(Execution exec){
        addChild(exec);
    }

    //----------------GETTERS---------------------
    public ArrayList<Execution> getExecutions() throws XMLElementException{
        ArrayList<Execution> array = new ArrayList<Execution>();
        List<Element> list = root.getChildren(Execution.TAG_NAME);
        for(Element elem : list){
            Execution temp = new Execution(elem);
            array.add(temp);
        }
        return array;
    }

    //----------------SETTERS---------------------
}

