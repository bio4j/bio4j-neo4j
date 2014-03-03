/*
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
 */
package com.bio4j.neo4j.programs;

import com.bio4j.neo4j.model.nodes.TaxonNode;
import com.bio4j.neo4j.model.nodes.ProteinNode;
import com.bio4j.neo4j.model.nodes.KeywordNode;
import com.bio4j.neo4j.model.nodes.InterproNode;
import com.bio4j.neo4j.model.nodes.OrganismNode;
import com.bio4j.neo4j.model.nodes.GoTermNode;
import com.bio4j.neo4j.model.nodes.citation.ArticleNode;
import com.bio4j.neo4j.model.nodes.citation.SubmissionNode;
import com.bio4j.neo4j.model.relationships.TaxonParentRel;
import com.bio4j.neo4j.model.relationships.protein.ProteinGoRel;
import com.bio4j.neo4j.model.relationships.protein.ProteinInterproRel;
import com.bio4j.neo4j.model.relationships.protein.ProteinKeywordRel;
import com.bio4j.neo4j.model.relationships.uniref.UniRef90MemberRel;
import com.bio4j.neo4j.model.util.Bio4jManager;
import com.bio4j.neo4j.model.util.NodeRetriever;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Test program
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class GetProteinData {

    private static final Logger logger = Logger.getLogger("GetProteinData");
    private static FileHandler fh;

    public static void main(String[] args) throws IOException {
    	
    	if (args.length != 2) {
            System.out.println("This program expects the following parameters: \n"
                    + "1. Bio4j DB folder \n"
                    + "2. Protein accession");

        } else {
        	
        	// This block configures the logger with handler and formatter
            fh = new FileHandler("GetProteinData" + args[0].split("\\.")[0] + ".log", false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);

            System.out.println("creating manager...");           
            

            String accessionSt = args[1];
            Bio4jManager manager = new Bio4jManager(args[0]);           


            try {

      
            	NodeRetriever nodeRetriever = new NodeRetriever(manager);

            	ProteinNode protein = nodeRetriever.getProteinNodeByAccession(accessionSt);


                if (protein != null) {

                    System.out.println("protein.getNode().getId() = " + protein.getNode().getId());


                    System.out.println("gene names:");
                    for (String string : protein.getGeneNames()) {
                        System.out.println(string);
                    }

                    System.out.println("Getting Uniref 90 information...");
                    Iterator<Relationship> uniref90RepresentantIterator = protein.getNode().getRelationships(new UniRef90MemberRel(null), Direction.OUTGOING).iterator();
                    if (uniref90RepresentantIterator.hasNext()) {
                        System.out.println("The protein is representant of a Uniref 90 cluster");
                        System.out.println("the members of this cluster are:");
                        while (uniref90RepresentantIterator.hasNext()) {
                            ProteinNode tempProt = new ProteinNode(uniref90RepresentantIterator.next().getEndNode());
                            System.out.println(tempProt.getAccession());
                        }
                    } else {
                        Iterator<Relationship> uniref90memberIterator = protein.getNode().getRelationships(new UniRef90MemberRel(null), Direction.INCOMING).iterator();
                        if (uniref90memberIterator.hasNext()) {
                            System.out.println("The protein is member of the cluster: ");
                            ProteinNode tempProt = new ProteinNode(uniref90memberIterator.next().getStartNode());                        
                            System.out.println(tempProt.getAccession());
                        }
                    }


                    System.out.println("Getting keywords...");
                    Iterator<Relationship> relIt = protein.getNode().getRelationships(new ProteinKeywordRel(null), Direction.OUTGOING).iterator();
                    while (relIt.hasNext()) {
                        KeywordNode keyword = new KeywordNode(relIt.next().getEndNode());
                        System.out.println("keyword.getId() = " + keyword.getId());
                        System.out.println("keyword.getName() = " + keyword.getName());
                    }

                    System.out.println("Getting interpro...");
                    relIt = protein.getNode().getRelationships(new ProteinInterproRel(null), Direction.OUTGOING).iterator();
                    while (relIt.hasNext()) {
                        InterproNode interpro = new InterproNode(relIt.next().getEndNode());
                        System.out.println(interpro);
                    }


                    System.out.println("Getting go annotations...");
                    relIt = protein.getNode().getRelationships(new ProteinGoRel(null), Direction.OUTGOING).iterator();
                    while (relIt.hasNext()) {
                        ProteinGoRel goRel = new ProteinGoRel(relIt.next());
                        String evidence = goRel.getEvidence();
                        GoTermNode term = new GoTermNode(goRel.getRelationship().getEndNode());
                        System.out.println(term);
                        System.out.println("evidence = " + evidence);
                    }

                    OrganismNode organism = protein.getOrganism();
                    System.out.println("organism.getScientificName() = " + organism.getScientificName());
                    System.out.println("organism.getCommonName() = " + organism.getCommonName());
                    System.out.println("organism.getSynonymName() = " + organism.getSynonymName());
                    System.out.println("organism.getNcbiTaxonomyId() = " + organism.getNcbiTaxonomyId());

                    System.out.println("Protein dataset: " + protein.getDataset().getName());

                    System.out.println("Getting lineage...");
                    Node currentNode = organism.getNode();
                    Iterator<Relationship> iterator = null;
                    iterator = currentNode.getRelationships(new TaxonParentRel(null), Direction.INCOMING).iterator();
                    while (iterator.hasNext()) {
                        Relationship rel = iterator.next();
                        TaxonNode taxon = new TaxonNode(rel.getStartNode());
                        System.out.print(taxon.getName() + " --> ");
                        currentNode = taxon.getNode();
                        iterator = currentNode.getRelationships(new TaxonParentRel(null), Direction.INCOMING).iterator();
                    }

                    System.out.println("Getting citations...");

                    System.out.println("Article citations:");
                    for (ArticleNode article : protein.getArticleCitations()) {
                        System.out.println(article);
                    }
                    System.out.println("Submission citations:");
                    for (SubmissionNode submission : protein.getSubmissionCitations()) {
                        System.out.println(submission);
                    }

                    System.out.println("DONE!! :)");



                } else {
                    System.out.println("node is null.... :(");
                }


                //txn.success();

            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement stackTraceElement : trace) {
                    logger.log(Level.SEVERE, stackTraceElement.toString());
                }
            } finally {
                try {
                    //txn.finish();
                    manager.shutDown();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                    StackTraceElement[] trace = e.getStackTrace();
                    for (StackTraceElement stackTraceElement : trace) {
                        logger.log(Level.SEVERE, stackTraceElement.toString());
                    }
                }

            }
        	
        }

    }

}
