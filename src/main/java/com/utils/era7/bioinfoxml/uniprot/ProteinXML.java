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

package com.era7.bioinfoxml.uniprot;

import com.era7.bioinfoxml.go.GoTermXML;
import com.era7.era7xmlapi.model.XMLElement;
import com.era7.era7xmlapi.model.XMLElementException;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 *
 * @author Pablo Pareja Tobes
 */
public class ProteinXML extends XMLElement{

    public static final String TAG_NAME = "protein";

    public static final String ID_TAG_NAME = "id";
    public static final String NAME_TAG_NAME = "name";
    public static final String FULL_NAME_TAG_NAME = "full_name";
    public static final String SHORT_NAME_TAG_NAME = "short_name";
    public static final String SEQUENCE_TAG_NAME = "sequence";    
    public static final String LENGTH_TAG_NAME = "length";
    
    public static final String ORGANISM_TAG_NAME = "organism";
    
    public static final String KEYWORDS_TAG_NAME = "keywords";
    public static final String INTERPROS_TAG_NAME = "interpros";
    public static final String COMMENTS_TAG_NAME = "comments";
    public static final String SUBCELLULAR_LOCATIONS_TAG_NAME = "subcellular_locations";
    public static final String ARTICLE_CITATIONS_TAG_NAME = "article_citations";
    
    public static final String SIGNAL_PEPTIDE_FEATURES = "signal_peptide_features";
    public static final String SPLICE_VARIANT_FEATURES = "splice_variant_features";
    public static final String TRANSMEMBRANE_REGION_FEATURES = "transmembrane_region_features";
    public static final String ACTIVE_SITE_FEATURES = "active_site_features";
    
    public static final String PROTEIN_PROTEIN_OUTGOING_INTERACTIONS_TAG_NAME = "protein_protein_outgoing_interactions";
    public static final String PROTEIN_PROTEIN_INCOMING_INTERACTIONS_TAG_NAME = "protein_protein_incoming_interactions";
    public static final String PROTEIN_ISOFORM_OUTGOING_INTERACTIONS_TAG_NAME = "protein_isoform_outgoing_interactions";
    public static final String PROTEIN_ISOFORM_INCOMING_INTERACTIONS_TAG_NAME = "protein_isoform_incoming_interactions";
    
    
    public static final String PROTEIN_COVERAGE_ABSOLUTE = "protein_coverage_absolute";
    public static final String PROTEIN_COVERAGE_PERCENTAGE = "protein_coverage_percentage";
    public static final String NUMBER_OF_ISOTIGS = "number_of_isotigs";

    public static final String GO_TERMS_TAG_NAME = "go_terms";
    public static final String PROCESS_GO_TERMS_TAG_NAME = "biological_process";
    public static final String FUNCTION_GO_TERMS_TAG_NAME = "molecular_function";
    public static final String COMPONENT_GO_TERMS_TAG_NAME = "cellular_component";



