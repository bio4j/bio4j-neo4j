
 * Copyright (C) 2010-2011  "Bio4j"
 *
 * This file is part of Bio4j
 *
 * Bio4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>


```java
package com.bio4j.neo4jdb.model.nodes;

import com.bio4j.neo4jdb.model.nodes.citation.*;
import com.bio4j.neo4jdb.model.nodes.reactome.ReactomeTermNode;
import com.bio4j.neo4jdb.model.nodes.refseq.GenomeElementNode;
import com.bio4j.neo4jdb.model.relationships.citation.article.ArticleProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.book.BookProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.onarticle.OnlineArticleProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.patent.PatentProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.submission.SubmissionProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.thesis.ThesisProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.uo.UnpublishedObservationProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.comment.DomainCommentRel;
import com.bio4j.neo4jdb.model.relationships.comment.FunctionCommentRel;
import com.bio4j.neo4jdb.model.relationships.comment.PathwayCommentRel;
import com.bio4j.neo4jdb.model.relationships.comment.SimilarityCommentRel;
import com.bio4j.neo4jdb.model.relationships.features.ActiveSiteFeatureRel;
import com.bio4j.neo4jdb.model.relationships.features.SignalPeptideFeatureRel;
import com.bio4j.neo4jdb.model.relationships.features.SpliceVariantFeatureRel;
import com.bio4j.neo4jdb.model.relationships.features.TransmembraneRegionFeatureRel;
import com.bio4j.neo4jdb.model.relationships.protein.*;
import com.bio4j.neo4jdb.model.relationships.uniref.UniRef100MemberRel;
import com.bio4j.neo4jdb.model.relationships.uniref.UniRef50MemberRel;
import com.bio4j.neo4jdb.model.relationships.uniref.UniRef90MemberRel;
import com.bio4j.neo4jdb.BasicEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Uniprot proteins
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class ProteinNode extends BasicEntity {

    public static final String PROTEIN_ACCESSION_INDEX = "protein_accession_index";
    public static final String PROTEIN_FULL_NAME_FULL_TEXT_INDEX = "protein_full_name_full_text_index";
    public static final String PROTEIN_GENE_NAMES_FULL_TEXT_INDEX = "protein_gene_names_full_text_index";
    public static final String PROTEIN_ENSEMBL_PLANTS_INDEX = "protein_ensembl_plants_index";

    public static final String NODE_TYPE = ProteinNode.class.getCanonicalName();
    
    public static final String NAME_PROPERTY = "protein_name";
    public static final String FULL_NAME_PROPERTY = "protein_full_name";
    public static final String SHORT_NAME_PROPERTY = "protein_short_name";
    public static final String ACCESSION_PROPERTY = "protein_accession";
    public static final String SEQUENCE_PROPERTY = "protein_sequence";
    public static final String MASS_PROPERTY = "protein_mass";
    public static final String LENGTH_PROPERTY = "protein_length";
    public static final String MODIFIED_DATE_PROPERTY = "protein_modified_date";
    public static final String GENE_NAMES_PROPERTY = "protein_gene_names";
    public static final String ENSEMBL_ID_PROPERTY = "protein_ensembl_id";
    public static final String PIR_ID_PROPERTY = "protein_pir_id";
    public static final String KEGG_ID_PROPERTY = "protein_kegg_id";
    public static final String EMBL_REFERENCES_PROPERTY = "protein_embl_references";
    public static final String REFSEQ_REFERENCES_PROPERTY = "protein_refseq_references";
    public static final String ARRAY_EXPRESS_ID_PROPERTY = "protein_array_express_id";
    public static final String UNIGENE_ID_PROPERTY = "protein_unigene_id";
    public static final String ALTERNATIVE_ACCESSIONS_PROPERTY = "protein_alternative_accessions";
    public static final String ENSEMBL_PLANTS_REFERENCES_PROPERTY = "protein_ensembl_plants_references";

    //public static final String GENE_NAMES_SEPARATOR = "\t";

    public ProteinNode(Node n) {
        super(n);
    }

    public String getName() {
        return String.valueOf(node.getProperty(NAME_PROPERTY));
    }

    public String getFullName(){
        return String.valueOf(node.getProperty(FULL_NAME_PROPERTY));
    }

    public String getShortName(){
        return String.valueOf(node.getProperty(SHORT_NAME_PROPERTY));
    }

    public String getAccession() {
        return String.valueOf(node.getProperty(ACCESSION_PROPERTY));
    }

    public String getSequence() {
        return String.valueOf(node.getProperty(SEQUENCE_PROPERTY));
    }

    public String getEnsemblId(){
        return String.valueOf(node.getProperty(ENSEMBL_ID_PROPERTY));
    }

    public String[] getEMBLreferences(){
        return (String[]) node.getProperty(EMBL_REFERENCES_PROPERTY);
    }
    
    public String[] getEnsemblPlantsReferences(){
        return (String[]) node.getProperty(ENSEMBL_PLANTS_REFERENCES_PROPERTY);
    }
    
    public String[] getRefseqReferences(){
        return (String[]) node.getProperty(REFSEQ_REFERENCES_PROPERTY);
    }

    public String[] getAlternativeAcessions(){
        return (String[]) node.getProperty(ALTERNATIVE_ACCESSIONS_PROPERTY);
    }

    public String getPIRId(){
        return String.valueOf(node.getProperty(PIR_ID_PROPERTY));
    }

    public String getKeggId(){
        return String.valueOf(node.getProperty(KEGG_ID_PROPERTY));
    }

    public String getArrayExpressId(){
        return String.valueOf(node.getProperty(ARRAY_EXPRESS_ID_PROPERTY));
    }

    public String getUniGeneId(){
        return String.valueOf(node.getProperty(UNIGENE_ID_PROPERTY));
    }

    public String getModifiedDate(){
        return String.valueOf(node.getProperty(MODIFIED_DATE_PROPERTY));
    }

    public float getMass() {
        return Float.parseFloat(String.valueOf(node.getProperty(MASS_PROPERTY)));
    }

    public int getLength() {
        return Integer.parseInt(String.valueOf(node.getProperty(LENGTH_PROPERTY)));
    }

    public String[] getGeneNames(){
        return (String[])node.getProperty(GENE_NAMES_PROPERTY);
    }

//    public String[] getGeneNamesArray(){
//        return String.valueOf(node.getProperty(GENE_NAMES_PROPERTY)).split(GENE_NAMES_SEPARATOR);
//    }

    
    public boolean isUniref50Representant(){
        return !node.getRelationships(Direction.INCOMING, new UniRef50MemberRel(null)).iterator().hasNext();
    }
    public boolean isUniref90Representant(){
        return !node.getRelationships(Direction.INCOMING, new UniRef90MemberRel(null)).iterator().hasNext();
    }
    public boolean isUniref100Representant(){
        return !node.getRelationships(Direction.INCOMING, new UniRef100MemberRel(null)).iterator().hasNext();
    }
    
    public List<ProteinNode> getUniref50ClusterThisProteinBelongsTo(){
        List<ProteinNode> list = new LinkedList<ProteinNode>();
        if(isUniref50Representant()){
            list.add(this);
            Iterator<Relationship> relIterator = node.getRelationships(Direction.OUTGOING, new UniRef50MemberRel(null)).iterator();
            while(relIterator.hasNext()){
                list.add(new ProteinNode(relIterator.next().getEndNode()));
            }
        }else{
            ProteinNode representant = new ProteinNode(node.getRelationships(Direction.INCOMING, new UniRef50MemberRel(null)).iterator().next().getStartNode());
            return representant.getUniref50ClusterThisProteinBelongsTo();
        }
        return list;
    }
    
    public List<ProteinNode> getUniref90ClusterThisProteinBelongsTo(){
        List<ProteinNode> list = new LinkedList<ProteinNode>();
        if(isUniref90Representant()){
            list.add(this);
            Iterator<Relationship> relIterator = node.getRelationships(Direction.OUTGOING, new UniRef90MemberRel(null)).iterator();
            while(relIterator.hasNext()){
                list.add(new ProteinNode(relIterator.next().getEndNode()));
            }
        }else{
            ProteinNode representant = new ProteinNode(node.getRelationships(Direction.INCOMING, new UniRef90MemberRel(null)).iterator().next().getStartNode());
            return representant.getUniref90ClusterThisProteinBelongsTo();
        }
        return list;
    }
    
    public List<ProteinNode> getUniref100ClusterThisProteinBelongsTo(){
        List<ProteinNode> list = new LinkedList<ProteinNode>();
        if(isUniref100Representant()){
            list.add(this);
            Iterator<Relationship> relIterator = node.getRelationships(Direction.OUTGOING, new UniRef100MemberRel(null)).iterator();
            while(relIterator.hasNext()){
                list.add(new ProteinNode(relIterator.next().getEndNode()));
            }
        }else{
            ProteinNode representant = new ProteinNode(node.getRelationships(Direction.INCOMING, new UniRef100MemberRel(null)).iterator().next().getStartNode());
            return representant.getUniref100ClusterThisProteinBelongsTo();
        }
        return list;
    }
    
    public OrganismNode getOrganism() {
        OrganismNode org = null;
        Relationship rel = node.getSingleRelationship(new ProteinOrganismRel(null), Direction.OUTGOING);
        if (rel != null) {
            org = new OrganismNode(rel.getEndNode());
        }
        return org;
    }
    

    public DatasetNode getDataset(){
        DatasetNode dataset = null;
        Relationship rel = node.getSingleRelationship(new ProteinDatasetRel(null), Direction.OUTGOING);
        if(rel != null){
            dataset = new DatasetNode(rel.getEndNode());
        }
        return dataset;
    }
    
    public List<GenomeElementNode> getGenomeElements(){
        List<GenomeElementNode> list = new ArrayList<>();
        Iterator<Relationship> iterator = node.getRelationships(new ProteinGenomeElementRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            list.add(new GenomeElementNode(iterator.next().getEndNode()));
        }
        return list;
    }

    
    public List<SubcellularLocationNode> getSubcellularLocations(){
        List<SubcellularLocationNode> list = new ArrayList<>();        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinSubcellularLocationRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            list.add(new SubcellularLocationNode(iterator.next().getEndNode()));
        }
        return list;
    }
    
    public List<InterproNode> getInterpro(){
        List<InterproNode> interpros = new ArrayList<InterproNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinInterproRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            InterproNode interpro = new InterproNode(iterator.next().getEndNode());
            interpros.add(interpro);                        
        }
        return interpros;  
    }
    
    public List<PfamNode> getPfamTerms(){
        List<PfamNode> pfamTerms = new ArrayList<PfamNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinPfamRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            PfamNode interpro = new PfamNode(iterator.next().getEndNode());
            pfamTerms.add(interpro);                        
        }
        return pfamTerms;  
    }
    
    public List<ReactomeTermNode> getReactomeTerms(){
        List<ReactomeTermNode> list = new LinkedList<ReactomeTermNode>();
        Iterator<Relationship> iterator = node.getRelationships(new ProteinReactomeRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            ReactomeTermNode reactomeTerm = new ReactomeTermNode(iterator.next().getEndNode());
            list.add(reactomeTerm);
        }
        return list;
    }
    
    public List<EnzymeNode> getProteinEnzymaticActivity(){
        List<EnzymeNode> list = new LinkedList<EnzymeNode>();
        Iterator<Relationship> iterator = node.getRelationships(new ProteinEnzymaticActivityRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            EnzymeNode enzyme = new EnzymeNode(iterator.next().getEndNode());
            list.add(enzyme);
        }
        return list;        
    }
    
    public List<GoTermNode> getGOAnnotations(){
        List<GoTermNode> list = new ArrayList<GoTermNode>();
        Iterator<Relationship> iterator = node.getRelationships(new ProteinGoRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            GoTermNode goTerm = new GoTermNode(iterator.next().getEndNode());
            list.add(goTerm);                        
        }        
        return list;
    }
    
    public List<KeywordNode> getKeywords(){
        List<KeywordNode> keywords = new ArrayList<KeywordNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinKeywordRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            KeywordNode keyword = new KeywordNode(iterator.next().getEndNode());
            keywords.add(keyword);                        
        }
        return keywords;  
    }
    
    public List<SignalPeptideFeatureRel> getSignalPeptideFeature(){
        List<SignalPeptideFeatureRel> list = new ArrayList<SignalPeptideFeatureRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new SignalPeptideFeatureRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            SignalPeptideFeatureRel rel = new SignalPeptideFeatureRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    public List<SpliceVariantFeatureRel> getSpliceVariantFeature(){
        List<SpliceVariantFeatureRel> list = new ArrayList<SpliceVariantFeatureRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new SpliceVariantFeatureRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            SpliceVariantFeatureRel rel = new SpliceVariantFeatureRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    public List<TransmembraneRegionFeatureRel> getTransmembraneRegionFeature(){
        List<TransmembraneRegionFeatureRel> list = new ArrayList<TransmembraneRegionFeatureRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new TransmembraneRegionFeatureRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            TransmembraneRegionFeatureRel rel = new TransmembraneRegionFeatureRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    public List<ActiveSiteFeatureRel> getActiveSiteFeature(){
        List<ActiveSiteFeatureRel> list = new ArrayList<ActiveSiteFeatureRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ActiveSiteFeatureRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            ActiveSiteFeatureRel rel = new ActiveSiteFeatureRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    
    public List<FunctionCommentRel> getFunctionComment(){
        List<FunctionCommentRel> list = new ArrayList<FunctionCommentRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new FunctionCommentRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            FunctionCommentRel rel = new FunctionCommentRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    
    public List<PathwayCommentRel> getPathwayComment(){
        List<PathwayCommentRel> list = new ArrayList<PathwayCommentRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new PathwayCommentRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            PathwayCommentRel rel = new PathwayCommentRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    
    public List<DomainCommentRel> getDomainComment(){
        List<DomainCommentRel> list = new ArrayList<DomainCommentRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new DomainCommentRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            DomainCommentRel rel = new DomainCommentRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    
    public List<SimilarityCommentRel> getSimilarityComment(){
        List<SimilarityCommentRel> list = new ArrayList<SimilarityCommentRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new SimilarityCommentRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            SimilarityCommentRel rel = new SimilarityCommentRel(iterator.next());
            list.add(rel);                        
        }        
        return list;
    }
    
    /**
     * Protein-protein outgoing interactions
     * @return 
     */
    public List<ProteinProteinInteractionRel> getProteinOutgoingInteractions(){
        List<ProteinProteinInteractionRel> list = new ArrayList<ProteinProteinInteractionRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinProteinInteractionRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            list.add(new ProteinProteinInteractionRel(iterator.next()));
        }
        
        return list;
    }
    /**
     * Protein-protein incoming interactions
     * @return 
     */
    public List<ProteinProteinInteractionRel> getProteinIncomingInteractions(){
        List<ProteinProteinInteractionRel> list = new ArrayList<ProteinProteinInteractionRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinProteinInteractionRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new ProteinProteinInteractionRel(iterator.next()));
        }
        
        return list;
    }
    
    /**
     * Protein-Isoform outgoing interactions
     * @return A list of ProteinIsoformInteraction relationships
     */
    public List<ProteinIsoformInteractionRel> getIsoformOutgoingInteractions(){
        List<ProteinIsoformInteractionRel> list = new ArrayList<ProteinIsoformInteractionRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinIsoformInteractionRel(null), Direction.OUTGOING).iterator();
        while(iterator.hasNext()){
            list.add(new ProteinIsoformInteractionRel(iterator.next()));
        }
        
        return list;
    }
    
    /**
     * Protein-Isoform incoming interactions
     * @return A list of ProteinIsoformInteraction relationships
     */
    public List<ProteinIsoformInteractionRel> getIsoformIncomingInteractions(){
        List<ProteinIsoformInteractionRel> list = new ArrayList<ProteinIsoformInteractionRel>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinIsoformInteractionRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new ProteinIsoformInteractionRel(iterator.next()));
        }
        
        return list;
    }
    
    //---------------------------------------------------------------------------------------------
    //-------------------------------------CITATIONS-----------------------------------------------
    //---------------------------------------------------------------------------------------------
    
    /**
     * Protein article citations
     * @return A list of articles
     */
    public List<ArticleNode> getArticleCitations(){
        List<ArticleNode> list = new ArrayList<ArticleNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ArticleProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new ArticleNode(iterator.next().getStartNode()));
        }
        return list;
    }
    /**
     * Protein submission citations
     * @return A list of submissions
     */
    public List<SubmissionNode> getSubmissionCitations(){
        List<SubmissionNode> list = new ArrayList<SubmissionNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new SubmissionProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new SubmissionNode(iterator.next().getStartNode()));
        }
        return list;
    }
    /**
     * Protein Online article citations
     * @return A list of online articles
     */
    public List<OnlineArticleNode> getOnlineArticleCitations(){
        List<OnlineArticleNode> list = new ArrayList<OnlineArticleNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new OnlineArticleProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new OnlineArticleNode(iterator.next().getStartNode()));
        }
        return list;
    } 
    /**
     * Protein Book citations
     * @return A list of books
     */
    public List<BookNode> getBookCitations(){
        List<BookNode> list = new ArrayList<BookNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new BookProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new BookNode(iterator.next().getStartNode()));
        }
        return list;
    }
    /**
     * Protein patent citations
     * @return A list of patents
     */
    public List<PatentNode> getPatentCitations(){
        List<PatentNode> list = new ArrayList<PatentNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new PatentProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new PatentNode(iterator.next().getStartNode()));
        }
        return list;
    }
    /**
     * Protein thesis citations
     * @return A list of Thesis
     */
    public List<ThesisNode> getThesisCitations(){
        List<ThesisNode> list = new ArrayList<ThesisNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ThesisProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new ThesisNode(iterator.next().getStartNode()));
        }
        return list;
    }
    /**
     * Protein unpublished observations citations
     * @return A list of unpublished observations
     */
    public List<UnpublishedObservationNode> getUnpublishedObservationsCitations(){
        List<UnpublishedObservationNode> list = new ArrayList<UnpublishedObservationNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new UnpublishedObservationProteinCitationRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            list.add(new UnpublishedObservationNode(iterator.next().getStartNode()));
        }
        return list;
    }
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    
    public void setName(String value) {
        node.setProperty(NAME_PROPERTY, value);
    }

    public void setFullName(String value){
        node.setProperty(FULL_NAME_PROPERTY, value);
    }

    public void setShortName(String value){
        node.setProperty(SHORT_NAME_PROPERTY, value);
    }

    public void setAccession(String value) {
        node.setProperty(ACCESSION_PROPERTY, value);
    }

    public void setSequence(String value) {
        node.setProperty(SEQUENCE_PROPERTY, value);
    }

    public void setModifiedDate(String value){
        node.setProperty(MODIFIED_DATE_PROPERTY, value);
    }

    public void setEnsemblId(String value){
        node.setProperty(ENSEMBL_ID_PROPERTY, value);
    }

    public void setKeggId(String value){
        node.setProperty(KEGG_ID_PROPERTY, value);
    }

    public void setPIRId(String value){
        node.setProperty(PIR_ID_PROPERTY, value);
    }

    public void setEMBLreferences(String[] value){
        node.setProperty(EMBL_REFERENCES_PROPERTY, value);
    }
    
    public void setEnsemblPlantsReferences(String[] value){
        node.setProperty(ENSEMBL_PLANTS_REFERENCES_PROPERTY, value);
    }

    public void setRefseqReferences(String[] value){
        node.setProperty(REFSEQ_REFERENCES_PROPERTY, value);
    }
    
    public void setAlternativeAccessions(String[] value){
        node.setProperty(ALTERNATIVE_ACCESSIONS_PROPERTY, value);
    }

    public void setArrayExpressId(String value){
        node.setProperty(ARRAY_EXPRESS_ID_PROPERTY, value);
    }

    public void setUniGeneId(String value){
        node.setProperty(UNIGENE_ID_PROPERTY, value);
    }

    public void setMass(float value) {
        node.setProperty(MASS_PROPERTY, value);
    }

    public void setLength(int value) {
        node.setProperty(LENGTH_PROPERTY, value);
    }

    public void setGeneNames(String[] value){
        node.setProperty(GENE_NAMES_PROPERTY, value);
    }
    

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProteinNode) {
            ProteinNode other = (ProteinNode) obj;
            return this.node.equals(other.node);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        
        String geneNamesSt = "";        
        for (String geneName : getGeneNames()) {
            geneNamesSt += geneName + ", ";
        }
        if(geneNamesSt.length() > 0){
            geneNamesSt = geneNamesSt.substring(0,geneNamesSt.length() - 2);
        }
        
        String emblReferencesSt = "";        
        for (String emblReference : getEMBLreferences()) {
            emblReferencesSt += emblReference + ", ";
        }
        if(emblReferencesSt.length() > 0){
            emblReferencesSt = emblReferencesSt.substring(0,emblReferencesSt.length() - 2);
        }
        
        
        
        String result = "\naccession = " + this.getAccession() +
                "\nname = " + this.getName() +
                "\nfull name = " + this.getFullName() +
                "\nshort name = " + this.getShortName() + 
                "\nmodified date = " + this.getModifiedDate() +
                "\nmass = " + this.getMass() +
                "\nlength = " + this.getLength() +
                "\ngene names = " + geneNamesSt +
                "\nEMBL references = " + emblReferencesSt +
                "\nPIR id = " + this.getPIRId() +
                "\nKegg id = " + this.getKeggId() +
                "\nArrayExpress id = " + this.getArrayExpressId() +
                "\nEnsembl id = " + this.getEnsemblId() +
                "\nUniGene id = " + this.getUniGeneId() + 
                "\nsequence = " + this.getSequence();

        return result;
    }
}

```


