package com.bio4j.neo4jdb;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.GraphDatabaseService;

public abstract class Neo4jPropertyContainer implements PropertyContainer {

  protected PropertyContainer wrapped;

  public GraphDatabaseService getGraphDatabase() { return wrapped.getGraphDatabase(); }
  public boolean hasProperty( String key ) { return wrapped.hasProperty(key); }
  public Object getProperty( String key ){ return wrapped.getProperty(key); }
  public Object getProperty( String key, Object defaultValue ) { return wrapped.getProperty(key, defaultValue); }
  public void setProperty( String key, Object value ) { wrapped.setProperty(key, value); }
  public Object removeProperty( String key ) { return wrapped.removeProperty(key); }
  public Iterable<String> getPropertyKeys() { return wrapped.getPropertyKeys(); }
  @Deprecated
  public Iterable<Object> getPropertyValues() { return wrapped.getPropertyValues(); }
} 