    public ProteinXML(){
        super(new Element(TAG_NAME));
    }
    public ProteinXML(Element elem) throws XMLElementException{
        super(elem);
        if(!elem.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(elem));
        }
    }
    public ProteinXML(String value) throws Exception{
        super(value);
        if(!root.getName().equals(TAG_NAME)){
            throw new XMLElementException(XMLElementException.WRONG_TAG_NAME,new XMLElement(value));
        }
    }

    //----------------SETTERS-------------------
    public void setId(String value){  setNodeText(ID_TAG_NAME, value);}
    public void setLength(int value){    setNodeText(LENGTH_TAG_NAME, String.valueOf(value));}
    public void setProteinCoverageAbsolute(int value){  setNodeText(PROTEIN_COVERAGE_ABSOLUTE, String.valueOf(value));}
    public void setProteinCoveragePercentage(double value){  setNodeText(PROTEIN_COVERAGE_PERCENTAGE, String.valueOf(value));}
    public void setNumberOfIsotigs(int value){  setNodeText(NUMBER_OF_ISOTIGS, String.valueOf(value));}
    public void setProteinName(String value){   setNodeText(NAME_TAG_NAME, value);}
    public void setFullName(String value){  setNodeText(FULL_NAME_TAG_NAME, value);}
    public void setShortName(String value){ setNodeText(SHORT_NAME_TAG_NAME, value);}
    public void setSequence(String value){  setNodeText(SEQUENCE_TAG_NAME,value);}
    public void setOrganism(String value){  setNodeText(ORGANISM_TAG_NAME, value);}

    //----------------GETTERS---------------------
    public String getId( ){  return getNodeText(ID_TAG_NAME);}
    public int getLength(){ return Integer.parseInt(getNodeText(LENGTH_TAG_NAME));}
    public int getProteinCoverageAbsolute(){ return Integer.parseInt(getNodeText(PROTEIN_COVERAGE_ABSOLUTE));}
    public double getProteinCoveragePercentage(){   return Double.parseDouble(getNodeText(PROTEIN_COVERAGE_PERCENTAGE));}
    public int getNumberOfIsotigs(){ return Integer.parseInt(getNodeText(NUMBER_OF_ISOTIGS));}
    public String getProteinName() {    return getNodeText(NAME_TAG_NAME);    }
    public String getFullName(){     return getNodeText(FULL_NAME_TAG_NAME);   }
    public String getShortName(){     return getNodeText(SHORT_NAME_TAG_NAME);   }
    public String getSequence(){    return getNodeText(SEQUENCE_TAG_NAME);}
    public String getOrganism(){    return getNodeText(ORGANISM_TAG_NAME);}

    
    public void addArticleCitation(ArticleXML article){
        initArticleCitationsTag();
        root.getChild(ARTICLE_CITATIONS_TAG_NAME).addContent(article.asJDomElement());
    }
    public void addSignalPeptideFeature(FeatureXML feature){
        initSignalPeptideTag();
        root.getChild(SIGNAL_PEPTIDE_FEATURES).addContent(feature.asJDomElement());
    }
    public void addSpliceVariantFeature(FeatureXML feature){
        initSpliceVariantTag();
        root.getChild(SPLICE_VARIANT_FEATURES).addContent(feature.asJDomElement());
    }
    public void addTransmembraneRegionFeature(FeatureXML feature){
        initTransmembraneRegionTag();
        root.getChild(TRANSMEMBRANE_REGION_FEATURES).addContent(feature.asJDomElement());
    }
    public void addActiveSiteFeature(FeatureXML feature){
        initActiveSiteTag();
        root.getChild(ACTIVE_SITE_FEATURES).addContent(feature.asJDomElement());
    }
    public void addKeyword(KeywordXML keyword){
        initKeywordsTag();
        root.getChild(KEYWORDS_TAG_NAME).addContent(keyword.asJDomElement());
    }
    public void addInterpro(InterproXML interpro){
        initInterprosTag();
        root.getChild(INTERPROS_TAG_NAME).addContent(interpro.asJDomElement());
    }
    public void addComment(CommentXML comment){
        initCommentsTag();
        root.getChild(COMMENTS_TAG_NAME).addContent(comment.asJDomElement());
    }
    public void addProteinProteinOutgoingInteraction(ProteinXML prot){
        initProteinProteinOutgoingInteractionsTag();
        root.getChild(PROTEIN_PROTEIN_OUTGOING_INTERACTIONS_TAG_NAME).addContent(prot.asJDomElement());
    }
    public void addProteinProteinIncomingInteraction(ProteinXML prot){
        initProteinProteinIncomingInteractionsTag();
        root.getChild(PROTEIN_PROTEIN_INCOMING_INTERACTIONS_TAG_NAME).addContent(prot.asJDomElement());
    }
    
    public void addProteinIsoformOutgoingInteraction(IsoformXML iso){
        initProteinIsoformOutgoingInteractionsTag();
        root.getChild(PROTEIN_ISOFORM_OUTGOING_INTERACTIONS_TAG_NAME).addContent(iso.asJDomElement());
    }
    public void addProteinIsoformIncomingInteraction(IsoformXML iso){
        initProteinIsoformIncomingInteractionsTag();
        root.getChild(PROTEIN_ISOFORM_INCOMING_INTERACTIONS_TAG_NAME).addContent(iso.asJDomElement());
    }
    public void addSubcellularLocation(SubcellularLocationXML subCell){
        initSubcellularLocationsTag();
        root.getChild(SUBCELLULAR_LOCATIONS_TAG_NAME).addContent(subCell.asJDomElement());
    }
    
    public List<KeywordXML> getKeywords() throws XMLElementException{
        List<KeywordXML> list = new ArrayList<KeywordXML>();
        Element keywords = root.getChild(KEYWORDS_TAG_NAME);
        
        List<Element> elemList = keywords.getChildren(KeywordXML.TAG_NAME);
        for (Element elem : elemList) {
            list.add(new KeywordXML(elem));
        }       
        
        return list;
    }
    
    
    public List<GoTermXML> getMolecularFunctionGoTerms(){
        Element goTerms = root.getChild(GO_TERMS_TAG_NAME);
        if(goTerms != null){
            Element molFunc = goTerms.getChild(FUNCTION_GO_TERMS_TAG_NAME);
            if(molFunc != null){
                List<Element> gos = molFunc.getChildren(GoTermXML.TAG_NAME);
                ArrayList<GoTermXML> result = new ArrayList<GoTermXML>();
                for (Element elem : gos) {
                    result.add(new GoTermXML(elem));
                }
                return result;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public List<GoTermXML> getBiologicalProcessGoTerms(){
        Element goTerms = root.getChild(GO_TERMS_TAG_NAME);
        if(goTerms != null){
            Element bioProc = goTerms.getChild(PROCESS_GO_TERMS_TAG_NAME);
            if(bioProc != null){
                List<Element> gos = bioProc.getChildren(GoTermXML.TAG_NAME);
                ArrayList<GoTermXML> result = new ArrayList<GoTermXML>();
                for (Element elem : gos) {
                    result.add(new GoTermXML(elem));
                }
                return result;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public List<GoTermXML> getCellularComponentGoTerms(){
        Element goTerms = root.getChild(GO_TERMS_TAG_NAME);
        if(goTerms != null){
            Element cellComp = goTerms.getChild(COMPONENT_GO_TERMS_TAG_NAME);
            if(cellComp != null){
                List<Element> gos = cellComp.getChildren(GoTermXML.TAG_NAME);
                ArrayList<GoTermXML> result = new ArrayList<GoTermXML>();
                for (Element elem : gos) {
                    result.add(new GoTermXML(elem));
                }
                return result;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    public void addGoTerm(GoTermXML term, boolean basedOnAspect){
        initGoTermsTag();
        if(basedOnAspect){
            if(term.getAspect().equals(GoTermXML.ASPECT_COMPONENT)){
                initComponentTag();
                root.getChild(GO_TERMS_TAG_NAME).getChild(COMPONENT_GO_TERMS_TAG_NAME).addContent(term.asJDomElement());
            }else if(term.getAspect().equals(GoTermXML.ASPECT_FUNCTION)){
                initFunctionTag();
                root.getChild(GO_TERMS_TAG_NAME).getChild(FUNCTION_GO_TERMS_TAG_NAME).addContent(term.asJDomElement());
            }else if(term.getAspect().equals(GoTermXML.ASPECT_PROCESS)){
                initProcessTag();
                root.getChild(GO_TERMS_TAG_NAME).getChild(PROCESS_GO_TERMS_TAG_NAME).addContent(term.asJDomElement());
            }
        }else{
            root.getChild(GO_TERMS_TAG_NAME).addContent(term.asJDomElement());
        }
        
    }

    public void clasifyGoTermsByAspect() throws JDOMException{
        initComponentTag();
        initFunctionTag();
        initProcessTag();

        if(doc == null){
            doc = root.getDocument();
        }
        
        XPathExpression<Element> xpProcess = XPathFactory.instance().compile("//protein[id/text()='"+getId()+"']//"+GoTermXML.TAG_NAME+"["+GoTermXML.ASPECT_TAG_NAME+"/text()='"+GoTermXML.ASPECT_PROCESS+"']", Filters.element());
        List<Element> processGoTerms = xpProcess.evaluate(doc);
        
        XPathExpression<Element> xpFunction = XPathFactory.instance().compile("//protein[id/text()='"+getId()+"']//"+GoTermXML.TAG_NAME+"["+GoTermXML.ASPECT_TAG_NAME+"/text()='"+GoTermXML.ASPECT_FUNCTION+"']", Filters.element());
        List<Element> functionGoTerms = xpFunction.evaluate(doc);
        
        XPathExpression<Element> xpComponent = XPathFactory.instance().compile("//protein[id/text()='"+getId()+"']//"+GoTermXML.TAG_NAME+"["+GoTermXML.ASPECT_TAG_NAME+"/text()='"+GoTermXML.ASPECT_COMPONENT+"']", Filters.element());
        List<Element> componentGoTerms = xpComponent.evaluate(doc);
                
        for(Element processGo : processGoTerms){
        	processGo.detach();
            this.addGoTerm(new GoTermXML(processGo), true);
        }
        for(Element componentGo : componentGoTerms){
        	componentGo.detach();
            this.addGoTerm(new GoTermXML(componentGo), true);
        }
        for(Element functionGo : functionGoTerms){
        	functionGo.detach();
            this.addGoTerm(new GoTermXML(functionGo), true);
        }
       

    }

    private void initGoTermsTag(){  initTag(GO_TERMS_TAG_NAME); }
    
    private void initComponentTag(){
        initGoTermsTag();
        Element temp = root.getChild(GO_TERMS_TAG_NAME).getChild(COMPONENT_GO_TERMS_TAG_NAME);
        if(temp == null){
            root.getChild(GO_TERMS_TAG_NAME).addContent(new Element(COMPONENT_GO_TERMS_TAG_NAME));
        }
    }
    private void initFunctionTag(){
        initGoTermsTag();
        Element temp = root.getChild(GO_TERMS_TAG_NAME).getChild(FUNCTION_GO_TERMS_TAG_NAME);
        if(temp == null){
            root.getChild(GO_TERMS_TAG_NAME).addContent(new Element(FUNCTION_GO_TERMS_TAG_NAME));
        }
    }
    private void initProcessTag(){
        initGoTermsTag();
        Element temp = root.getChild(GO_TERMS_TAG_NAME).getChild(PROCESS_GO_TERMS_TAG_NAME);
        if(temp == null){
            root.getChild(GO_TERMS_TAG_NAME).addContent(new Element(PROCESS_GO_TERMS_TAG_NAME));
        }
    }
    
    private void initKeywordsTag(){ initTag(KEYWORDS_TAG_NAME); }
    private void initInterprosTag(){    initTag(INTERPROS_TAG_NAME);  }
    private void initCommentsTag(){ initTag(COMMENTS_TAG_NAME);}
    private void initProteinProteinOutgoingInteractionsTag(){   initTag(PROTEIN_PROTEIN_OUTGOING_INTERACTIONS_TAG_NAME);}
    private void initProteinProteinIncomingInteractionsTag(){   initTag(PROTEIN_PROTEIN_INCOMING_INTERACTIONS_TAG_NAME);}
    private void initProteinIsoformOutgoingInteractionsTag(){   initTag(PROTEIN_ISOFORM_OUTGOING_INTERACTIONS_TAG_NAME);}
    private void initProteinIsoformIncomingInteractionsTag(){   initTag(PROTEIN_ISOFORM_INCOMING_INTERACTIONS_TAG_NAME);}    
    private void initSubcellularLocationsTag(){ initTag(SUBCELLULAR_LOCATIONS_TAG_NAME);}
    private void initSignalPeptideTag(){ initTag(SIGNAL_PEPTIDE_FEATURES);  }
    private void initActiveSiteTag(){ initTag(ACTIVE_SITE_FEATURES);  }
    private void initTransmembraneRegionTag(){ initTag(TRANSMEMBRANE_REGION_FEATURES);  }
    private void initSpliceVariantTag(){ initTag(SPLICE_VARIANT_FEATURES);  }
    private void initArticleCitationsTag(){ initTag(ARTICLE_CITATIONS_TAG_NAME);  }
    
    private void initTag(String tagName){
        Element temp = root.getChild(tagName);
        if(temp == null){
            root.addContent(new Element(tagName));
        }
    }

}
