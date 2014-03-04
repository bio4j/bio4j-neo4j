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
package com.era7.bioinfoxml.gb;

import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;
import org.jdom2.Element;

public class GenBankXML extends XMLElement{

public static final String TAG_NAME = "gen_bank_xml";

public static final String  LOCUS_NAME_TAG_NAME = "locus_name";
public static final String  SEQUENCE_LENGTH_TAG_NAME = "sequence_length";
public static final String  MOLECULE_TYPE_TAG_NAME = "molecule_type";
public static final String  GEN_BANK_DIVISION_TAG_NAME = "gen_bank_division";
public static final String  STRANDED_TYPE_TAG_NAME = "stranded_type";
public static final String  DNA_TYPE_TAG_NAME = "dna_type";
public static final String  MODIFICATION_DATE_TAG_NAME = "modification_date";
public static final String  DEFINITION_TAG_NAME = "definition";
public static final String  LINEAR_TAG_NAME = "linear";
public static final String  ACCESSION_TAG_NAME = "accession";
public static final String  VERSION_TAG_NAME = "version";
public static final String  KEYWORDS_TAG_NAME = "keywords";
public static final String  SOURCE_TAG_NAME = "source";
public static final String  ORGANISM_TAG_NAME = "organism";
public static final String  COMMENT_TAG_NAME = "comment";
public static final String  ORGANISM_COMPLETE_TAXONOMY_LINEAGE_TAG_NAME = "organism_complete_taxonomy_lineage";

public GenBankXML(){
super(new Element(TAG_NAME));

}
public GenBankXML(Element elem) throws XMLElementException{
super(elem);
if(!elem.getName().equals(TAG_NAME)){
throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
}
}
public GenBankXML(String value) throws Exception{
super(value);
if(!root.getName().equals(TAG_NAME)){
throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
}
}

//----------------GETTERS-------------------

public boolean getLinear(){	return Boolean.parseBoolean(getNodeText(LINEAR_TAG_NAME));}
public String getKeywords(){	return getNodeText(KEYWORDS_TAG_NAME);}
public String getDefinition(){	return getNodeText(DEFINITION_TAG_NAME);}
public String getOrganismCompleteTaxonomyLineage(){	return getNodeText(ORGANISM_COMPLETE_TAXONOMY_LINEAGE_TAG_NAME);}
public int getSequenceLength(){	return Integer.parseInt(getNodeText(SEQUENCE_LENGTH_TAG_NAME));}
public String getLocusName(){	return getNodeText(LOCUS_NAME_TAG_NAME);}
public String getMoleculeType(){	return getNodeText(MOLECULE_TYPE_TAG_NAME);}
public String getOrganism(){	return getNodeText(ORGANISM_TAG_NAME);}
public String getVersion(){	return getNodeText(VERSION_TAG_NAME);}
public String getSource(){	return getNodeText(SOURCE_TAG_NAME);}
public String getDnaType(){	return getNodeText(DNA_TYPE_TAG_NAME);}
public String getAccession(){	return getNodeText(ACCESSION_TAG_NAME);}
public String getGenBankDivision(){	return getNodeText(GEN_BANK_DIVISION_TAG_NAME);}
public String getComment(){	return getNodeText(COMMENT_TAG_NAME);}
public String getModificationDate(){	return getNodeText(MODIFICATION_DATE_TAG_NAME);}
public String getStrandedType(){	return getNodeText(STRANDED_TYPE_TAG_NAME);}

//----------------SETTERS-------------------

public void  setLinear(boolean value){	 setNodeText(LINEAR_TAG_NAME, String.valueOf(value));}
public void  setKeywords(String value){	 setNodeText(KEYWORDS_TAG_NAME, value);}
public void  setDefinition(String value){	 setNodeText(DEFINITION_TAG_NAME, value);}
public void  setOrganismCompleteTaxonomyLineage(String value){	 setNodeText(ORGANISM_COMPLETE_TAXONOMY_LINEAGE_TAG_NAME, value);}
public void  setSequenceLength(int value){	 setNodeText(SEQUENCE_LENGTH_TAG_NAME, String.valueOf(value));}
public void  setLocusName(String value){	 setNodeText(LOCUS_NAME_TAG_NAME, value);}
public void  setMoleculeType(String value){	 setNodeText(MOLECULE_TYPE_TAG_NAME, value);}
public void  setOrganism(String value){	 setNodeText(ORGANISM_TAG_NAME, value);}
public void  setVersion(String value){	 setNodeText(VERSION_TAG_NAME, value);}
public void  setSource(String value){	 setNodeText(SOURCE_TAG_NAME, value);}
public void  setDnaType(String value){	 setNodeText(DNA_TYPE_TAG_NAME, value);}
public void  setAccession(String value){	 setNodeText(ACCESSION_TAG_NAME, value);}
public void  setGenBankDivision(String value){	 setNodeText(GEN_BANK_DIVISION_TAG_NAME, value);}
public void  setComment(String value){	 setNodeText(COMMENT_TAG_NAME, value);}
public void  setModificationDate(String value){	 setNodeText(MODIFICATION_DATE_TAG_NAME, value);}
public void  setStrandedType(String value){	 setNodeText(STRANDED_TYPE_TAG_NAME, value);}
}
