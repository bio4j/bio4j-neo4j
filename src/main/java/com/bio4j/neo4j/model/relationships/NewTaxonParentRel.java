package com.bio4j.neo4jdb.model.relationships;

import com.bio4j.neo4jdb.Neo4jRelationship;
import org.neo4j.graphdb.Relationship;
import com.bio4j.neo4jdb.Bio4jRelationshipType;

import com.bio4j.model.relationships.TaxonParent;

/**
 * 
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class NewTaxonParentRel extends Neo4jRelationship implements TaxonParent {

    public static Bio4jRelationshipType type = Bio4jRelationshipType.NewTaxonParentRel;
    // public static final String NAME = "TAXON_PARENT";

    public NewTaxonParentRel(Relationship rel){
        super(rel);
    }

}
