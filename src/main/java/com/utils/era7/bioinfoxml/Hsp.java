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

package com.era7.bioinfoxml;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes
 */
public class Hsp extends XMLElement{

    public static final String TAG_NAME = "Hsp";

    public static final String NUM_TAG_NAME = "Hsp_num";
    public static final String BIT_SCORE_TAG_NAME = "Hsp_bit-score";
    public static final String SCORE_TAG_NAME = "Hsp_score";
    public static final String EVALUE_TAG_NAME = "Hsp_evalue";
    public static final String QUERY_FROM_TAG_NAME = "Hsp_query-from";
    public static final String QUERY_TO_TAG_NAME = "Hsp_query-to";
    public static final String QUERY_FRAME_TAG_NAME = "Hsp_query-frame";
    public static final String HIT_FROM_TAG_NAME = "Hsp_hit-from";
    public static final String HIT_TO_TAG_NAME = "Hsp_hit-to";
    public static final String HIT_FRAME_TAG_NAME = "Hsp_hit-frame";
    public static final String POSITIVE_TAG_NAME = "Hsp_positive";
    public static final String IDENTITY_TAG_NAME = "Hsp_identity";
    public static final String GAPS_TAG_NAME = "Hsp_gaps";
    public static final String ALIGN_LEN_TAG_NAME = "Hsp_align-len";
    public static final String QSEQ_TAG_NAME = "Hsp_qseq";
    public static final String HSEQ_TAG_NAME = "Hsp_hseq";
    public static final String MIDLINE_TAG_NAME = "Hsp_midline";
    
    
    public Hsp(){
        super(new Element(TAG_NAME));
    }
    public Hsp(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public Hsp(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    

    //----------------SETTERS-------------------
    public void setNum(String value){  setNodeText(NUM_TAG_NAME, value);}
    public void setScore(String value){ setNodeText(SCORE_TAG_NAME, value);}
    public void setBitScore(String value){ setNodeText(BIT_SCORE_TAG_NAME, value);}
    public void setEvalue(String value){    setNodeText(EVALUE_TAG_NAME, value);}
    public void setQueryFrom(int value){   setNodeText(QUERY_FROM_TAG_NAME, String.valueOf(value));}
    public void setQueryTo(int value){ setNodeText(QUERY_TO_TAG_NAME, String.valueOf(value));}
    public void setQueryFrame(int value){   setNodeText(QUERY_FRAME_TAG_NAME, String.valueOf(value));}
    public void setHitFrom(int value){    setNodeText(HIT_FROM_TAG_NAME, String.valueOf(value));}
    public void setHitTo(int value){    setNodeText(HIT_TO_TAG_NAME, String.valueOf(value));}
    public void setHitFrame(int value){    setNodeText(HIT_FRAME_TAG_NAME, String.valueOf(value));}
    public void setIdentity(String value){    setNodeText(IDENTITY_TAG_NAME, value);}
    public void setPositive(String value){  setNodeText(POSITIVE_TAG_NAME, value);}
    public void setGaps(String value){  setNodeText(GAPS_TAG_NAME, value);}
    public void setAlignLen(String value){  setNodeText(ALIGN_LEN_TAG_NAME, value);}
    public void setQSeq(String value){  setNodeText(QSEQ_TAG_NAME, value);}
    public void setHSeq(String value){  setNodeText(HSEQ_TAG_NAME, value);}
    public void setMidline(String value){  setNodeText(MIDLINE_TAG_NAME, value);}


    //----------------GETTERS---------------------
    public String getNum( ){  return getNodeText(NUM_TAG_NAME);}
    public String getScore(){   return getNodeText(SCORE_TAG_NAME);}
    public String getBitScore(){   return getNodeText(BIT_SCORE_TAG_NAME);}
    public String getEvalue( ){  return getNodeText(EVALUE_TAG_NAME);}
    public double getEvalueDoubleFormat(){
        String[] array = getEvalue().split("e");
        if(array.length < 2){
            //System.out.println("evalue = " + getEvalue());
            return Double.valueOf(array[0]);
        }else{
            return Double.valueOf(array[0]) * Math.pow(10.0, Double.valueOf(array[1]));
        }
    }
    public int getQueryFrom( ){  return Integer.parseInt(getNodeText(QUERY_FROM_TAG_NAME));}
    public int getQueryTo( ){  return Integer.parseInt(getNodeText(QUERY_TO_TAG_NAME));}
    public int getQueryFrame(){ return Integer.parseInt(getNodeText(QUERY_FRAME_TAG_NAME));}
    public int getHitFrom( ){  return Integer.parseInt(getNodeText(HIT_FROM_TAG_NAME));}
    public int getHitTo( ){  return Integer.parseInt(getNodeText(HIT_TO_TAG_NAME));}
    public int getHitFrame( ){  return Integer.parseInt(getNodeText(HIT_FRAME_TAG_NAME));}
    public String getIdentity( ){  return getNodeText(IDENTITY_TAG_NAME);}
    public boolean getOrientation(){    return getHitFrame() > 0;}
    public String getPositive(){    return getNodeText(POSITIVE_TAG_NAME);}
    public String getGaps(){    return getNodeText(GAPS_TAG_NAME);}
    public String getAlignLen(){    return getNodeText(ALIGN_LEN_TAG_NAME);}
    public String getQSeq(){    return getNodeText(QSEQ_TAG_NAME);}
    public String getHSeq(){    return getNodeText(HSEQ_TAG_NAME);}
    public String getMidline(){    return getNodeText(MIDLINE_TAG_NAME);}

    

}
