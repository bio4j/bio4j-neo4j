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
package com.era7.bioinfoxml.bio4j;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

/**
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class UniprotDataXML extends XMLElement{
    
    public static final String TAG_NAME = "uniprot_data";
    
    public static final String KEYWORDS_TAG_NAME = "keywords";
    public static final String INTERPRO_TAG_NAME = "interpro";
    public static final String PFAM_TAG_NAME = "pfam";
    public static final String CITATIONS_TAG_NAME = "citations";
    public static final String ARTICLES_TAG_NAME = "articles";
    public static final String PATENT_TAG_NAME = "patent";
    public static final String ONLINE_ARTICLES_TAG_NAME = "online_articles";
    public static final String THESIS_TAG_NAME = "thesis";
    public static final String BOOKS_TAG_NAME = "books";
    public static final String SUBMISSIONS_TAG_NAME = "submissions";
    public static final String UNPUBLISHED_OBSERVATIONS_TAG_NAME = "unpublished_observations";
    public static final String COMMENTS_TAG_NAME = "comments";
    public static final String FEATURES_TAG_NAME = "features";
    public static final String REACTOME_TAG_NAME = "reactome";
    public static final String ISOFORMS_TAG_NAME = "isoforms";
    public static final String SUBCELLULAR_LOCATIONS_TAG_NAME = "subcellular_locations";
    public static final String ENZYME_DB_TAG_NAME = "enzyme_db";
    public static final String GENE_ONTOLOGY_TAG_NAME = "gene_ontology";
    public static final String REFSEQ_TAG_NAME = "refseq";
    
    public UniprotDataXML(){
        super(new Element(TAG_NAME));
    }
    public UniprotDataXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public UniprotDataXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }
        
    //----------------GETTERS---------------------
    public boolean getKeywords( ){  return Boolean.parseBoolean(getNodeText(KEYWORDS_TAG_NAME));}
    public boolean getInterpro( ){  return Boolean.parseBoolean(getNodeText(INTERPRO_TAG_NAME));}
    public boolean getPfam( ){  return Boolean.parseBoolean(getNodeText(PFAM_TAG_NAME));}
    public boolean getCitations( ){  return Boolean.parseBoolean(getNodeText(CITATIONS_TAG_NAME));}
    public boolean getArticles( ){  return Boolean.parseBoolean(getNodeText(ARTICLES_TAG_NAME));}
    public boolean getOnlineArticles( ){  return Boolean.parseBoolean(getNodeText(ONLINE_ARTICLES_TAG_NAME));}
    public boolean getPatents(){    return Boolean.parseBoolean(getNodeText(PATENT_TAG_NAME));}
    public boolean getThesis( ){  return Boolean.parseBoolean(getNodeText(THESIS_TAG_NAME));}
    public boolean getBooks( ){  return Boolean.parseBoolean(getNodeText(BOOKS_TAG_NAME));}
    public boolean getUnpublishedObservations( ){  return Boolean.parseBoolean(getNodeText(UNPUBLISHED_OBSERVATIONS_TAG_NAME));}
    public boolean getSubmissions( ){  return Boolean.parseBoolean(getNodeText(SUBMISSIONS_TAG_NAME));}
    public boolean getComments( ){  return Boolean.parseBoolean(getNodeText(COMMENTS_TAG_NAME));}
    public boolean getFeatures( ){  return Boolean.parseBoolean(getNodeText(FEATURES_TAG_NAME));}
    public boolean getReactome( ){  return Boolean.parseBoolean(getNodeText(REACTOME_TAG_NAME));}
    public boolean getIsoforms( ){  return Boolean.parseBoolean(getNodeText(ISOFORMS_TAG_NAME));}
    public boolean getSubcellularLocations( ){  return Boolean.parseBoolean(getNodeText(SUBCELLULAR_LOCATIONS_TAG_NAME));}
    public boolean getEnzymeDb( ){  return Boolean.parseBoolean(getNodeText(ENZYME_DB_TAG_NAME));}
    public boolean getGeneOntology( ){  return Boolean.parseBoolean(getNodeText(GENE_ONTOLOGY_TAG_NAME));}
    public boolean getRefseq( ){  return Boolean.parseBoolean(getNodeText(REFSEQ_TAG_NAME));}
    
    
    //----------------SETTERS-------------------
    public void setKeywords(boolean value){  setNodeText(KEYWORDS_TAG_NAME, String.valueOf(value));}
    public void setInterpro(boolean value){  setNodeText(INTERPRO_TAG_NAME, String.valueOf(value));}
    public void setPfam(boolean value){  setNodeText(PFAM_TAG_NAME, String.valueOf(value));}
    public void setCitations(boolean value){  setNodeText(CITATIONS_TAG_NAME, String.valueOf(value));}
    public void setArticless(boolean value){  setNodeText(ARTICLES_TAG_NAME, String.valueOf(value));}
    public void setOnlineArticles(boolean value){  setNodeText(ONLINE_ARTICLES_TAG_NAME, String.valueOf(value));}
    public void setPatents(boolean value){  setNodeText(PATENT_TAG_NAME, String.valueOf(value));}
    public void setThesis(boolean value){  setNodeText(THESIS_TAG_NAME, String.valueOf(value));}
    public void setBooks(boolean value){  setNodeText(BOOKS_TAG_NAME, String.valueOf(value));}
    public void setUnpublishedObservations(boolean value){  setNodeText(UNPUBLISHED_OBSERVATIONS_TAG_NAME, String.valueOf(value));}
    public void setSubmissions(boolean value){  setNodeText(SUBMISSIONS_TAG_NAME, String.valueOf(value));}
    public void setComments(boolean value){  setNodeText(COMMENTS_TAG_NAME, String.valueOf(value));}
    public void setFeatures(boolean value){  setNodeText(FEATURES_TAG_NAME, String.valueOf(value));}
    public void setReactome(boolean value){  setNodeText(REACTOME_TAG_NAME, String.valueOf(value));}
    public void setIsoforms(boolean value){  setNodeText(ISOFORMS_TAG_NAME, String.valueOf(value));}
    public void setSubcellularLocations(boolean value){  setNodeText(SUBCELLULAR_LOCATIONS_TAG_NAME, String.valueOf(value));}
    public void setEnzymeDb(boolean value){  setNodeText(ENZYME_DB_TAG_NAME, String.valueOf(value));}
    public void setGeneOntology(boolean value){  setNodeText(GENE_ONTOLOGY_TAG_NAME, String.valueOf(value));}
    public void setRefseq(boolean value){  setNodeText(REFSEQ_TAG_NAME, String.valueOf(value));}
    
}
