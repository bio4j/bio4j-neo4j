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

package com.ohnosequences.xml.model.genome;

import com.ohnosequences.xml.model.genome.feature.Feature;
import com.ohnosequences.xml.model.pal.PalindromicityResultXML;
import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import com.ohnosequences.xml.model.util.Error;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class GenomeElement extends XMLElement{

    public static final String TAG_NAME = "genome_element";

    public static final String ID_TAG_NAME = "id";
    public static final String FEATURES_TAG_NAME = "features";
    public static final String LENGTH_TAG_NAME = "length";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String ERRORS_TAG_NAME = "errors";

    //----------PALINDROMICITY-----------
    public static final String ODD_AXIS_PALINDROMICITY_TAG_NAME = "odd_axis_pal";
    public static final String EVEN_AXIS_PALINDROMICITY_TAG_NAME = "even_axis_pal";
    public static final String MAX_WORD_LENGTH_EVEN_AXIS_PALINDROMICITY_TAG_NAME = "mwl_even_axis_pal";
    public static final String MAX_WORD_LENGTH_ODD_AXIS_PALINDROMICITY_TAG_NAME = "mwl_odd_axis_pal";


    public GenomeElement(){
        super(new Element(TAG_NAME));
    }
    public GenomeElement(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public GenomeElement(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }


    public void addFeature(Feature feature){
        initFeaturesTag();
        root.getChild(FEATURES_TAG_NAME).addContent(feature.getRoot());
    }

    //----------------GETTERS---------------------
    public Element getFeatures(){   return root.getChild(FEATURES_TAG_NAME);}
    public String getId(){  return getNodeText(ID_TAG_NAME); }
    public int getLength() {    return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));}
    public Element getErrors(){   return root.getChild(ERRORS_TAG_NAME);}
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}

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
    public void setLength(int value){   setNodeText(LENGTH_TAG_NAME,String.valueOf(value));}
    public void setSequence(String value){ setNodeText(SEQUENCE_TAG_NAME, value);}
    public void addError(Error value){
        initErrorsTag();
        root.getChild(ERRORS_TAG_NAME).addContent(value.getRoot());
    }
    public void setFeatures(Element features){
        root.removeChildren(FEATURES_TAG_NAME);
        root.addContent(features);
    }
    public void setErrors(Element errors){
        root.removeChildren(ERRORS_TAG_NAME);
        root.addContent(errors);
    }
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


    private void initFeaturesTag(){
        if(root.getChild(FEATURES_TAG_NAME) == null){
            root.addContent(new Element(FEATURES_TAG_NAME));
        }
    }
    private void initErrorsTag(){
        if(root.getChild(ERRORS_TAG_NAME) == null){
            root.addContent(new Element(ERRORS_TAG_NAME));
        }
    }

}