------

### Index

+ src
  + main
    + java
      + com
        + bio4j
          + neo4j
            + [BasicEntity.java][main/java/com/bio4j/neo4j/BasicEntity.java]
            + [BasicRelationship.java][main/java/com/bio4j/neo4j/BasicRelationship.java]
            + codesamples
              + [BiodieselProductionSample.java][main/java/com/bio4j/neo4j/codesamples/BiodieselProductionSample.java]
              + [GetEnzymeData.java][main/java/com/bio4j/neo4j/codesamples/GetEnzymeData.java]
              + [GetGenesInfo.java][main/java/com/bio4j/neo4j/codesamples/GetGenesInfo.java]
              + [GetGOAnnotationsForOrganism.java][main/java/com/bio4j/neo4j/codesamples/GetGOAnnotationsForOrganism.java]
              + [GetProteinsWithInterpro.java][main/java/com/bio4j/neo4j/codesamples/GetProteinsWithInterpro.java]
              + [RealUseCase1.java][main/java/com/bio4j/neo4j/codesamples/RealUseCase1.java]
              + [RetrieveProteinSample.java][main/java/com/bio4j/neo4j/codesamples/RetrieveProteinSample.java]
            + model
              + nodes
                + [AlternativeProductNode.java][main/java/com/bio4j/neo4j/model/nodes/AlternativeProductNode.java]
                + citation
                  + [ArticleNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/ArticleNode.java]
                  + [BookNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/BookNode.java]
                  + [DBNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/DBNode.java]
                  + [JournalNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/JournalNode.java]
                  + [OnlineArticleNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/OnlineArticleNode.java]
                  + [OnlineJournalNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/OnlineJournalNode.java]
                  + [PatentNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/PatentNode.java]
                  + [PublisherNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/PublisherNode.java]
                  + [SubmissionNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/SubmissionNode.java]
                  + [ThesisNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/ThesisNode.java]
                  + [UnpublishedObservationNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/UnpublishedObservationNode.java]
                + [CityNode.java][main/java/com/bio4j/neo4j/model/nodes/CityNode.java]
                + [CommentTypeNode.java][main/java/com/bio4j/neo4j/model/nodes/CommentTypeNode.java]
                + [ConsortiumNode.java][main/java/com/bio4j/neo4j/model/nodes/ConsortiumNode.java]
                + [CountryNode.java][main/java/com/bio4j/neo4j/model/nodes/CountryNode.java]
                + [DatasetNode.java][main/java/com/bio4j/neo4j/model/nodes/DatasetNode.java]
                + [EnzymeNode.java][main/java/com/bio4j/neo4j/model/nodes/EnzymeNode.java]
                + [FeatureTypeNode.java][main/java/com/bio4j/neo4j/model/nodes/FeatureTypeNode.java]
                + [GoTermNode.java][main/java/com/bio4j/neo4j/model/nodes/GoTermNode.java]
                + [InstituteNode.java][main/java/com/bio4j/neo4j/model/nodes/InstituteNode.java]
                + [InterproNode.java][main/java/com/bio4j/neo4j/model/nodes/InterproNode.java]
                + [IsoformNode.java][main/java/com/bio4j/neo4j/model/nodes/IsoformNode.java]
                + [KeywordNode.java][main/java/com/bio4j/neo4j/model/nodes/KeywordNode.java]
                + ncbi
                  + [NCBITaxonNode.java][main/java/com/bio4j/neo4j/model/nodes/ncbi/NCBITaxonNode.java]
                + [OrganismNode.java][main/java/com/bio4j/neo4j/model/nodes/OrganismNode.java]
                + [PersonNode.java][main/java/com/bio4j/neo4j/model/nodes/PersonNode.java]
                + [PfamNode.java][main/java/com/bio4j/neo4j/model/nodes/PfamNode.java]
                + [ProteinNode.java][main/java/com/bio4j/neo4j/model/nodes/ProteinNode.java]
                + reactome
                  + [ReactomeTermNode.java][main/java/com/bio4j/neo4j/model/nodes/reactome/ReactomeTermNode.java]
                + refseq
                  + [CDSNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/CDSNode.java]
                  + [GeneNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/GeneNode.java]
                  + [GenomeElementNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/GenomeElementNode.java]
                  + rna
                    + [MiscRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MiscRNANode.java]
                    + [MRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MRNANode.java]
                    + [NcRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/NcRNANode.java]
                    + [RNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RNANode.java]
                    + [RRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RRNANode.java]
                    + [TmRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TmRNANode.java]
                    + [TRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TRNANode.java]
                + [SequenceCautionNode.java][main/java/com/bio4j/neo4j/model/nodes/SequenceCautionNode.java]
                + [SubcellularLocationNode.java][main/java/com/bio4j/neo4j/model/nodes/SubcellularLocationNode.java]
                + [TaxonNode.java][main/java/com/bio4j/neo4j/model/nodes/TaxonNode.java]
              + relationships
                + aproducts
                  + [AlternativeProductInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductInitiationRel.java]
                  + [AlternativeProductPromoterRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductPromoterRel.java]
                  + [AlternativeProductRibosomalFrameshiftingRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java]
                  + [AlternativeProductSplicingRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductSplicingRel.java]
                + citation
                  + article
                    + [ArticleAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleAuthorRel.java]
                    + [ArticleJournalRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleJournalRel.java]
                    + [ArticleProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleProteinCitationRel.java]
                  + book
                    + [BookAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookAuthorRel.java]
                    + [BookCityRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookCityRel.java]
                    + [BookEditorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookEditorRel.java]
                    + [BookProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookProteinCitationRel.java]
                    + [BookPublisherRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookPublisherRel.java]
                  + onarticle
                    + [OnlineArticleAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleAuthorRel.java]
                    + [OnlineArticleJournalRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleJournalRel.java]
                    + [OnlineArticleProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleProteinCitationRel.java]
                  + patent
                    + [PatentAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentAuthorRel.java]
                    + [PatentProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentProteinCitationRel.java]
                  + submission
                    + [SubmissionAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionAuthorRel.java]
                    + [SubmissionDbRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionDbRel.java]
                    + [SubmissionProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionProteinCitationRel.java]
                  + thesis
                    + [ThesisAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisAuthorRel.java]
                    + [ThesisInstituteRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisInstituteRel.java]
                    + [ThesisProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisProteinCitationRel.java]
                  + uo
                    + [UnpublishedObservationAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationAuthorRel.java]
                    + [UnpublishedObservationProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationProteinCitationRel.java]
                + comment
                  + [AllergenCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/AllergenCommentRel.java]
                  + [BasicCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BasicCommentRel.java]
                  + [BioPhysicoChemicalPropertiesCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java]
                  + [BiotechnologyCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BiotechnologyCommentRel.java]
                  + [CatalyticActivityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CatalyticActivityCommentRel.java]
                  + [CautionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CautionCommentRel.java]
                  + [CofactorCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CofactorCommentRel.java]
                  + [DevelopmentalStageCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DevelopmentalStageCommentRel.java]
                  + [DiseaseCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DiseaseCommentRel.java]
                  + [DisruptionPhenotypeCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DisruptionPhenotypeCommentRel.java]
                  + [DomainCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DomainCommentRel.java]
                  + [EnzymeRegulationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/EnzymeRegulationCommentRel.java]
                  + [FunctionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/FunctionCommentRel.java]
                  + [InductionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/InductionCommentRel.java]
                  + [MassSpectrometryCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/MassSpectrometryCommentRel.java]
                  + [MiscellaneousCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/MiscellaneousCommentRel.java]
                  + [OnlineInformationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/OnlineInformationCommentRel.java]
                  + [PathwayCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PathwayCommentRel.java]
                  + [PharmaceuticalCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PharmaceuticalCommentRel.java]
                  + [PolymorphismCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PolymorphismCommentRel.java]
                  + [PostTranslationalModificationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PostTranslationalModificationCommentRel.java]
                  + [RnaEditingCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/RnaEditingCommentRel.java]
                  + [SimilarityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/SimilarityCommentRel.java]
                  + [SubunitCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/SubunitCommentRel.java]
                  + [TissueSpecificityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/TissueSpecificityCommentRel.java]
                  + [ToxicDoseCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/ToxicDoseCommentRel.java]
                + features
                  + [ActiveSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ActiveSiteFeatureRel.java]
                  + [BasicFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/BasicFeatureRel.java]
                  + [BindingSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/BindingSiteFeatureRel.java]
                  + [CalciumBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CalciumBindingRegionFeatureRel.java]
                  + [ChainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ChainFeatureRel.java]
                  + [CoiledCoilRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CoiledCoilRegionFeatureRel.java]
                  + [CompositionallyBiasedRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CompositionallyBiasedRegionFeatureRel.java]
                  + [CrossLinkFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CrossLinkFeatureRel.java]
                  + [DisulfideBondFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DisulfideBondFeatureRel.java]
                  + [DnaBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DnaBindingRegionFeatureRel.java]
                  + [DomainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DomainFeatureRel.java]
                  + [GlycosylationSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/GlycosylationSiteFeatureRel.java]
                  + [HelixFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/HelixFeatureRel.java]
                  + [InitiatorMethionineFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/InitiatorMethionineFeatureRel.java]
                  + [IntramembraneRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/IntramembraneRegionFeatureRel.java]
                  + [LipidMoietyBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/LipidMoietyBindingRegionFeatureRel.java]
                  + [MetalIonBindingSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/MetalIonBindingSiteFeatureRel.java]
                  + [ModifiedResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ModifiedResidueFeatureRel.java]
                  + [MutagenesisSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/MutagenesisSiteFeatureRel.java]
                  + [NonConsecutiveResiduesFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonConsecutiveResiduesFeatureRel.java]
                  + [NonStandardAminoAcidFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonStandardAminoAcidFeatureRel.java]
                  + [NonTerminalResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonTerminalResidueFeatureRel.java]
                  + [NucleotidePhosphateBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java]
                  + [PeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/PeptideFeatureRel.java]
                  + [PropeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/PropeptideFeatureRel.java]
                  + [RegionOfInterestFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/RegionOfInterestFeatureRel.java]
                  + [RepeatFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/RepeatFeatureRel.java]
                  + [SequenceConflictFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SequenceConflictFeatureRel.java]
                  + [SequenceVariantFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SequenceVariantFeatureRel.java]
                  + [ShortSequenceMotifFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ShortSequenceMotifFeatureRel.java]
                  + [SignalPeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SignalPeptideFeatureRel.java]
                  + [SiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SiteFeatureRel.java]
                  + [SpliceVariantFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SpliceVariantFeatureRel.java]
                  + [StrandFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/StrandFeatureRel.java]
                  + [TopologicalDomainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TopologicalDomainFeatureRel.java]
                  + [TransitPeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TransitPeptideFeatureRel.java]
                  + [TransmembraneRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TransmembraneRegionFeatureRel.java]
                  + [TurnFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TurnFeatureRel.java]
                  + [UnsureResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/UnsureResidueFeatureRel.java]
                  + [ZincFingerRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ZincFingerRegionFeatureRel.java]
                + go
                  + [BiologicalProcessRel.java][main/java/com/bio4j/neo4j/model/relationships/go/BiologicalProcessRel.java]
                  + [CellularComponentRel.java][main/java/com/bio4j/neo4j/model/relationships/go/CellularComponentRel.java]
                  + [HasPartOfGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/HasPartOfGoRel.java]
                  + [IsAGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/IsAGoRel.java]
                  + [MainGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/MainGoRel.java]
                  + [MolecularFunctionRel.java][main/java/com/bio4j/neo4j/model/relationships/go/MolecularFunctionRel.java]
                  + [NegativelyRegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/NegativelyRegulatesGoRel.java]
                  + [PartOfGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/PartOfGoRel.java]
                  + [PositivelyRegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/PositivelyRegulatesGoRel.java]
                  + [RegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/RegulatesGoRel.java]
                + [InstituteCountryRel.java][main/java/com/bio4j/neo4j/model/relationships/InstituteCountryRel.java]
                + [IsoformEventGeneratorRel.java][main/java/com/bio4j/neo4j/model/relationships/IsoformEventGeneratorRel.java]
                + ncbi
                  + [NCBIMainTaxonRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBIMainTaxonRel.java]
                  + [NCBITaxonParentRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonParentRel.java]
                  + [NCBITaxonRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonRel.java]
                + protein
                  + [BasicProteinSequenceCautionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/BasicProteinSequenceCautionRel.java]
                  + [ProteinDatasetRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinDatasetRel.java]
                  + [ProteinEnzymaticActivityRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinEnzymaticActivityRel.java]
                  + [ProteinErroneousGeneModelPredictionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousGeneModelPredictionRel.java]
                  + [ProteinErroneousInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousInitiationRel.java]
                  + [ProteinErroneousTerminationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTerminationRel.java]
                  + [ProteinErroneousTranslationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTranslationRel.java]
                  + [ProteinFrameshiftRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinFrameshiftRel.java]
                  + [ProteinGenomeElementRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGenomeElementRel.java]
                  + [ProteinGoRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGoRel.java]
                  + [ProteinInterproRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinInterproRel.java]
                  + [ProteinIsoformInteractionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformInteractionRel.java]
                  + [ProteinIsoformRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformRel.java]
                  + [ProteinKeywordRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinKeywordRel.java]
                  + [ProteinMiscellaneousDiscrepancyRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinMiscellaneousDiscrepancyRel.java]
                  + [ProteinOrganismRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinOrganismRel.java]
                  + [ProteinPfamRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinPfamRel.java]
                  + [ProteinProteinInteractionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinProteinInteractionRel.java]
                  + [ProteinReactomeRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinReactomeRel.java]
                  + [ProteinSubcellularLocationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinSubcellularLocationRel.java]
                + refseq
                  + [GenomeElementCDSRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementCDSRel.java]
                  + [GenomeElementGeneRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementGeneRel.java]
                  + [GenomeElementMiscRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMiscRnaRel.java]
                  + [GenomeElementMRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMRnaRel.java]
                  + [GenomeElementNcRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementNcRnaRel.java]
                  + [GenomeElementRRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementRRnaRel.java]
                  + [GenomeElementTmRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTmRnaRel.java]
                  + [GenomeElementTRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTRnaRel.java]
                + sc
                  + [ErroneousGeneModelPredictionRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousGeneModelPredictionRel.java]
                  + [ErroneousInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousInitiationRel.java]
                  + [ErroneousTerminationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTerminationRel.java]
                  + [ErroneousTranslationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTranslationRel.java]
                  + [FrameshiftRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/FrameshiftRel.java]
                  + [MiscellaneousDiscrepancyRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/MiscellaneousDiscrepancyRel.java]
                + [SubcellularLocationParentRel.java][main/java/com/bio4j/neo4j/model/relationships/SubcellularLocationParentRel.java]
                + [TaxonParentRel.java][main/java/com/bio4j/neo4j/model/relationships/TaxonParentRel.java]
                + uniref
                  + [UniRef100MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef100MemberRel.java]
                  + [UniRef50MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef50MemberRel.java]
                  + [UniRef90MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef90MemberRel.java]
              + util
                + [Bio4jManager.java][main/java/com/bio4j/neo4j/model/util/Bio4jManager.java]
                + [GoUtil.java][main/java/com/bio4j/neo4j/model/util/GoUtil.java]
                + [NodeIndexer.java][main/java/com/bio4j/neo4j/model/util/NodeIndexer.java]
                + [NodeRetriever.java][main/java/com/bio4j/neo4j/model/util/NodeRetriever.java]
                + [UniprotStuff.java][main/java/com/bio4j/neo4j/model/util/UniprotStuff.java]
            + [Neo4jManager.java][main/java/com/bio4j/neo4j/Neo4jManager.java]
            + programs
              + [GetProteinData.java][main/java/com/bio4j/neo4j/programs/GetProteinData.java]
              + [ImportEnzymeDB.java][main/java/com/bio4j/neo4j/programs/ImportEnzymeDB.java]
              + [ImportGeneOntology.java][main/java/com/bio4j/neo4j/programs/ImportGeneOntology.java]
              + [ImportIsoformSequences.java][main/java/com/bio4j/neo4j/programs/ImportIsoformSequences.java]
              + [ImportNCBITaxonomy.java][main/java/com/bio4j/neo4j/programs/ImportNCBITaxonomy.java]
              + [ImportNeo4jDB.java][main/java/com/bio4j/neo4j/programs/ImportNeo4jDB.java]
              + [ImportProteinInteractions.java][main/java/com/bio4j/neo4j/programs/ImportProteinInteractions.java]
              + [ImportRefSeq.java][main/java/com/bio4j/neo4j/programs/ImportRefSeq.java]
              + [ImportUniprot.java][main/java/com/bio4j/neo4j/programs/ImportUniprot.java]
              + [ImportUniref.java][main/java/com/bio4j/neo4j/programs/ImportUniref.java]
              + [IndexNCBITaxonomyByGiId.java][main/java/com/bio4j/neo4j/programs/IndexNCBITaxonomyByGiId.java]
              + [InitBio4jDB.java][main/java/com/bio4j/neo4j/programs/InitBio4jDB.java]
              + [UploadRefSeqSequencesToS3.java][main/java/com/bio4j/neo4j/programs/UploadRefSeqSequencesToS3.java]
        + ohnosequences
          + util
            + [Executable.java][main/java/com/ohnosequences/util/Executable.java]
            + [ExecuteFromFile.java][main/java/com/ohnosequences/util/ExecuteFromFile.java]
            + genbank
              + [GBCommon.java][main/java/com/ohnosequences/util/genbank/GBCommon.java]
          + xml
            + api
              + interfaces
                + [IAttribute.java][main/java/com/ohnosequences/xml/api/interfaces/IAttribute.java]
                + [IElement.java][main/java/com/ohnosequences/xml/api/interfaces/IElement.java]
                + [INameSpace.java][main/java/com/ohnosequences/xml/api/interfaces/INameSpace.java]
                + [IXmlThing.java][main/java/com/ohnosequences/xml/api/interfaces/IXmlThing.java]
                + [package-info.java][main/java/com/ohnosequences/xml/api/interfaces/package-info.java]
              + model
                + [NameSpace.java][main/java/com/ohnosequences/xml/api/model/NameSpace.java]
                + [package-info.java][main/java/com/ohnosequences/xml/api/model/package-info.java]
                + [XMLAttribute.java][main/java/com/ohnosequences/xml/api/model/XMLAttribute.java]
                + [XMLElement.java][main/java/com/ohnosequences/xml/api/model/XMLElement.java]
                + [XMLElementException.java][main/java/com/ohnosequences/xml/api/model/XMLElementException.java]
              + util
                + [XMLUtil.java][main/java/com/ohnosequences/xml/api/util/XMLUtil.java]
            + model
              + bio4j
                + [Bio4jNodeIndexXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java]
                + [Bio4jNodeXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeXML.java]
                + [Bio4jPropertyXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java]
                + [Bio4jRelationshipIndexXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java]
                + [Bio4jRelationshipXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java]
                + [UniprotDataXML.java][main/java/com/ohnosequences/xml/model/bio4j/UniprotDataXML.java]
              + go
                + [GoAnnotationXML.java][main/java/com/ohnosequences/xml/model/go/GoAnnotationXML.java]
                + [GOSlimXML.java][main/java/com/ohnosequences/xml/model/go/GOSlimXML.java]
                + [GoTermXML.java][main/java/com/ohnosequences/xml/model/go/GoTermXML.java]
                + [SlimSetXML.java][main/java/com/ohnosequences/xml/model/go/SlimSetXML.java]
              + uniprot
                + [ArticleXML.java][main/java/com/ohnosequences/xml/model/uniprot/ArticleXML.java]
                + [CommentXML.java][main/java/com/ohnosequences/xml/model/uniprot/CommentXML.java]
                + [FeatureXML.java][main/java/com/ohnosequences/xml/model/uniprot/FeatureXML.java]
                + [InterproXML.java][main/java/com/ohnosequences/xml/model/uniprot/InterproXML.java]
                + [IsoformXML.java][main/java/com/ohnosequences/xml/model/uniprot/IsoformXML.java]
                + [KeywordXML.java][main/java/com/ohnosequences/xml/model/uniprot/KeywordXML.java]
                + [ProteinXML.java][main/java/com/ohnosequences/xml/model/uniprot/ProteinXML.java]
                + [SubcellularLocationXML.java][main/java/com/ohnosequences/xml/model/uniprot/SubcellularLocationXML.java]
              + util
                + [Argument.java][main/java/com/ohnosequences/xml/model/util/Argument.java]
                + [Arguments.java][main/java/com/ohnosequences/xml/model/util/Arguments.java]
                + [Error.java][main/java/com/ohnosequences/xml/model/util/Error.java]
                + [Execution.java][main/java/com/ohnosequences/xml/model/util/Execution.java]
                + [FlexXMLWrapperClassCreator.java][main/java/com/ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java]
                + [ScheduledExecutions.java][main/java/com/ohnosequences/xml/model/util/ScheduledExecutions.java]
                + [XMLWrapperClass.java][main/java/com/ohnosequences/xml/model/util/XMLWrapperClass.java]
                + [XMLWrapperClassCreator.java][main/java/com/ohnosequences/xml/model/util/XMLWrapperClassCreator.java]

