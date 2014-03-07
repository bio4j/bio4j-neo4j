
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
package com.bio4j.neo4jdb.model.util;

import com.bio4j.neo4jdb.model.nodes.GoTermNode;
import com.bio4j.neo4jdb.model.nodes.ProteinNode;
import com.bio4j.neo4jdb.model.relationships.go.IsAGoRel;
import com.bio4j.neo4jdb.model.relationships.protein.ProteinGoRel;
import com.ohnosequences.xml.model.go.GOSlimXML;
import com.ohnosequences.xml.model.go.GoAnnotationXML;
import com.ohnosequences.xml.model.go.GoTermXML;
import com.ohnosequences.xml.model.go.SlimSetXML;
import com.ohnosequences.xml.model.uniprot.ProteinXML;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.jdom2.Element;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.RelationshipIndex;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GoUtil {

    private static final Logger logger = Logger.getLogger("GoUtil");

    static {
        try {
            FileHandler fh = new FileHandler("GoUtil.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException ex) {
            Logger.getLogger(GoUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(GoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static GoAnnotationXML getGoAnnotation(ArrayList<ProteinXML> proteins,
            Bio4jManager manager) {

        GoAnnotationXML annotationXML = new GoAnnotationXML();
        
        HashMap<String, GoTermXML> goAnnotatorsMap = new HashMap<String, GoTermXML>();
        HashMap<String, Integer> goCountsMap = new HashMap<String, Integer>();

//        Transaction txn = manager.beginTransaction();

        try {

            //IndexService indexService = manager.getIndexService();
            Index<Node> proteinAccessionIndex = manager.getProteinAccessionIndex();

            ProteinGoRel proteinGoRel = new ProteinGoRel(null);
            Iterator<Relationship> relIterator = null;

            for (ProteinXML proteinXML : proteins) {

                IndexHits<Node> protHits = proteinAccessionIndex.get(ProteinNode.PROTEIN_ACCESSION_INDEX, proteinXML.getId());
                if (protHits.hasNext()) {
                    ProteinNode proteinNode = new ProteinNode(protHits.getSingle());
                    relIterator = proteinNode.getNode().getRelationships(proteinGoRel, Direction.OUTGOING).iterator();
                    while (relIterator.hasNext()) {

                        proteinGoRel = new ProteinGoRel(relIterator.next());
                        GoTermNode goTermNode = new GoTermNode(proteinGoRel.getRelationship().getEndNode());
                        String goId = goTermNode.getId();

                        GoTermXML goXml = new GoTermXML();
                        goXml.setId(goId);
                        goXml.setAspect(goTermNode.getNamespace());
                        goXml.setGoName(goTermNode.getName());
                        goXml.setEvidence(proteinGoRel.getEvidence());
                        proteinXML.addGoTerm(goXml, true);

                        Integer goCount = goCountsMap.get(goId);
                        if (goCount == null) {
                            goCountsMap.put(goId, 1);
                            goAnnotatorsMap.put(goId, new GoTermXML((Element) goXml.asJDomElement().clone()));
                        } else {
                            goCountsMap.put(goId, (goCount + 1));
                        }
                    }

                    proteinXML.detach();

                    annotationXML.addProteinAnnotation(proteinXML);
                }

            }

            Set<String> keySet = goAnnotatorsMap.keySet();
            for (String currentKey : keySet) {
                GoTermXML tempGo = goAnnotatorsMap.get(currentKey);
                tempGo.setAnnotationsCount(goCountsMap.get(currentKey));
                annotationXML.addAnnotatorGoTerm(tempGo);
            }


//            txn.success();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
//            txn.failure();
            annotationXML = null;
        } finally {
//            txn.finish();
        }


        return annotationXML;
    }

    /**
     * 
     * @param proteins
     * @param slimSetXML
     * @param manager
     * @param goAnnotationXML
     * @return GO Slim in xml format
     */
    public static GOSlimXML getGoSlim(ArrayList<ProteinXML> proteins,
            SlimSetXML slimSetXML,
            Bio4jManager manager,
            GoAnnotationXML goAnnotationXML) {

        GOSlimXML goSlimXML = new GOSlimXML();

        if (goAnnotationXML == null) {
            goAnnotationXML = GoUtil.getGoAnnotation(proteins, manager);
        }

        //int goTermsLostNotIncludedInSlimSet = 0;

        Index<Node> goTermIdIndex = manager.getGoTermIdIndex();

        if (goAnnotationXML != null) {

            List<GoTermXML> goAnnotators = goAnnotationXML.getAnnotatorGoTerms();

            //IndexService indexService = manager.getIndexService();

            // in this hash map there is one entry for each annotator go term
            // the hash-set contains every slim-set go term including the annotator
            HashMap<String, HashSet<String>> goAnnotatorsIncludingSlimSetTermsMap = new HashMap<String, HashSet<String>>();


            //Here are the xml elements of the Go terms from the slim set termid --> term xml
            HashMap<String, GoTermXML> slimSetGos = new HashMap<String, GoTermXML>();

            //Here are the number of proteins annotated for each go term of the slim set termid --> number of proteins annotated
            HashMap<String, Integer> slimSetTermsAnnotationCounts = new HashMap<String, Integer>();

            //Now I extract the ids of the SlimSet
            List<Element> slimElements = slimSetXML.asJDomElement().getChildren(GoTermXML.TAG_NAME);
            for (Element slimElement : slimElements) {
                GoTermXML tempGo = new GoTermXML(slimElement);

                //--completing data of slimset go terms-----
                GoTermNode tempGoNode = new GoTermNode(manager.getGoTermIdIndex().get(GoTermNode.GO_TERM_ID_INDEX, tempGo.getId()).getSingle());
                tempGo.setAspect(tempGoNode.getNamespace());
                tempGo.setGoName(tempGoNode.getName());
                //------------------------

                slimSetGos.put(tempGo.getId(), tempGo);

                //initializing annotation counts map
                slimSetTermsAnnotationCounts.put(tempGo.getId(), 0);
            }
            //--------------------------------------------


//            logger.log(Level.INFO, "slimsetIds:");
//            for (String slimId : slimSetIds) {
//                logger.log(Level.INFO, ("slimId:" + slimId));
//            }


            //Now it is time for goAnnotatorsIncludingSlimSetTermsMap initialization
            for (GoTermXML goAnnotator : goAnnotators) {
                goAnnotatorsIncludingSlimSetTermsMap.put(goAnnotator.getId(), new HashSet<String>());
            }
            //------------------------------------------------------



            try {

                IsAGoRel goParentRel = new IsAGoRel(null);

                //Now I search the way up of every go Annotator and check if in the way I find
                //any of the terms included in the slim set.

                //logger.log(Level.INFO, "lalalala");

                int callCounter = 0;

                for (GoTermXML goAnnotator : goAnnotators) {
                    //this array includes the term own id and every ancestor id
                    HashSet<String> ancestorsIds = new HashSet<String>();

                    GoTermNode goTermNode = new GoTermNode(goTermIdIndex.get(GoTermNode.GO_TERM_ID_INDEX, goAnnotator.getId()).getSingle());

                    //logger.log(Level.INFO, goTermNode.toString());

                    fillUpAncestorIds(goTermNode, ancestorsIds, goParentRel, manager.getIsAGoRelIndex(), callCounter);

                    for (String ancestorId : ancestorsIds) {
                        //If the ancestor is included in the slim set, it means that this term
                        //from the slim set includes the goAnnotator 
                        if (slimSetGos.keySet().contains(ancestorId)) {
                            HashSet<String> hashSet = goAnnotatorsIncludingSlimSetTermsMap.get(goAnnotator.getId());
                            hashSet.add(ancestorId); //ancestorId is actually one of the slim-set terms ids.
                        }
                    }

                    callCounter++;
                }

                //So now I should have every goAnnotator with its corresponing slimSet terms

                List<Element> proteinList = goAnnotationXML.getProteinAnnotations().getChildren(ProteinXML.TAG_NAME);

                Element proteinsElem = new Element(GOSlimXML.PROTEINS_TAG_NAME);

                int sampleAnnotatedGeneNumber = 0;

                for (Element currentElem : proteinList) {

                    boolean annotated = false;

                    //getting the protein
                    ProteinXML currentProteinXML = new ProteinXML(currentElem);

                    //initializing inductors map                    
                    HashMap<String, String> currentProteinSlimTermInductors = new HashMap<String, String>();
                    //proteinSlimTermsAndInductorTermsMap.put(currentProteinXML.getId(), currentProteinSlimTermInductors);

                    //System.out.println("currentProteinXML.getId() = " + currentProteinXML.getId());

                    //--------now we access to its go annotations-------------
                    List<GoTermXML> proteinTerms = new ArrayList<GoTermXML>();
                    List<GoTermXML> bpTerms = currentProteinXML.getBiologicalProcessGoTerms();
                    List<GoTermXML> ccTerms = currentProteinXML.getCellularComponentGoTerms();
                    List<GoTermXML> mfTerms = currentProteinXML.getMolecularFunctionGoTerms();
                    if (bpTerms != null) {
                        proteinTerms.addAll(bpTerms);
                    }
                    if (ccTerms != null) {
                        proteinTerms.addAll(ccTerms);
                    }
                    if (mfTerms != null) {
                        proteinTerms.addAll(mfTerms);
                    }
                    //------------------------------------------------------
                    //creating the result xml protein
                    ProteinXML proteinResult = new ProteinXML();
                    proteinResult.setId(currentProteinXML.getId());
                    HashSet<String> proteinSlimTems = new HashSet<String>();
                    for (GoTermXML goTermXML : proteinTerms) {
                        HashSet<String> hashSet = goAnnotatorsIncludingSlimSetTermsMap.get(goTermXML.getId());

                        System.out.println("");

                        if (hashSet != null) {
                            if (hashSet.size() > 0) {
                                proteinSlimTems.addAll(hashSet);
                                for (String tempSlimTermId : hashSet) {
                                    currentProteinSlimTermInductors.put(tempSlimTermId, goTermXML.getId());
                                }
                                annotated = true;
                            } else {
                                //-----The go term annotation lost is stored-------
                                goSlimXML.addGoTermLostNotIncludedInSlimSet(new GoTermXML((Element) goTermXML.asJDomElement().clone()));
                                System.out.println("holaaa!" + goTermXML.getId());
                            }
                        }
                    }

                    //now we get the info from the slimset go terms
                    for (String string : proteinSlimTems) {
                        //logger.log(Level.INFO, ("string: " + string));
                        GoTermXML tempGoTerm = new GoTermXML((Element) slimSetGos.get(string).asJDomElement().clone());

                        //------Adding protein annotation term leading to slim set term -----------
                        String termInductorId = currentProteinSlimTermInductors.get(tempGoTerm.getId());
                        //look for inductor info                        
                        for (GoTermXML goTermXML : proteinTerms) {
                            //System.out.println("goTermXML = " + goTermXML);
                            if (goTermXML.getId().equals(termInductorId)) {
                                tempGoTerm.setProteinAnnotationLeadingToSlimTerm(new GoTermXML((Element) goTermXML.asJDomElement().clone()));
                                break;
                            }
                        }
                        //--------------------------------------------------------------------------

                        //logger.log(Level.INFO, ("tempGoTerm: " + tempGoTerm));
                        proteinResult.addGoTerm(tempGoTerm, true);

                        //updating annotation counts
                        slimSetTermsAnnotationCounts.put(string, slimSetTermsAnnotationCounts.get(string) + 1);
                    }

                    proteinsElem.addContent(proteinResult.asJDomElement());

                    if (annotated) {
                        sampleAnnotatedGeneNumber++;
                    }

                }

                //updating slimset annotation counts
                List<Element> slimSetElems = slimSetXML.asJDomElement().getChildren(GoTermXML.TAG_NAME);
                for (Element slimSetElem : slimSetElems) {
                    GoTermXML slimSetGo = new GoTermXML(slimSetElem);
                    slimSetGo.setAnnotationsCount(slimSetTermsAnnotationCounts.get(slimSetGo.getId()));
                }

                goSlimXML.asJDomElement().addContent(proteinsElem);
                slimSetXML.detach();
                goSlimXML.setSlimSet(slimSetXML);
                goSlimXML.setSampleGeneNumber(proteinList.size());
                goSlimXML.setSampleAnnotatedGeneNumber(sampleAnnotatedGeneNumber);
                //goSlimXML.set

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement stackTraceElement : trace) {
                    logger.log(Level.SEVERE, stackTraceElement.toString());
                }
                goSlimXML = null;
            }


        } else {
            goSlimXML = null;
        }


        return goSlimXML;
    }

    private static void fillUpAncestorIds(GoTermNode node,
            HashSet<String> ancestorsIds,
            IsAGoRel goParentRel,
            RelationshipIndex goParentRelIndex,
            int call) {

        ancestorsIds.add(node.getId());

        //logger.log(Level.INFO, ("fillUpAncestorIds (v2) --> " + node.getId() + " call: " + call));


        Iterator<Relationship> relIterator = goParentRelIndex.get(IsAGoRel.IS_A_REL_INDEX, String.valueOf(node.getNode().getId())).iterator();
        if (relIterator.hasNext()) {
            node = new GoTermNode(relIterator.next().getEndNode());

            fillUpAncestorIds(node, ancestorsIds, goParentRel, goParentRelIndex, call);
            while (relIterator.hasNext()) {
                node = new GoTermNode(relIterator.next().getEndNode());
                //logger.log(Level.INFO, ("double parent --> " + node.getId() + " call: " + call));
                fillUpAncestorIds(node, ancestorsIds, goParentRel, goParentRelIndex, call + 400000);
            }
        }


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
[main/java/com/bio4j/neo4j/model/nodes/AlternativeProductNode.java]: ../nodes/AlternativeProductNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ArticleNode.java]: ../nodes/citation/ArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/BookNode.java]: ../nodes/citation/BookNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/DBNode.java]: ../nodes/citation/DBNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/JournalNode.java]: ../nodes/citation/JournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineArticleNode.java]: ../nodes/citation/OnlineArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineJournalNode.java]: ../nodes/citation/OnlineJournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PatentNode.java]: ../nodes/citation/PatentNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PublisherNode.java]: ../nodes/citation/PublisherNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/SubmissionNode.java]: ../nodes/citation/SubmissionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ThesisNode.java]: ../nodes/citation/ThesisNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/UnpublishedObservationNode.java]: ../nodes/citation/UnpublishedObservationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CityNode.java]: ../nodes/CityNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CommentTypeNode.java]: ../nodes/CommentTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ConsortiumNode.java]: ../nodes/ConsortiumNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CountryNode.java]: ../nodes/CountryNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/DatasetNode.java]: ../nodes/DatasetNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/EnzymeNode.java]: ../nodes/EnzymeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/FeatureTypeNode.java]: ../nodes/FeatureTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/GoTermNode.java]: ../nodes/GoTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InstituteNode.java]: ../nodes/InstituteNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InterproNode.java]: ../nodes/InterproNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/IsoformNode.java]: ../nodes/IsoformNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/KeywordNode.java]: ../nodes/KeywordNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ncbi/NCBITaxonNode.java]: ../nodes/ncbi/NCBITaxonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/OrganismNode.java]: ../nodes/OrganismNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PersonNode.java]: ../nodes/PersonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PfamNode.java]: ../nodes/PfamNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ProteinNode.java]: ../nodes/ProteinNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/reactome/ReactomeTermNode.java]: ../nodes/reactome/ReactomeTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/CDSNode.java]: ../nodes/refseq/CDSNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GeneNode.java]: ../nodes/refseq/GeneNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GenomeElementNode.java]: ../nodes/refseq/GenomeElementNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MiscRNANode.java]: ../nodes/refseq/rna/MiscRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MRNANode.java]: ../nodes/refseq/rna/MRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/NcRNANode.java]: ../nodes/refseq/rna/NcRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RNANode.java]: ../nodes/refseq/rna/RNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RRNANode.java]: ../nodes/refseq/rna/RRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TmRNANode.java]: ../nodes/refseq/rna/TmRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TRNANode.java]: ../nodes/refseq/rna/TRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SequenceCautionNode.java]: ../nodes/SequenceCautionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SubcellularLocationNode.java]: ../nodes/SubcellularLocationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/TaxonNode.java]: ../nodes/TaxonNode.java.md
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
[main/java/com/bio4j/neo4j/model/util/Bio4jManager.java]: Bio4jManager.java.md
[main/java/com/bio4j/neo4j/model/util/GoUtil.java]: GoUtil.java.md
[main/java/com/bio4j/neo4j/model/util/NodeIndexer.java]: NodeIndexer.java.md
[main/java/com/bio4j/neo4j/model/util/NodeRetriever.java]: NodeRetriever.java.md
[main/java/com/bio4j/neo4j/model/util/UniprotStuff.java]: UniprotStuff.java.md
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