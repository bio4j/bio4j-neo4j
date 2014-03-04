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
package com.era7.bioinfoxml.pal;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class PalindromicityResultXML extends XMLElement {

    public static final String TAG_NAME = "pal_result";
    
    public static final String EVEN_AXIS = "even";
    public static final String ODD_AXIS = "odd";

    public static final String IS_MAX_WORD_LENGTH_TAG_NAME = "is_max_word_length";
    public static final String AXIS_TAG_NAME = "axis";
    public static final String RESULT_TAG_NAME = "result";
    public static final String RESULT_MAX_WORD_LENGTH_TAG_NAME = "result_max_word_length";
    public static final String PATTERNS_TAG_NAME = "patterns";
    public static final String SEPARATOR_TAG_NAME = "separator";

    public static final String MEAN_TAG_NAME = "mean";
    public static final String MEDIAN_TAG_NAME = "median";
    public static final String MODE_TAG_NAME = "mode";
    public static final String STANDARD_DEVIATION_TAG_NAME = "standard_deviation";


    public PalindromicityResultXML() {
        super(new Element(TAG_NAME));
    }

    public PalindromicityResultXML(Element elem) throws XMLElementException {
        super(elem);
        if (!elem.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(elem));
        }
    }

    public PalindromicityResultXML(String value) throws Exception {
        super(value);
        if (!root.getName().equals(TAG_NAME)) {
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME, new XMLElement(value));
        }
    }

//    public void calculatePalXValues(int offset,
//            boolean normalize) {
//        String res = getResult();
//        float[] palValues = new float[11];
//        for (int c = 0; c < 11; c++) {
//            palValues[c] = 0;
//        }
//
//        char separator = getSeparator().charAt(0);
//        String currentValue = null;
//
//        if (offset == 0) {
//            for (int i = 0; i < res.length(); i++) {
//                char value = res.charAt(i);
//                if (value == separator || i == (res.length()-1)) {
//                    currentValue = "";
//                    if (currentValue.equals("0")) {
//                        palValues[0] += 1;
//                    } else if (currentValue.equals("1")) {
//                        palValues[1] += 1;
//                    } else if (currentValue.equals("2")) {
//                        palValues[2] += 1;
//                    } else if (currentValue.equals("3")) {
//                        palValues[3] += 1;
//                    } else if (currentValue.equals("4")) {
//                        palValues[4] += 1;
//                    } else if (currentValue.equals("5")) {
//                        palValues[5] += 1;
//                    } else if (currentValue.equals("6")) {
//                        palValues[6] += 1;
//                    } else if (currentValue.equals("7")) {
//                        palValues[7] += 1;
//                    } else if (currentValue.equals("8")) {
//                        palValues[8] += 1;
//                    } else if (currentValue.equals("9")) {
//                        palValues[9] += 1;
//                    } else if (currentValue.equals("10")) {
//                        palValues[10] += 1;
//                    }
//                } else {
//                    currentValue += value;
//                }
//
//            }
//
//            if (normalize) {
//                float length;
//                if (getAxis().equals(ODD_AXIS)) {
//                    length = res.length() + 2;
//                } else {
//                    length = res.length() + 1;
//                }
//
//                for (int j = 0; j < palValues.length; j++) {
//                    palValues[j] = palValues[j] / length;
//                }
//            }
//
//            setPal0(palValues[0]);
//            setPal1(palValues[1]);
//            setPal2(palValues[2]);
//            setPal3(palValues[3]);
//            setPal4(palValues[4]);
//            setPal5(palValues[5]);
//            setPal6(palValues[6]);
//            setPal7(palValues[7]);
//            setPal8(palValues[8]);
//            setPal9(palValues[9]);
//            setPal10(palValues[10]);
//
//        } else {
//        }
//    }

    public void removeResult() {
        root.removeChildren(RESULT_TAG_NAME);
    }

    //----------------GETTERS---------------------
    public String getAxis() {       return getNodeText(AXIS_TAG_NAME);    }
    public boolean getIsMaxWordLength() {       return Boolean.parseBoolean(getNodeText(IS_MAX_WORD_LENGTH_TAG_NAME));    }
    public String getResult() {        return getNodeText(RESULT_TAG_NAME);    }
    public String getPatterns(){        return getNodeText(PATTERNS_TAG_NAME);    }
    public String getSeparator() {        return getNodeText(SEPARATOR_TAG_NAME);    }
    public String getResultMaxWordLength() {        return getNodeText(RESULT_MAX_WORD_LENGTH_TAG_NAME);    }
    public double getMean(){    return Double.parseDouble(getNodeText(MEAN_TAG_NAME));}
    public double getMedian(){  return Double.parseDouble(getNodeText(MEDIAN_TAG_NAME));}
    public double getMode(){    return Double.parseDouble(getNodeText(MODE_TAG_NAME));}
    public double getStandardDeviation(){   return Double.parseDouble(getNodeText(STANDARD_DEVIATION_TAG_NAME));}   


    //----------------SETTERS---------------------
    public void setAxis(String axis) {       setNodeText(AXIS_TAG_NAME, axis);    }
    public void setIsMaxWordLength(boolean value){  setNodeText(IS_MAX_WORD_LENGTH_TAG_NAME, String.valueOf(value));}
    public void setResult(String value) {        setNodeText(RESULT_TAG_NAME, value);    }
    public void setSeparator(String value) {        setNodeText(SEPARATOR_TAG_NAME, value);    }
    public void setPatterns(String value){        setNodeText(PATTERNS_TAG_NAME, value);    }
    public void setResultMaxWordLength(String value) {        setNodeText(RESULT_MAX_WORD_LENGTH_TAG_NAME, value);    }
    public void setMean(double value){  setNodeText(MEAN_TAG_NAME, String.valueOf(value));  }
    public void setMedian(double value){    setNodeText(MEDIAN_TAG_NAME, String.valueOf(value));}
    public void setMode(double value){  setNodeText(MODE_TAG_NAME, String.valueOf(value));}
    public void setStandardDeviation(double value){ setNodeText(STANDARD_DEVIATION_TAG_NAME, String.valueOf(value));}


    
}
