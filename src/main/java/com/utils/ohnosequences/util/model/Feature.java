/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohnosequences.util.model;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class Feature implements Comparable<Feature> {

    public static final String INTERGENIC_FEATURE_TYPE = "int";
    public static final String RNA_FEATURE_TYPE = "rna";
    public static final String ORF_FEATURE_TYPE = "orf";
    public static final String ALL_FEATURE_TYPE = "all";
    public static final char POSITIVE_STRAND = '+';
    public static final char NEGATIVE_STRAND = '-';
    protected int begin;
    protected int end;
    protected int length;
    protected String id;
    protected double eValue;
    protected String organism;
    protected char strand;
    protected String name;
    protected String genomeElementId;
    protected String sequence;
    protected String type;

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    public double geteValue() {
        return eValue;
    }

    public void seteValue(double eValue) {
        this.eValue = eValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Feature() {
    }

    public Feature(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getGenomeElementId() {
        return genomeElementId;
    }

    public void setGenomeElementId(String genomeElementId) {
        this.genomeElementId = genomeElementId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public char getStrand() {
        return strand;
    }

    public void setStrand(char strand) {
        this.strand = strand;
    }

    @Override
    public int compareTo(Feature f) {
        if (getBegin() == f.getBegin()) {
            if (getEnd() == f.getEnd()) {
                if (geteValue() < f.geteValue()) {
                    return -1;
                } else if (geteValue() > f.geteValue()) {
                    return 1;
                } else {
                    if (f.getId().equals(getId())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            } else if (getEnd() < f.getEnd()) {
                return 1;
            } else {
                return -1;
            }
        } else if (getBegin() < f.getBegin()) {
            return -1;
        } else {
            return 1;
        }
    }
}
