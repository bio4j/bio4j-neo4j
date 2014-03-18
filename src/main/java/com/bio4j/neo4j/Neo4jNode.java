package com.bio4j.neo4jdb;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

public class Neo4jNode extends Neo4jPropertyContainer implements TypedNeo4jNode, com.bio4j.model.Node {

  /*
    The wrapped Neo4j node
  */
  protected final Node wrapped;
  public Node getNode(){ return wrapped; }

  protected Neo4jNode(Node n, String type) { wrapped = n; this.setType(type); }

  private void setType(String type) { this.setProperty(this.NODE_TYPE_PROPERTY, type); }
  @Override
  public String getType() { return String.valueOf(getProperty(NODE_TYPE_PROPERTY)); }

  public long getId() { 
    return wrapped.getId();
  }
  public void delete() { 
    wrapped.delete();  
  }
  public Iterable<Relationship> getRelationships() { 
    return wrapped.getRelationships(); 
  }
  public boolean hasRelationship() { 
    return wrapped.hasRelationship(); 
  }
  public Iterable<Relationship> getRelationships( RelationshipType... types ) { 
    return wrapped.getRelationships(types); 
  }
  public Iterable<Relationship> getRelationships( Direction direction, RelationshipType... types ) { 
    return wrapped.getRelationships(direction, types); 
  }
  public boolean hasRelationship( RelationshipType... types ) { 
    return wrapped.hasRelationship(types); 
  }
  public boolean hasRelationship( Direction direction, RelationshipType... types ) { 
    return wrapped.hasRelationship(direction, types); 
  }
  public Iterable<Relationship> getRelationships( Direction dir ) { 
    return wrapped.getRelationships(dir); 
  }
  public boolean hasRelationship( Direction dir ) { 
    return wrapped.hasRelationship(dir); 
  }
  public Iterable<Relationship> getRelationships( RelationshipType type, Direction dir ) {
    return wrapped.getRelationships(type, dir);
  }
  public boolean hasRelationship( RelationshipType type, Direction dir ) {
    return wrapped.hasRelationship(type, dir);
  }
  public Relationship getSingleRelationship( RelationshipType type, Direction dir ) {
    return wrapped.getSingleRelationship(type, dir);
  }
  public Relationship createRelationshipTo( Node otherNode, RelationshipType type ) {
    return wrapped.createRelationshipTo(otherNode, type);
  }
  @Deprecated
  public Traverser traverse( 
    Traverser.Order traversalOrder,
    StopEvaluator stopEvaluator,
    ReturnableEvaluator returnableEvaluator,
    RelationshipType relationshipType, Direction direction 
  ) {
    return wrapped.traverse(traversalOrder, stopEvaluator, returnableEvaluator, relationshipType, direction);
  }
  @Deprecated
  public Traverser traverse( 
    Traverser.Order traversalOrder,
    StopEvaluator stopEvaluator,
    ReturnableEvaluator returnableEvaluator,
    RelationshipType firstRelationshipType, Direction firstDirection,
    RelationshipType secondRelationshipType, Direction secondDirection 
  ) {
    return wrapped.traverse(
      traversalOrder, 
      stopEvaluator, 
      returnableEvaluator,
      firstRelationshipType,
      firstDirection,
      secondRelationshipType,
      secondDirection
    );
  }
  @Deprecated   
  public Traverser traverse( 
    Traverser.Order traversalOrder,
    StopEvaluator stopEvaluator,
    ReturnableEvaluator returnableEvaluator,
    Object... relationshipTypesAndDirections 
  ) {
    return wrapped.traverse(
      traversalOrder, 
      stopEvaluator, 
      returnableEvaluator, 
      relationshipTypesAndDirections
    );
  }
}