[main/java/com/bio4j/neo4j/BasicEntity.java]: ../../BasicEntity.java.md
[main/java/com/bio4j/neo4j/BasicRelationship.java]: ../../BasicRelationship.java.md
[main/java/com/bio4j/neo4j/codesamples/BiodieselProductionSample.java]: ../../codesamples/BiodieselProductionSample.java.md
[main/java/com/bio4j/neo4j/codesamples/GetEnzymeData.java]: ../../codesamples/GetEnzymeData.java.md
[main/java/com/bio4j/neo4j/codesamples/GetGenesInfo.java]: ../../codesamples/GetGenesInfo.java.md
[main/java/com/bio4j/neo4j/codesamples/GetGOAnnotationsForOrganism.java]: ../../codesamples/GetGOAnnotationsForOrganism.java.md
[main/java/com/bio4j/neo4j/codesamples/GetProteinsWithInterpro.java]: ../../codesamples/GetProteinsWithInterpro.java.md
[main/java/com/bio4j/neo4j/codesamples/RealUseCase1.java]: ../../codesamples/RealUseCase1.java.md
[main/java/com/bio4j/neo4j/codesamples/RetrieveProteinSample.java]: ../../codesamples/RetrieveProteinSample.java.md
[main/java/com/bio4j/neo4j/model/nodes/AlternativeProductNode.java]: AlternativeProductNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ArticleNode.java]: citation/ArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/BookNode.java]: citation/BookNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/DBNode.java]: citation/DBNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/JournalNode.java]: citation/JournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineArticleNode.java]: citation/OnlineArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineJournalNode.java]: citation/OnlineJournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PatentNode.java]: citation/PatentNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PublisherNode.java]: citation/PublisherNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/SubmissionNode.java]: citation/SubmissionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ThesisNode.java]: citation/ThesisNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/UnpublishedObservationNode.java]: citation/UnpublishedObservationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CityNode.java]: CityNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CommentTypeNode.java]: CommentTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ConsortiumNode.java]: ConsortiumNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CountryNode.java]: CountryNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/DatasetNode.java]: DatasetNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/EnzymeNode.java]: EnzymeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/FeatureTypeNode.java]: FeatureTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/GoTermNode.java]: GoTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InstituteNode.java]: InstituteNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InterproNode.java]: InterproNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/IsoformNode.java]: IsoformNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/KeywordNode.java]: KeywordNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ncbi/NCBITaxonNode.java]: ncbi/NCBITaxonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/OrganismNode.java]: OrganismNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PersonNode.java]: PersonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PfamNode.java]: PfamNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ProteinNode.java]: ProteinNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/reactome/ReactomeTermNode.java]: reactome/ReactomeTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/CDSNode.java]: refseq/CDSNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GeneNode.java]: refseq/GeneNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GenomeElementNode.java]: refseq/GenomeElementNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MiscRNANode.java]: refseq/rna/MiscRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MRNANode.java]: refseq/rna/MRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/NcRNANode.java]: refseq/rna/NcRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RNANode.java]: refseq/rna/RNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RRNANode.java]: refseq/rna/RRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TmRNANode.java]: refseq/rna/TmRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TRNANode.java]: refseq/rna/TRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SequenceCautionNode.java]: SequenceCautionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SubcellularLocationNode.java]: SubcellularLocationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/TaxonNode.java]: TaxonNode.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductInitiationRel.java]: ../relationships/aproducts/AlternativeProductInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductPromoterRel.java]: ../relationships/aproducts/AlternativeProductPromoterRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java]: ../relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductSplicingRel.java]: ../relationships/aproducts/AlternativeProductSplicingRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleAuthorRel.java]: ../relationships/citation/article/ArticleAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleJournalRel.java]: ../relationships/citation/article/ArticleJournalRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleProteinCitationRel.java]: ../relationships/citation/article/ArticleProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookAuthorRel.java]: ../relationships/citation/book/BookAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookCityRel.java]: ../relationships/citation/book/BookCityRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookEditorRel.java]: ../relationships/citation/book/BookEditorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookProteinCitationRel.java]: ../relationships/citation/book/BookProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookPublisherRel.java]: ../relationships/citation/book/BookPublisherRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleAuthorRel.java]: ../relationships/citation/onarticle/OnlineArticleAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleJournalRel.java]: ../relationships/citation/onarticle/OnlineArticleJournalRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleProteinCitationRel.java]: ../relationships/citation/onarticle/OnlineArticleProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentAuthorRel.java]: ../relationships/citation/patent/PatentAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentProteinCitationRel.java]: ../relationships/citation/patent/PatentProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionAuthorRel.java]: ../relationships/citation/submission/SubmissionAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionDbRel.java]: ../relationships/citation/submission/SubmissionDbRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionProteinCitationRel.java]: ../relationships/citation/submission/SubmissionProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisAuthorRel.java]: ../relationships/citation/thesis/ThesisAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisInstituteRel.java]: ../relationships/citation/thesis/ThesisInstituteRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisProteinCitationRel.java]: ../relationships/citation/thesis/ThesisProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationAuthorRel.java]: ../relationships/citation/uo/UnpublishedObservationAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationProteinCitationRel.java]: ../relationships/citation/uo/UnpublishedObservationProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/AllergenCommentRel.java]: ../relationships/comment/AllergenCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BasicCommentRel.java]: ../relationships/comment/BasicCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java]: ../relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BiotechnologyCommentRel.java]: ../relationships/comment/BiotechnologyCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CatalyticActivityCommentRel.java]: ../relationships/comment/CatalyticActivityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CautionCommentRel.java]: ../relationships/comment/CautionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CofactorCommentRel.java]: ../relationships/comment/CofactorCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DevelopmentalStageCommentRel.java]: ../relationships/comment/DevelopmentalStageCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DiseaseCommentRel.java]: ../relationships/comment/DiseaseCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DisruptionPhenotypeCommentRel.java]: ../relationships/comment/DisruptionPhenotypeCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DomainCommentRel.java]: ../relationships/comment/DomainCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/EnzymeRegulationCommentRel.java]: ../relationships/comment/EnzymeRegulationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/FunctionCommentRel.java]: ../relationships/comment/FunctionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/InductionCommentRel.java]: ../relationships/comment/InductionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/MassSpectrometryCommentRel.java]: ../relationships/comment/MassSpectrometryCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/MiscellaneousCommentRel.java]: ../relationships/comment/MiscellaneousCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/OnlineInformationCommentRel.java]: ../relationships/comment/OnlineInformationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PathwayCommentRel.java]: ../relationships/comment/PathwayCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PharmaceuticalCommentRel.java]: ../relationships/comment/PharmaceuticalCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PolymorphismCommentRel.java]: ../relationships/comment/PolymorphismCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PostTranslationalModificationCommentRel.java]: ../relationships/comment/PostTranslationalModificationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/RnaEditingCommentRel.java]: ../relationships/comment/RnaEditingCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/SimilarityCommentRel.java]: ../relationships/comment/SimilarityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/SubunitCommentRel.java]: ../relationships/comment/SubunitCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/TissueSpecificityCommentRel.java]: ../relationships/comment/TissueSpecificityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/ToxicDoseCommentRel.java]: ../relationships/comment/ToxicDoseCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ActiveSiteFeatureRel.java]: ../relationships/features/ActiveSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/BasicFeatureRel.java]: ../relationships/features/BasicFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/BindingSiteFeatureRel.java]: ../relationships/features/BindingSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CalciumBindingRegionFeatureRel.java]: ../relationships/features/CalciumBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ChainFeatureRel.java]: ../relationships/features/ChainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CoiledCoilRegionFeatureRel.java]: ../relationships/features/CoiledCoilRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CompositionallyBiasedRegionFeatureRel.java]: ../relationships/features/CompositionallyBiasedRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CrossLinkFeatureRel.java]: ../relationships/features/CrossLinkFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DisulfideBondFeatureRel.java]: ../relationships/features/DisulfideBondFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DnaBindingRegionFeatureRel.java]: ../relationships/features/DnaBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DomainFeatureRel.java]: ../relationships/features/DomainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/GlycosylationSiteFeatureRel.java]: ../relationships/features/GlycosylationSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/HelixFeatureRel.java]: ../relationships/features/HelixFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/InitiatorMethionineFeatureRel.java]: ../relationships/features/InitiatorMethionineFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/IntramembraneRegionFeatureRel.java]: ../relationships/features/IntramembraneRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/LipidMoietyBindingRegionFeatureRel.java]: ../relationships/features/LipidMoietyBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/MetalIonBindingSiteFeatureRel.java]: ../relationships/features/MetalIonBindingSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ModifiedResidueFeatureRel.java]: ../relationships/features/ModifiedResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/MutagenesisSiteFeatureRel.java]: ../relationships/features/MutagenesisSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonConsecutiveResiduesFeatureRel.java]: ../relationships/features/NonConsecutiveResiduesFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonStandardAminoAcidFeatureRel.java]: ../relationships/features/NonStandardAminoAcidFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonTerminalResidueFeatureRel.java]: ../relationships/features/NonTerminalResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java]: ../relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/PeptideFeatureRel.java]: ../relationships/features/PeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/PropeptideFeatureRel.java]: ../relationships/features/PropeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/RegionOfInterestFeatureRel.java]: ../relationships/features/RegionOfInterestFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/RepeatFeatureRel.java]: ../relationships/features/RepeatFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SequenceConflictFeatureRel.java]: ../relationships/features/SequenceConflictFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SequenceVariantFeatureRel.java]: ../relationships/features/SequenceVariantFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ShortSequenceMotifFeatureRel.java]: ../relationships/features/ShortSequenceMotifFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SignalPeptideFeatureRel.java]: ../relationships/features/SignalPeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SiteFeatureRel.java]: ../relationships/features/SiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SpliceVariantFeatureRel.java]: ../relationships/features/SpliceVariantFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/StrandFeatureRel.java]: ../relationships/features/StrandFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TopologicalDomainFeatureRel.java]: ../relationships/features/TopologicalDomainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TransitPeptideFeatureRel.java]: ../relationships/features/TransitPeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TransmembraneRegionFeatureRel.java]: ../relationships/features/TransmembraneRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TurnFeatureRel.java]: ../relationships/features/TurnFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/UnsureResidueFeatureRel.java]: ../relationships/features/UnsureResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ZincFingerRegionFeatureRel.java]: ../relationships/features/ZincFingerRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/BiologicalProcessRel.java]: ../relationships/go/BiologicalProcessRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/CellularComponentRel.java]: ../relationships/go/CellularComponentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/HasPartOfGoRel.java]: ../relationships/go/HasPartOfGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/IsAGoRel.java]: ../relationships/go/IsAGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/MainGoRel.java]: ../relationships/go/MainGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/MolecularFunctionRel.java]: ../relationships/go/MolecularFunctionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/NegativelyRegulatesGoRel.java]: ../relationships/go/NegativelyRegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/PartOfGoRel.java]: ../relationships/go/PartOfGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/PositivelyRegulatesGoRel.java]: ../relationships/go/PositivelyRegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/RegulatesGoRel.java]: ../relationships/go/RegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/InstituteCountryRel.java]: ../relationships/InstituteCountryRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/IsoformEventGeneratorRel.java]: ../relationships/IsoformEventGeneratorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBIMainTaxonRel.java]: ../relationships/ncbi/NCBIMainTaxonRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonParentRel.java]: ../relationships/ncbi/NCBITaxonParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonRel.java]: ../relationships/ncbi/NCBITaxonRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/BasicProteinSequenceCautionRel.java]: ../relationships/protein/BasicProteinSequenceCautionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinDatasetRel.java]: ../relationships/protein/ProteinDatasetRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinEnzymaticActivityRel.java]: ../relationships/protein/ProteinEnzymaticActivityRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousGeneModelPredictionRel.java]: ../relationships/protein/ProteinErroneousGeneModelPredictionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousInitiationRel.java]: ../relationships/protein/ProteinErroneousInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTerminationRel.java]: ../relationships/protein/ProteinErroneousTerminationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTranslationRel.java]: ../relationships/protein/ProteinErroneousTranslationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinFrameshiftRel.java]: ../relationships/protein/ProteinFrameshiftRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGenomeElementRel.java]: ../relationships/protein/ProteinGenomeElementRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGoRel.java]: ../relationships/protein/ProteinGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinInterproRel.java]: ../relationships/protein/ProteinInterproRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformInteractionRel.java]: ../relationships/protein/ProteinIsoformInteractionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformRel.java]: ../relationships/protein/ProteinIsoformRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinKeywordRel.java]: ../relationships/protein/ProteinKeywordRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinMiscellaneousDiscrepancyRel.java]: ../relationships/protein/ProteinMiscellaneousDiscrepancyRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinOrganismRel.java]: ../relationships/protein/ProteinOrganismRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinPfamRel.java]: ../relationships/protein/ProteinPfamRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinProteinInteractionRel.java]: ../relationships/protein/ProteinProteinInteractionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinReactomeRel.java]: ../relationships/protein/ProteinReactomeRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinSubcellularLocationRel.java]: ../relationships/protein/ProteinSubcellularLocationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementCDSRel.java]: ../relationships/refseq/GenomeElementCDSRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementGeneRel.java]: ../relationships/refseq/GenomeElementGeneRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMiscRnaRel.java]: ../relationships/refseq/GenomeElementMiscRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMRnaRel.java]: ../relationships/refseq/GenomeElementMRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementNcRnaRel.java]: ../relationships/refseq/GenomeElementNcRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementRRnaRel.java]: ../relationships/refseq/GenomeElementRRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTmRnaRel.java]: ../relationships/refseq/GenomeElementTmRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTRnaRel.java]: ../relationships/refseq/GenomeElementTRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousGeneModelPredictionRel.java]: ../relationships/sc/ErroneousGeneModelPredictionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousInitiationRel.java]: ../relationships/sc/ErroneousInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTerminationRel.java]: ../relationships/sc/ErroneousTerminationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTranslationRel.java]: ../relationships/sc/ErroneousTranslationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/FrameshiftRel.java]: ../relationships/sc/FrameshiftRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/MiscellaneousDiscrepancyRel.java]: ../relationships/sc/MiscellaneousDiscrepancyRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/SubcellularLocationParentRel.java]: ../relationships/SubcellularLocationParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/TaxonParentRel.java]: ../relationships/TaxonParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef100MemberRel.java]: ../relationships/uniref/UniRef100MemberRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef50MemberRel.java]: ../relationships/uniref/UniRef50MemberRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef90MemberRel.java]: ../relationships/uniref/UniRef90MemberRel.java.md
[main/java/com/bio4j/neo4j/model/util/Bio4jManager.java]: ../util/Bio4jManager.java.md
[main/java/com/bio4j/neo4j/model/util/GoUtil.java]: ../util/GoUtil.java.md
[main/java/com/bio4j/neo4j/model/util/NodeIndexer.java]: ../util/NodeIndexer.java.md
[main/java/com/bio4j/neo4j/model/util/NodeRetriever.java]: ../util/NodeRetriever.java.md
[main/java/com/bio4j/neo4j/model/util/UniprotStuff.java]: ../util/UniprotStuff.java.md
[main/java/com/bio4j/neo4j/Neo4jManager.java]: ../../Neo4jManager.java.md
[main/java/com/bio4j/neo4j/programs/GetProteinData.java]: ../../programs/GetProteinData.java.md
[main/java/com/bio4j/neo4j/programs/ImportEnzymeDB.java]: ../../programs/ImportEnzymeDB.java.md
[main/java/com/bio4j/neo4j/programs/ImportGeneOntology.java]: ../../programs/ImportGeneOntology.java.md
[main/java/com/bio4j/neo4j/programs/ImportIsoformSequences.java]: ../../programs/ImportIsoformSequences.java.md
[main/java/com/bio4j/neo4j/programs/ImportNCBITaxonomy.java]: ../../programs/ImportNCBITaxonomy.java.md
[main/java/com/bio4j/neo4j/programs/ImportNeo4jDB.java]: ../../programs/ImportNeo4jDB.java.md
[main/java/com/bio4j/neo4j/programs/ImportProteinInteractions.java]: ../../programs/ImportProteinInteractions.java.md
[main/java/com/bio4j/neo4j/programs/ImportRefSeq.java]: ../../programs/ImportRefSeq.java.md
[main/java/com/bio4j/neo4j/programs/ImportUniprot.java]: ../../programs/ImportUniprot.java.md
[main/java/com/bio4j/neo4j/programs/ImportUniref.java]: ../../programs/ImportUniref.java.md
[main/java/com/bio4j/neo4j/programs/IndexNCBITaxonomyByGiId.java]: ../../programs/IndexNCBITaxonomyByGiId.java.md
[main/java/com/bio4j/neo4j/programs/InitBio4jDB.java]: ../../programs/InitBio4jDB.java.md
[main/java/com/bio4j/neo4j/programs/UploadRefSeqSequencesToS3.java]: ../../programs/UploadRefSeqSequencesToS3.java.md
[main/java/com/ohnosequences/util/Executable.java]: ../../../../ohnosequences/util/Executable.java.md
[main/java/com/ohnosequences/util/ExecuteFromFile.java]: ../../../../ohnosequences/util/ExecuteFromFile.java.md
[main/java/com/ohnosequences/util/genbank/GBCommon.java]: ../../../../ohnosequences/util/genbank/GBCommon.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IAttribute.java]: ../../../../ohnosequences/xml/api/interfaces/IAttribute.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IElement.java]: ../../../../ohnosequences/xml/api/interfaces/IElement.java.md
[main/java/com/ohnosequences/xml/api/interfaces/INameSpace.java]: ../../../../ohnosequences/xml/api/interfaces/INameSpace.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IXmlThing.java]: ../../../../ohnosequences/xml/api/interfaces/IXmlThing.java.md
[main/java/com/ohnosequences/xml/api/interfaces/package-info.java]: ../../../../ohnosequences/xml/api/interfaces/package-info.java.md
[main/java/com/ohnosequences/xml/api/model/NameSpace.java]: ../../../../ohnosequences/xml/api/model/NameSpace.java.md
[main/java/com/ohnosequences/xml/api/model/package-info.java]: ../../../../ohnosequences/xml/api/model/package-info.java.md
[main/java/com/ohnosequences/xml/api/model/XMLAttribute.java]: ../../../../ohnosequences/xml/api/model/XMLAttribute.java.md
[main/java/com/ohnosequences/xml/api/model/XMLElement.java]: ../../../../ohnosequences/xml/api/model/XMLElement.java.md
[main/java/com/ohnosequences/xml/api/model/XMLElementException.java]: ../../../../ohnosequences/xml/api/model/XMLElementException.java.md
[main/java/com/ohnosequences/xml/api/util/XMLUtil.java]: ../../../../ohnosequences/xml/api/util/XMLUtil.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java]: ../../../../ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeXML.java]: ../../../../ohnosequences/xml/model/bio4j/Bio4jNodeXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java]: ../../../../ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java]: ../../../../ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java]: ../../../../ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/UniprotDataXML.java]: ../../../../ohnosequences/xml/model/bio4j/UniprotDataXML.java.md
[main/java/com/ohnosequences/xml/model/go/GoAnnotationXML.java]: ../../../../ohnosequences/xml/model/go/GoAnnotationXML.java.md
[main/java/com/ohnosequences/xml/model/go/GOSlimXML.java]: ../../../../ohnosequences/xml/model/go/GOSlimXML.java.md
[main/java/com/ohnosequences/xml/model/go/GoTermXML.java]: ../../../../ohnosequences/xml/model/go/GoTermXML.java.md
[main/java/com/ohnosequences/xml/model/go/SlimSetXML.java]: ../../../../ohnosequences/xml/model/go/SlimSetXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/ArticleXML.java]: ../../../../ohnosequences/xml/model/uniprot/ArticleXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/CommentXML.java]: ../../../../ohnosequences/xml/model/uniprot/CommentXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/FeatureXML.java]: ../../../../ohnosequences/xml/model/uniprot/FeatureXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/InterproXML.java]: ../../../../ohnosequences/xml/model/uniprot/InterproXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/IsoformXML.java]: ../../../../ohnosequences/xml/model/uniprot/IsoformXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/KeywordXML.java]: ../../../../ohnosequences/xml/model/uniprot/KeywordXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/ProteinXML.java]: ../../../../ohnosequences/xml/model/uniprot/ProteinXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/SubcellularLocationXML.java]: ../../../../ohnosequences/xml/model/uniprot/SubcellularLocationXML.java.md
[main/java/com/ohnosequences/xml/model/util/Argument.java]: ../../../../ohnosequences/xml/model/util/Argument.java.md
[main/java/com/ohnosequences/xml/model/util/Arguments.java]: ../../../../ohnosequences/xml/model/util/Arguments.java.md
[main/java/com/ohnosequences/xml/model/util/Error.java]: ../../../../ohnosequences/xml/model/util/Error.java.md
[main/java/com/ohnosequences/xml/model/util/Execution.java]: ../../../../ohnosequences/xml/model/util/Execution.java.md
[main/java/com/ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java]: ../../../../ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java.md
[main/java/com/ohnosequences/xml/model/util/ScheduledExecutions.java]: ../../../../ohnosequences/xml/model/util/ScheduledExecutions.java.md
[main/java/com/ohnosequences/xml/model/util/XMLWrapperClass.java]: ../../../../ohnosequences/xml/model/util/XMLWrapperClass.java.md
[main/java/com/ohnosequences/xml/model/util/XMLWrapperClassCreator.java]: ../../../../ohnosequences/xml/model/util/XMLWrapperClassCreator.java.md