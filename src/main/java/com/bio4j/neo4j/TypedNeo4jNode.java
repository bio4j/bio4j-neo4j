package com.bio4j.neo4jdb;

public interface TypedNeo4jNode extends org.neo4j.graphdb.Node, com.bio4j.model.Node {
  
  public static final String NODE_TYPE_PROPERTY = "nodeType";
  // public String getNodeType();

}