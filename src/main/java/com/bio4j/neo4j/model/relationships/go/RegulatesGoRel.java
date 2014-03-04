/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bio4j.neo4j.model.relationships.go;

import com.bio4j.neo4j.BasicRelationship;
import org.neo4j.graphdb.Relationship;

/**
 *
 * GO term 'regulates' relationship
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class RegulatesGoRel extends BasicRelationship{

    public static final String NAME = "REGULATES_GO";

    public static final String OBOXML_RELATIONSHIP_NAME = "regulates";

    public RegulatesGoRel(Relationship rel){
        super(rel);
    }

    @Override
    public String name() {
        return NAME;
    }

}
