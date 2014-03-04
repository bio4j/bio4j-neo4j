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

package com.ohnosequences.xml.model;

import com.ohnosequences.xml.api.model.XMLElement;
import com.ohnosequences.xml.api.model.XMLElementException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author ppareja
 */
public class PredictedGene extends XMLElement implements Comparable<PredictedGene>{

    public static final String TAG_NAME = "predicted_gene";

    public static final String ID_TAG_NAME = "id";
    public static final String SEQUENCE_TAG_NAME = "sequence";
    public static final String EVALUE_TAG_NAME = "evalue";
    public static final String CONTIG_ID_TAG_NAME = "contig_id";
    public static final String ANNOTATION_UNIPROT_ID_TAG_NAME = "annotation_uniprot_id";
    public static final String ANNOTATION_SCORE_EVALUE_TAG_NAME = "annotation_score_evalue";
    public static final String BLAST_HIT_START_TAG_NAME = "blast_hit_start";
    public static final String BLAST_HIT_END_TAG_NAME = "blast_hit_end";
    public static final String PROTEIN_SEQUENCE_TAG_NAME = "protein_sequence";
    public static final String STRAND_TAG_NAME = "strand";
    public static final String HIT_DEF_TAG_NAME = "hit_def";

    //---COSAS UNIPROT-----
    public static final String ACCESSION_TAG_NAME = "accession";
    public static final String PROTEIN_NAMES_TAG_NAME = "protein_names";
    public static final String ORGANISM_TAG_NAME = "organism";
    public static final String COMMENT_FUNCTION_TAG_NAME = "comment_function";
    public static final String EC_NUMBERS_TAG_NAME = "ec_numbers";
    public static final String INTERPRO_TAG_NAME = "interpro";
    public static final String GENE_ONTOLOGY_TAG_NAME = "gene_ontology";
    public static final String GENE_ONTOLOGY_ID_TAG_NAME = "gene_ontology_id";
    public static final String PATHWAY_TAG_NAME = "pathway";
    public static final String PROTEIN_FAMILY_TAG_NAME = "protein_family";
    public static final String KEYWORDS_TAG_NAME = "keywords";
    public static final String LENGTH_TAG_NAME = "length";
    public static final String SUBCELLULAR_LOCATIONS_TAG_NAME = "subcellular_locations";
    public static final String PUBMED_ID_TAG_NAME = "pubmed_id";
    public static final String GENE_NAMES_TAG_NAME = "gene_names";
    public static final String DOMAINS_TAG_NAME = "domains";

    public static final String POSITIVE_STRAND = "+";
    public static final String NEGATIVE_STRAND = "-";

    public static final String START_POSITION_TAG_NAME = "start_position";
    public static final String START_IS_CANONICAL_TAG_NAME = "start_is_canonical";
    public static final String END_POSITION_TAG_NAME = "end_position";
    public static final String END_IS_CANONICAL_TAG_NAME = "end_is_canonical";

    public static final String START_CODON_TAG_NAME = "start_codon";
    public static final String STOP_CODON_TAG_NAME = "stop_codon";

    public static final String EXTRA_STOP_CODONS_TAG_NAME = "extra_stop_codons";
    public static final String FRAME_SHIFTS_TAG_NAME = "frameshifts";

    public static final String STATUS_TAG_NAME = "status";
    public static final String GENE_DISMISSED_BY_TAG_NAME = "gene_dismissed_by";


    //--------------POSSIBLE STATUS------------
    public static final String STATUS_DISMISSED = "dismissed";
    public static final String STATUS_SELECTED = "selected";
    public static final String STATUS_SELECTED_MINOR_THRESHOLD = "selected_minor_threshold";
    //---------------------------------------
    

