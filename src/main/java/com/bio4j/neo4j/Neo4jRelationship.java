package com.bio4j.neo4jdb;

import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Node;

/*
*
* @author Eduardo Pareja-Tobes <@eparejatobes@ohnosequences.com>
*/
public abstract class Neo4jRelationship extends Neo4jPropertyContainer implements 
  com.bio4j.model.Relationship, org.neo4j.graphdb.Relationship {

  /*
    The relationship type should be accessible from the class
  */
  public static Bio4jRelationshipType type;

  protected final Relationship wrapped;

  protected Neo4jRelationship(Relationship r) { wrapped = r; }

  // Neo4j rel
  public long getId() { return wrapped.getId(); }
  public void delete() { wrapped.delete(); }
  public Node getStartNode() { return wrapped.getStartNode(); }
  public Node getEndNode() { return wrapped.getEndNode(); }
  public Node getOtherNode( Node node )  { return wrapped.getOtherNode(node); } 
  public Node[] getNodes() { return wrapped.getNodes(); }
  public RelationshipType getType() { return wrapped.getType(); }
  public boolean isType( RelationshipType type ) { return wrapped.isType(type); }

  // Bio4j model rel
  public String type() { return wrapped.getType().toString(); }
}