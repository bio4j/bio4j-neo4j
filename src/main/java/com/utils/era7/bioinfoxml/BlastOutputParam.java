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
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class BlastOutputParam extends XMLElement{

    public static final String TAG_NAME = "BlastOutput_param";

    public static final String PARAMETERS_TAG_NAME = "Parameters";

    public static final String MATRIX_TAG_NAME = "Parameters_matrix";
    public static final String EXPECT_TAG_NAME = "Parameters_expect";
    public static final String GAP_OPEN_TAG_NAME = "Parameters_gap-open";
    public static final String GAP_EXTEND_TAG_NAME = "Parameters_gap-extend";
    public static final String FILTER_TAG_NAME = "Parameters_filter";    

    public BlastOutputParam(){
        super(new Element(TAG_NAME));
    }
    public BlastOutputParam(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public BlastOutputParam(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setMatrix(String value){
        initParametersTag();
        setNodeText(MATRIX_TAG_NAME, value);
    }
    public void setExpect(String value){
        initParametersTag();
        setNodeText(EXPECT_TAG_NAME, value);
    }
    public void setGapOpen(String value){
        initParametersTag();
        setNodeText(GAP_OPEN_TAG_NAME, value);
    }
    public void setGapExtend(String value){
        initParametersTag();
        setNodeText(GAP_EXTEND_TAG_NAME, value);
    }
    public void setFilter(String value){
        initParametersTag();
        setNodeText(FILTER_TAG_NAME, value);
    }


    //----------------GETTERS---------------------
    public String getMatrix(){       return getNodeText(MATRIX_TAG_NAME);  }
    public String getExpect(){  return getNodeText(EXPECT_TAG_NAME);  }
    public String getGapOpen(){  return getNodeText(GAP_OPEN_TAG_NAME);  }
    public String getGapExtend(){  return getNodeText(GAP_EXTEND_TAG_NAME);  }
    public String getFilter(){  return getNodeText(FILTER_TAG_NAME);  }


    public BlastOutputParam getBlastOutputParam() throws XMLElementException{
        Element elem = root.getChild(BlastOutputParam.TAG_NAME);
        if(elem == null){
            return null;
        }else{
            return new BlastOutputParam(elem);
        }
    }

    private void initParametersTag(){
        Element paramsTagName = root.getChild(PARAMETERS_TAG_NAME);
        if(paramsTagName == null){
            paramsTagName = new Element(PARAMETERS_TAG_NAME);
        }
    }

}
