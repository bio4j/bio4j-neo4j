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

package com.ohnosequences.xml.model.genome.feature;

import com.ohnosequences.xml.model.pal.PalindromicityResultXML;
import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class Feature extends XMLElement implements Comparable<Feature>{

    public static final String POSITIVE_STRAND = "+";
    public static final String NEGATIVE_STRAND = "-";

    public static final String ID_TAG_NAME = "id";
    public static final String NAME_TAG_NAME = "name";
    public static final String BEGIN_TAG_NAME = "begin";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String STRAND_TAG_NAME = "strand";
    public static final String LEADING_STRAND_TAG_NAME = "leading_strand";
    public static final String LENGTH_TAG_NAME = "length";
    public static final String END_TAG_NAME = "end";
    public static final String ERROR_TAG_NAME = "error";
    
    public static final String TOTAL_A_TAG_NAME = "total_a";
    public static final String TOTAL_C_TAG_NAME = "total_c";
    public static final String TOTAL_G_TAG_NAME = "total_g";
    public static final String TOTAL_T_TAG_NAME = "total_t";
    public static final String TOTAL_OTHER_TAG_NAME = "total_other";

    public static final String A_IN_1_TAG_NAME = "a_in_1";
    public static final String C_IN_1_TAG_NAME = "c_in_1";
    public static final String G_IN_1_TAG_NAME = "g_in_1";
    public static final String T_IN_1_TAG_NAME = "t_in_1";
    public static final String OTHER_IN_1_TAG_NAME = "other_in_1";

    public static final String A_IN_2_TAG_NAME = "a_in_2";
    public static final String C_IN_2_TAG_NAME = "c_in_2";
    public static final String G_IN_2_TAG_NAME = "g_in_2";
    public static final String T_IN_2_TAG_NAME = "t_in_2";
    public static final String OTHER_IN_2_TAG_NAME = "other_in_2";

    public static final String A_IN_3_TAG_NAME = "a_in_3";
    public static final String C_IN_3_TAG_NAME = "c_in_3";
    public static final String G_IN_3_TAG_NAME = "g_in_3";
    public static final String T_IN_3_TAG_NAME = "t_in_3";
    public static final String OTHER_IN_3_TAG_NAME = "other_in_3";

    public static final String A_IN_SILENT_3_TAG_NAME = "a_in_silent_3";
    public static final String C_IN_SILENT_3_TAG_NAME = "c_in_silent_3";
    public static final String G_IN_SILENT_3_TAG_NAME = "g_in_silent_3";
    public static final String T_IN_SILENT_3_TAG_NAME = "t_in_silent_3";
    public static final String OTHER_IN_SILENT_3_TAG_NAME = "other_in_silent_3";

    //----------PALINDROMICITY-----------
    public static final String ODD_AXIS_PALINDROMICITY_TAG_NAME = "odd_axis_pal";
    public static final String EVEN_AXIS_PALINDROMICITY_TAG_NAME = "even_axis_pal";
    public static final String MAX_WORD_LENGTH_EVEN_AXIS_PALINDROMICITY_TAG_NAME = "mwl_even_axis_pal";
    public static final String MAX_WORD_LENGTH_ODD_AXIS_PALINDROMICITY_TAG_NAME = "mwl_odd_axis_pal";


    public Feature(Element elem) throws XMLElementException{
        super(elem);
    }
    public Feature(String value) throws Exception{
        super(value);
    }

    public void appendToSequence(String value){
        Element seqElem = root.getChild(SEQUENCE_TAG_NAME);
        if(seqElem == null){
            root.addContent(new Element(SEQUENCE_TAG_NAME));
            seqElem = root.getChild(SEQUENCE_TAG_NAME);
        }
        seqElem.setText(seqElem.getText()+value);
    }

    //----------------GETTERS---------------------
    public String getId(){  return getNodeText(ID_TAG_NAME); }
    public String getFeatureName(){    return getNodeText(NAME_TAG_NAME);}
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}
    public String getStrand(){  return getNodeText(STRAND_TAG_NAME);}
    public boolean getLeadingStrand(){  return Boolean.parseBoolean(getNodeText(LEADING_STRAND_TAG_NAME));}
    public int getLength() {    return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));}
    public int getBegin(){  return Integer.parseInt(getNodeText(BEGIN_TAG_NAME)); }
    public int getEnd(){  return Integer.parseInt(getNodeText(END_TAG_NAME)); }
    public String getError(){   return getNodeText(ERROR_TAG_NAME);}

    public float getTotalA(){ return Float.parseFloat(getNodeText(TOTAL_A_TAG_NAME));}
    public float getTotalC(){ return Float.parseFloat(getNodeText(TOTAL_C_TAG_NAME));}
    public float getTotalG(){ return Float.parseFloat(getNodeText(TOTAL_G_TAG_NAME));}
    public float getTotalT(){ return Float.parseFloat(getNodeText(TOTAL_T_TAG_NAME));}
    public float getTotalOther(){   return Float.parseFloat(getNodeText(TOTAL_OTHER_TAG_NAME));}

    public float getAIn1(){   return Float.parseFloat(getNodeText(A_IN_1_TAG_NAME));}
    public float getCIn1(){   return Float.parseFloat(getNodeText(C_IN_1_TAG_NAME));}
    public float getGIn1(){   return Float.parseFloat(getNodeText(G_IN_1_TAG_NAME));}
    public float getTIn1(){   return Float.parseFloat(getNodeText(T_IN_1_TAG_NAME));}
    public float getOtherIn1(){ return Float.parseFloat(getNodeText(OTHER_IN_1_TAG_NAME));}

    public float getAIn2(){   return Float.parseFloat(getNodeText(A_IN_2_TAG_NAME));}
    public float getCIn2(){   return Float.parseFloat(getNodeText(C_IN_2_TAG_NAME));}
    public float getGIn2(){   return Float.parseFloat(getNodeText(G_IN_2_TAG_NAME));}
    public float getTIn2(){   return Float.parseFloat(getNodeText(T_IN_2_TAG_NAME));}
    public float getOtherIn2(){ return Float.parseFloat(getNodeText(OTHER_IN_2_TAG_NAME));}

    public float getAIn3(){   return Float.parseFloat(getNodeText(A_IN_3_TAG_NAME));}
    public float getCIn3(){   return Float.parseFloat(getNodeText(C_IN_3_TAG_NAME));}
    public float getGIn3(){   return Float.parseFloat(getNodeText(G_IN_3_TAG_NAME));}
    public float getTIn3(){   return Float.parseFloat(getNodeText(T_IN_3_TAG_NAME));}
    public float getOtherIn3(){ return Float.parseFloat(getNodeText(OTHER_IN_3_TAG_NAME));}

    public float getAInSilent3(){   return Float.parseFloat(getNodeText(A_IN_SILENT_3_TAG_NAME));}
    public float getCInSilent3(){   return Float.parseFloat(getNodeText(C_IN_SILENT_3_TAG_NAME));}
    public float getGInSilent3(){   return Float.parseFloat(getNodeText(G_IN_SILENT_3_TAG_NAME));}
    public float getTInSilent3(){   return Float.parseFloat(getNodeText(T_IN_SILENT_3_TAG_NAME));}
    public float getOtherInSilent3(){   return Float.parseFloat(getNodeText(OTHER_IN_SILENT_3_TAG_NAME));}

    public PalindromicityResultXML getOddAxisPalindromicityResult() throws XMLElementException{
        Element elem = root.getChild(ODD_AXIS_PALINDROMICITY_TAG_NAME);
        if(elem == null){
            return null;
        }else{
            Element palResult = elem.getChild(PalindromicityResultXML.TAG_NAME);
            if(palResult == null){
                return null;
            }else{
                return new PalindromicityResultXML(palResult);
            }
        }
    }
    public PalindromicityResultXML getEvenAxisPalindromicityResult() throws XMLElementException{
        Element elem = root.getChild(EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        if(elem == null){
            return null;
        }else{
            Element palResult = elem.getChild(PalindromicityResultXML.TAG_NAME);
            if(palResult == null){
                return null;
            }else{
                return new PalindromicityResultXML(palResult);
            }
        }
    }
    public PalindromicityResultXML getMaxWordLengthEvenAxisPalindromicityResult() throws XMLElementException{
        Element elem = root.getChild(MAX_WORD_LENGTH_EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        if(elem == null){
            return null;
        }else{
            Element palResult = elem.getChild(PalindromicityResultXML.TAG_NAME);
            if(palResult == null){
                return null;
            }else{
                return new PalindromicityResultXML(palResult);
            }
        }
    }
    public PalindromicityResultXML getMaxWordLengthOddAxisPalindromicityResult() throws XMLElementException{
        Element elem = root.getChild(MAX_WORD_LENGTH_ODD_AXIS_PALINDROMICITY_TAG_NAME);
        if(elem == null){
            return null;
        }else{
            Element palResult = elem.getChild(PalindromicityResultXML.TAG_NAME);
            if(palResult == null){
                return null;
            }else{
                return new PalindromicityResultXML(palResult);
            }
        }
    }


    //----------------SETTERS---------------------
    public void setId(String value){    setNodeText(ID_TAG_NAME,value);}
    public void setFeatureName(String value){  setNodeText(NAME_TAG_NAME, value);}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME,value);}
    public void setStrand(String value){    setNodeText(STRAND_TAG_NAME,value);}
    public void setLeadingStrand(boolean value){    setNodeText(LEADING_STRAND_TAG_NAME, String.valueOf(value));}
    public void setLength(int value){   setNodeText(LENGTH_TAG_NAME,String.valueOf(value));}
    public void setBegin(int value){    setNodeText(BEGIN_TAG_NAME,String.valueOf(value));}
    public void setEnd(int value){    setNodeText(END_TAG_NAME,String.valueOf(value));}
    public void setError(String value){ setNodeText(ERROR_TAG_NAME,value);}

    public void setTotalA(float value){   setNodeText(TOTAL_A_TAG_NAME, String.valueOf(value));}
    public void setTotalC(float value){   setNodeText(TOTAL_C_TAG_NAME, String.valueOf(value));}
    public void setTotalG(float value){   setNodeText(TOTAL_G_TAG_NAME, String.valueOf(value));}
    public void setTotalT(float value){   setNodeText(TOTAL_T_TAG_NAME, String.valueOf(value));}
    public void setTotalOther(float value){ setNodeText(TOTAL_OTHER_TAG_NAME, String.valueOf(value));}

    public void setAIn1(float value){     setNodeText(A_IN_1_TAG_NAME,String.valueOf(value));}
    public void setCIn1(float value){     setNodeText(C_IN_1_TAG_NAME,String.valueOf(value));}
    public void setGIn1(float value){     setNodeText(G_IN_1_TAG_NAME,String.valueOf(value));}
    public void setTIn1(float value){     setNodeText(T_IN_1_TAG_NAME,String.valueOf(value));}
    public void setOtherIn1(float value){   setNodeText(OTHER_IN_1_TAG_NAME,String.valueOf(value));}

    public void setAIn2(float value){     setNodeText(A_IN_2_TAG_NAME,String.valueOf(value));}
    public void setCIn2(float value){     setNodeText(C_IN_2_TAG_NAME,String.valueOf(value));}
    public void setGIn2(float value){     setNodeText(G_IN_2_TAG_NAME,String.valueOf(value));}
    public void setTIn2(float value){     setNodeText(T_IN_2_TAG_NAME,String.valueOf(value));}
    public void setOtherIn2(float value){   setNodeText(OTHER_IN_2_TAG_NAME,String.valueOf(value));}

    public void setAIn3(float value){     setNodeText(A_IN_3_TAG_NAME,String.valueOf(value));}
    public void setCIn3(float value){     setNodeText(C_IN_3_TAG_NAME,String.valueOf(value));}
    public void setGIn3(float value){     setNodeText(G_IN_3_TAG_NAME,String.valueOf(value));}
    public void setTIn3(float value){     setNodeText(T_IN_3_TAG_NAME,String.valueOf(value));}
    public void setOtherIn3(float value){   setNodeText(OTHER_IN_3_TAG_NAME, String.valueOf(value));}

    public void setAInSilent3(float value){   setNodeText(A_IN_SILENT_3_TAG_NAME,String.valueOf(value));}
    public void setCInSilent3(float value){   setNodeText(C_IN_SILENT_3_TAG_NAME,String.valueOf(value));}
    public void setGInSilent3(float value){   setNodeText(G_IN_SILENT_3_TAG_NAME,String.valueOf(value));}
    public void setTInSilent3(float value){   setNodeText(T_IN_SILENT_3_TAG_NAME,String.valueOf(value));}
    public void setOtherInSilent3(float value){  setNodeText(OTHER_IN_SILENT_3_TAG_NAME, String.valueOf(value));}

    public void setOddAxisPalindromicityResult(PalindromicityResultXML result){
        root.removeChildren(ODD_AXIS_PALINDROMICITY_TAG_NAME);
        Element elem = new Element(ODD_AXIS_PALINDROMICITY_TAG_NAME);
        elem.addContent(result.getRoot());
        root.addContent(elem);
    }
    public void setEvenAxisPalindromicityResult(PalindromicityResultXML result){
        root.removeChildren(EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        Element elem = new Element(EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        elem.addContent(result.getRoot());
        root.addContent(elem);
    }
    public void setMaxWordLengthOddAxisPalindromicityResult(PalindromicityResultXML result){
        root.removeChildren(MAX_WORD_LENGTH_ODD_AXIS_PALINDROMICITY_TAG_NAME);
        Element elem = new Element(MAX_WORD_LENGTH_ODD_AXIS_PALINDROMICITY_TAG_NAME);
        elem.addContent(result.getRoot());
        root.addContent(elem);
    }
    public void setMaxWordLengthEvenAxisPalindromicityResult(PalindromicityResultXML result){
        root.removeChildren(MAX_WORD_LENGTH_EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        Element elem = new Element(MAX_WORD_LENGTH_EVEN_AXIS_PALINDROMICITY_TAG_NAME);
        elem.addContent(result.getRoot());
        root.addContent(elem);
    }


    @Override
    public int compareTo(Feature feature) {
        if(feature.getBegin() == this.getBegin()){
            return 0;
        }else if(this.getBegin() < feature.getBegin() ){
            return -1;
        }else{
            return 1;
        }
    }

    public void removeSequence(){
        root.removeChildren(SEQUENCE_TAG_NAME);
    }

    
    


}
