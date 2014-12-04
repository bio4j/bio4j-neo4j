package com.bio4j.neo4jdb.model.nodes;

import com.bio4j.neo4jdb.Neo4jNode;
import com.bio4j.neo4jdb.TypedNeo4jNode;

import com.bio4j.neo4jdb.model.relationships.NewTaxonParentRel;
import com.bio4j.model.nodes.Taxon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Uniprot taxonomy taxon
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public final class NewTaxonNode extends Neo4jNode implements com.bio4j.model.nodes.Taxon {

  // node type
  public static final String NODE_TYPE = NewTaxonNode.class.getCanonicalName();

  // properties and their getters/setters
  public static final String NAME_PROPERTY = "taxon_name";
  public String getName(){ return String.valueOf(getProperty(NAME_PROPERTY)); }
  public void setName(String value){ setProperty(NAME_PROPERTY, value); }

  // indexes
  public static final String TAXON_NAME_INDEX = "taxon_name_index";


  public NewTaxonNode(Node n){ super(n, NODE_TYPE); }

  // methods
  public NewTaxonNode getParent() {
        
    NewTaxonNode parent = null;
    
    Iterator<Relationship> iterator = 
      getRelationships(NewTaxonParentRel.type, Direction.INCOMING).iterator();

    if(iterator.hasNext()){

      parent = new NewTaxonNode(iterator.next().getStartNode());
    }
    
    return parent;
  }
    
  /**
   * 
   * @return A list holding the taxon children of the current node
   */
  public List<NewTaxonNode> getChildren() {
      
    List<NewTaxonNode> list = new ArrayList<NewTaxonNode>();
    
    Iterator<Relationship> iterator = 
      getRelationships(NewTaxonParentRel.type, Direction.OUTGOING).iterator();
    
    while(iterator.hasNext()) {

      Node tempNode = iterator.next().getEndNode();

      if(tempNode.getProperty(TypedNeo4jNode.NODE_TYPE_PROPERTY).equals(NewTaxonNode.NODE_TYPE)) {

          list.add( new NewTaxonNode(tempNode) );
      }         
    }
    
    return list;
  }

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////// modify accordingly /////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////  
  /**
   * 
   * @return The organisms associated to the current node
   */
  public List<OrganismNode> getOrganisms(){

    List<OrganismNode> list = new ArrayList<OrganismNode>();
    
    Iterator<Relationship> iterator = getRelationships(NewTaxonParentRel.type, Direction.OUTGOING).iterator();
    
    while(iterator.hasNext()){
      Node tempNode = iterator.next().getEndNode();            
      if(tempNode.getProperty(com.bio4j.neo4jdb.BasicEntity.NODE_TYPE_PROPERTY).equals(OrganismNode.NODE_TYPE)){
          list.add(new OrganismNode(tempNode));
      }           
    }
    
    return list;
  }
  @Override
  public int hashCode(){
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if(obj instanceof NewTaxonNode){
      NewTaxonNode other = (NewTaxonNode) obj;
      return this.wrapped.equals(other.wrapped);
    } else {
      return false;
    }
  }

  @Override
  public String toString(){
    return "name = " + getName();
  }

}