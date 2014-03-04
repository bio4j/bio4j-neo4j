/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era7.bioinfo.bioinfoutil.model;

/**
 *
 * @author ppareja
 */
public class Intergenic extends Feature{

    protected char orfUpstreamStrand;

    public Intergenic(int begin, int end){
        super(begin,end);
    }

    public char getOrfUpstreamStrand() {
        return orfUpstreamStrand;
    }

    public void setOrfUpstreamStrand(char orfUpstreamStrand) {
        this.orfUpstreamStrand = orfUpstreamStrand;
    }

}
