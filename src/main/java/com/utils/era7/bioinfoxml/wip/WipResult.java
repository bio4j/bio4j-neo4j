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

package com.era7.bioinfoxml.wip;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class WipResult extends XMLElement{

    public static final String TAG_NAME = "wip_result";
    
    public static final String ID_TAG_NAME = "id";
    public static final String RANDOM_SEQUENCES_NUMBER_TAG_NAME = "random_sequences_number";
    public static final String RESULT_TAG_NAME = "result";
    public static final String POSITIONS_TAG_NAME = "positions";
    public static final String STATISTIC_VALUE_TAG_NAME = "statistic_value";
    public static final String ALGORITHM_VERSION_ID_TAG_NAME = "algorithm_version_id";
    public static final String DATE_TAG_NAME = "date";
    public static final String ABSOLUTE_FREQUENCY_TAG_NAME = "absolute_frequency";
    public static final String REAL_CASE_TAG_NAME = "real_case";

    public static final String REGION_A_TAG_NAME = "region_a";
    public static final String REGION_B_TAG_NAME = "region_b";



    public WipResult() {
        super(new Element(TAG_NAME));
    }

    public WipResult(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public WipResult(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }


    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public int getRandomSequencesNumber(){  return Integer.parseInt(getNodeText(RANDOM_SEQUENCES_NUMBER_TAG_NAME));}
    public String getResult(){  return getNodeText(RESULT_TAG_NAME);}
    public Element getPositions(){  return root.getChild(POSITIONS_TAG_NAME);}
    public double getStatisticValue(){  return Double.parseDouble(getNodeText(STATISTIC_VALUE_TAG_NAME));}
    public String getAlgorithmVersionId(){  return getNodeText(ALGORITHM_VERSION_ID_TAG_NAME);}
    public String getDate(){    return getNodeText(DATE_TAG_NAME);}
    public double getAbsoluteFrequency(){  return Double.parseDouble(getNodeText(ABSOLUTE_FREQUENCY_TAG_NAME));}
    public String getRealCase(){    return getNodeText(REAL_CASE_TAG_NAME);}

    //----------------SETTERS---------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setRandomSequencesNumber(int value){    setNodeText(RANDOM_SEQUENCES_NUMBER_TAG_NAME, String.valueOf(value));}
    public void setResult(String value){    setNodeText(RESULT_TAG_NAME, value);}
    public void setStatisticValue(double value){    setNodeText(STATISTIC_VALUE_TAG_NAME, String.valueOf(value));}
    public void setAlgorithmVersionId(String value){    setNodeText(ALGORITHM_VERSION_ID_TAG_NAME, value);}
    public void setDate(String value){  setNodeText(DATE_TAG_NAME, value);}
    public void setAbsoluteFrequency(double value){    setNodeText(ABSOLUTE_FREQUENCY_TAG_NAME, String.valueOf(value));}
    public void setRealCase(String value){  setNodeText(REAL_CASE_TAG_NAME, value);}

    public void addPosition(WipPosition p){
        Element elem = initPositionsTag();
        elem.addContent(p.getRoot());
    }

    public void setRegionA(Region r){
        root.removeChildren(REGION_A_TAG_NAME);
        initRegionATag().addContent(r.asJDomElement());
    }
    public void setRegionB(Region r){
        root.removeChildren(REGION_B_TAG_NAME);
        initRegionBTag().addContent(r.asJDomElement());
    }


    private Element initPositionsTag(){
        Element elem = root.getChild(POSITIONS_TAG_NAME);
        if(elem == null){
            elem = new Element(POSITIONS_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    private Element initRegionATag(){
        Element elem = root.getChild(REGION_A_TAG_NAME);
        if(elem == null){
            elem = new Element(REGION_A_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }
    private Element initRegionBTag(){
        Element elem = root.getChild(REGION_B_TAG_NAME);
        if(elem == null){
            elem = new Element(REGION_B_TAG_NAME);
            root.addContent(elem);
        }
        return elem;
    }

}
