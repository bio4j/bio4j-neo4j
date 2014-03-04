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
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class HspSet{

    public static final String TAG_NAME = "hsp_set";

    public static final String HSPS_TAG_NAME = "hsps";

    private ArrayList<Hsp> array = null;
    private Hit hit = null;
    private String organism = "";

    public HspSet(){
        array = new ArrayList<Hsp>();
        hit = new Hit();
    }

    public HspSet(Element elem) throws XMLElementException{
        array = new ArrayList<Hsp>();
        hit = new Hit(elem.getChild(Hit.TAG_NAME));
        List<Element> list = elem.getChild(HSPS_TAG_NAME).getChildren(Hsp.TAG_NAME);
        for(Element temp : list){
            array.add(new Hsp(temp));
        }
    }


    public String getOrganism(){    return organism;}
    public void setOrganism(String value){  this.organism = value;}

    public XMLElement toXML(){
        XMLElement temp = new XMLElement(new Element(TAG_NAME));
        Hit tempHit = new Hit();
        tempHit.setHitDef(hit.getHitDef());
        tempHit.setUniprotID(hit.getUniprotID());
        temp.addChild(tempHit);
        temp.addChild(new XMLElement(new Element(HSPS_TAG_NAME)));
        Element hsps = temp.getRoot().getChild(HSPS_TAG_NAME);
        for(Hsp hsp : array){
            hsps.addContent(hsp.getRoot());
        }       

        return temp;
    }

    public void addHsp(Hsp hsp){
        array.add(hsp);
    }

    public boolean hspsAreFromTheSameReadingFrame(){
        Hsp hsp = array.get(0);
        int readingFrame = hsp.getHitFrame();

        for(int i=1; i< array.size(); i++){
            if(array.get(i).getHitFrame() != readingFrame){
                return false;
            }
        }

        return true;
    }

    public Hit getHit(){    return hit;}
    public void setHit(Hit hitValue){ hit = hitValue;}

    public int getHspQueryFrom(){
        return array.get(0).getQueryFrom();
    }
    public int getHspQueryTo(){     return array.get(array.size()-1).getQueryTo();}
    public boolean getOrientation(){    
        return (array.get(0).getHitFrame() > 0);
    }

    //IMPORTANTE CONFIRMAR QUE ESTOS DOS METODOS SON ASI!!!! --> en principio si
    public int getHspHitFrom(){
        Hsp primero = array.get(0);
        Hsp ultimo = null;

        if(array.size() == 1){
            ultimo = primero;
        }else{
            ultimo = array.get(array.size()-1);
        }
        
        boolean orientacionNegativa = primero.getHitFrame() < 0;
        if(orientacionNegativa){
            return ultimo.getHitFrom();
        }else{
            return primero.getHitFrom();
        }
        
    }

    public double getEvalue(){
        double evalue = array.get(0).getEvalueDoubleFormat();
        for(int i=0;i<array.size();i++){
            double tempEvalue = array.get(i).getEvalueDoubleFormat();
            if(tempEvalue < evalue){
                evalue = tempEvalue;
            }
        }

        return evalue;
    }

    public int getHspHitTo(){
        Hsp primero = array.get(0);
        Hsp ultimo = null;
        
        if(array.size() == 1){
            ultimo = primero;
        }else{
            ultimo = array.get(array.size()-1);
        }

        boolean orientacionNegativa = primero.getHitFrame() < 0;
        if(orientacionNegativa){
            return primero.getHitTo();
        }else{
            return ultimo.getHitTo();
        }
    }

    public Hsp get(int i){
        return array.get(i);
    }
    public ArrayList<Hsp> getHsps(){
        return array;
    }

    public int size(){  return array.size();  }

}