    public PredictedGene(){
        super(new Element(TAG_NAME));
    }
    public PredictedGene(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public PredictedGene(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    public void addExtraStopCodon(Codon codon){
        initExtraStopCodonsTag();
        root.getChild(EXTRA_STOP_CODONS_TAG_NAME).addContent(codon.getRoot());
    }
    public void addFrameShift(Frameshift frameshift){
        initFrameshiftsTag();
        root.getChild(FRAME_SHIFTS_TAG_NAME).addContent(frameshift.getRoot());
    }

    //----------------SETTERS-------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setAccession(String value){ setNodeText(ACCESSION_TAG_NAME, value);}
    public void setGeneNames(String value){ setNodeText(GENE_NAMES_TAG_NAME, value);}
    public void setDomains(String value){ setNodeText(DOMAINS_TAG_NAME, value);}
    public void setHitDef(String value){ setNodeText(HIT_DEF_TAG_NAME, value);}
    public void setProteinNames(String value){  setNodeText(PROTEIN_NAMES_TAG_NAME, value);};
    public void setOrganism(String value){  setNodeText(ORGANISM_TAG_NAME, value);}
    public void setCommentFunction(String value){   setNodeText(COMMENT_FUNCTION_TAG_NAME, value);}
    public void setEcNumbers(String value){ setNodeText(EC_NUMBERS_TAG_NAME, value);}
    public void setInterpro(String value){  setNodeText(INTERPRO_TAG_NAME, value);}
    public void setGeneOntology(String value){  setNodeText(GENE_ONTOLOGY_TAG_NAME, value);}
    public void setGeneOntologyId(String value){  setNodeText(GENE_ONTOLOGY_ID_TAG_NAME, value);}
    public void setPathway(String value){   setNodeText(PATHWAY_TAG_NAME, value);}
    public void setProteinFamily(String value){ setNodeText(PROTEIN_FAMILY_TAG_NAME, value);}
    public void setKeywords(String value){  setNodeText(KEYWORDS_TAG_NAME, value);}
    public void setLength(int value){    setNodeText(LENGTH_TAG_NAME, String.valueOf(value));}
    public void setSubcellularLocations(String value){  setNodeText(SUBCELLULAR_LOCATIONS_TAG_NAME, value);}
    public void setPubmedId(String value){  setNodeText(PUBMED_ID_TAG_NAME, value);}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME,value);}
    public void setEvalue(double value){    setNodeText(EVALUE_TAG_NAME, String.valueOf(value));}
    public void setStatus(String value){    setNodeText(STATUS_TAG_NAME, value);}
    public void setGeneDismissedBy(String value){   setNodeText(GENE_DISMISSED_BY_TAG_NAME, value);}
    public void setContigId(String value){  setNodeText(CONTIG_ID_TAG_NAME, value);}
    public void setStartPosition(int value){  setNodeText(START_POSITION_TAG_NAME, String.valueOf(value));}
    public void setStartIsCanonical(boolean value){ setNodeText(START_IS_CANONICAL_TAG_NAME,String.valueOf(value));}
    public void setEndPosition(int value){  setNodeText(END_POSITION_TAG_NAME, String.valueOf(value));}
    public void setEndIsCanonical(boolean value){   setNodeText(END_IS_CANONICAL_TAG_NAME, String.valueOf(value));}
    public void setStrand(String value){  setNodeText(STRAND_TAG_NAME, value);}
    public void setStrand(boolean value){
        if(value){
            setNodeText(STRAND_TAG_NAME, POSITIVE_STRAND);
        }else{
            setNodeText(STRAND_TAG_NAME, NEGATIVE_STRAND);
        }
    }
    public void setAnnotationUniprotId(String value){  setNodeText(ANNOTATION_UNIPROT_ID_TAG_NAME, value);}
    public void setAnnotationScoreEvalue(String value){  setNodeText(ANNOTATION_SCORE_EVALUE_TAG_NAME, value);}
    public void setBlastHitStart(int value){  setNodeText(BLAST_HIT_START_TAG_NAME, String.valueOf(value));}
    public void setBlastHitEnd(int value){  setNodeText(BLAST_HIT_END_TAG_NAME, String.valueOf(value));}
    public void setProteinSequence(String value){   setNodeText(PROTEIN_SEQUENCE_TAG_NAME, value);}
    public void setStartCodon(Codon codon){
        initStartCodonTag();
        Element temp = root.getChild(START_CODON_TAG_NAME);
        temp.removeChildren(Codon.TAG_NAME);
        temp.addContent(codon.getRoot());
    }
    public void setStopCodon(Codon codon){
        initStopCodonTag();
        Element temp = root.getChild(STOP_CODON_TAG_NAME);
        temp.removeChildren(Codon.TAG_NAME);
        temp.addContent(codon.getRoot());
    }
    public void setHspSet(HspSet value){
        root.removeChildren(HspSet.TAG_NAME);
        root.addContent(value.toXML().getRoot());
    }

    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public String getHitDef(){   return getNodeText(HIT_DEF_TAG_NAME);}
    public String getGeneNames(){   return getNodeText(GENE_NAMES_TAG_NAME);}
    public String getDomains(){   return getNodeText(DOMAINS_TAG_NAME);}
    public String getAccession(){   return getNodeText(ACCESSION_TAG_NAME);}
    public String getProteinNames(){    return getNodeText(PROTEIN_NAMES_TAG_NAME);}
    public String getOrganism(){    return getNodeText(ORGANISM_TAG_NAME);}
    public String getCommentFunction(){ return getNodeText(COMMENT_FUNCTION_TAG_NAME);}
    public String getEcNumbers(){   return getNodeText(EC_NUMBERS_TAG_NAME);}
    public String getInterpro(){    return getNodeText(INTERPRO_TAG_NAME);}
    public String getGeneOntology(){    return getNodeText(GENE_ONTOLOGY_TAG_NAME);}
    public String getGeneOntologyId(){    return getNodeText(GENE_ONTOLOGY_ID_TAG_NAME);}
    public String getPathway(){ return getNodeText(PATHWAY_TAG_NAME);}
    public String getProteinFamily(){   return getNodeText(PROTEIN_FAMILY_TAG_NAME);}
    public String getKeywords(){    return getNodeText(KEYWORDS_TAG_NAME);}
    public int getLength(){ return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));}
    public String getSubcellularLocations(){    return getNodeText(SUBCELLULAR_LOCATIONS_TAG_NAME);}
    public String getPubmedId(){    return getNodeText(PUBMED_ID_TAG_NAME);}
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}
    public double getEvalue(){  return Double.parseDouble(getNodeText(EVALUE_TAG_NAME));}
    public String getGeneDismissedBy(){ return getNodeText(GENE_DISMISSED_BY_TAG_NAME);}
    public String getStatus(){  return getNodeText(STATUS_TAG_NAME);}
    public String getContigId( ){  return getNodeText(CONTIG_ID_TAG_NAME);}
    public int getStartPosition( ){  return Integer.parseInt(getNodeText(START_POSITION_TAG_NAME));}
    public boolean getStartIsCanonical(){   return Boolean.parseBoolean(getNodeText(START_IS_CANONICAL_TAG_NAME));}
    public int getEndPosition( ){  return Integer.parseInt(getNodeText(END_POSITION_TAG_NAME));}
    public boolean getEndIsCanonical(){ return Boolean.parseBoolean(getNodeText(END_IS_CANONICAL_TAG_NAME));}
    public String getStrand( ){  return getNodeText(STRAND_TAG_NAME);}
    public String getAnnotationUniprotId( ){  return getNodeText(ANNOTATION_UNIPROT_ID_TAG_NAME);}
    public String getAnnotationScoreEvalue( ){  return getNodeText(ANNOTATION_SCORE_EVALUE_TAG_NAME);}
    public String getBlastHitStart( ){  return getNodeText(BLAST_HIT_START_TAG_NAME);}
    public String getBlastHitEnd( ){  return getNodeText(BLAST_HIT_END_TAG_NAME);}
    public String getProteinSequence(){ return getNodeText(PROTEIN_SEQUENCE_TAG_NAME);}
    public Element getExtraStopCodons(){ return root.getChild(EXTRA_STOP_CODONS_TAG_NAME);}
    public HspSet getHspSet() throws XMLElementException{
        HspSet set = null;
        Element temp = root.getChild(HspSet.TAG_NAME);
        if(temp != null){
            set = new HspSet(temp);
        }
        return set;
    }
    public int getHspSetHitFrom() throws XMLElementException{   return getHspSet().getHspHitFrom();  }
    public int getHspSetHitTo() throws XMLElementException{    return getHspSet().getHspHitTo();}
    public Codon getInitCodon() throws XMLElementException{
        Codon temp = null;
        Element elem = root.getChild(START_CODON_TAG_NAME);
        if(elem != null){
            Element elem2 = elem.getChild(Codon.TAG_NAME);
            if(elem2 != null){
                temp = new Codon(elem2);
            }
        }
        return temp;
    }
    public Codon getStopCodon() throws XMLElementException{
        Codon temp = null;
        Element elem = root.getChild(STOP_CODON_TAG_NAME);
        if(elem != null){
            Element elem2 = elem.getChild(Codon.TAG_NAME);
            if(elem2 != null){
                temp = new Codon(elem2);
            }
        }
        return temp;
    }

    public ArrayList<Frameshift> getFrameshifts() throws XMLElementException{
        ArrayList<Frameshift> array = null;
        Element temp = root.getChild(FRAME_SHIFTS_TAG_NAME);
        if(temp != null){
            array = new ArrayList<Frameshift>();
            List<Element> list = temp.getChildren(Frameshift.TAG_NAME);
            for(Element elem : list){
                Frameshift frameshift = new Frameshift(elem);
                array.add(frameshift);
            }
        }
        return array;
    }


    private void initStartCodonTag(){
        Element temp = root.getChild(START_CODON_TAG_NAME);
        if(temp == null){
            root.addContent(new Element(START_CODON_TAG_NAME));
        }
    }
    private void initStopCodonTag(){
        Element temp = root.getChild(STOP_CODON_TAG_NAME);
        if(temp == null){
            root.addContent(new Element(STOP_CODON_TAG_NAME));
        }
    }
    private void initExtraStopCodonsTag(){
        Element temp = root.getChild(EXTRA_STOP_CODONS_TAG_NAME);
        if(temp == null){
            root.addContent(new Element(EXTRA_STOP_CODONS_TAG_NAME));
        }
    }
    private void initFrameshiftsTag(){
        Element temp = root.getChild(FRAME_SHIFTS_TAG_NAME);
        if(temp == null){
            root.addContent(new Element(FRAME_SHIFTS_TAG_NAME));
        }
    }

    @Override
    public int compareTo(PredictedGene o) {
        if(this.getStartPosition() < o.getStartPosition()){
            return -1;
        }else if(this.getStartPosition() > o.getStartPosition()){
            return 1;
        }else{
            if(this.getEndPosition() < o.getEndPosition()){
                return -1;
            }else if(this.getEndPosition() > o.getEndPosition()){
                return 1;
            }else{
                if(this.getEvalue() < o.getEvalue()){
                    return -1;
                }else if(this.getEvalue() > o.getEvalue()){
                    return 1;
                }else{
                    return 0;
                }
            }
        }
    }
}
