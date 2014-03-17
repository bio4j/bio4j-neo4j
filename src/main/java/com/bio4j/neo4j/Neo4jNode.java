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

























// import java.util.Set;

// /**
//  *
//  * @author Pablo Pareja Tobes <ppareja@era7.com>
//  * @author Eduardo Pareja-Tobes <eparejatobes@ohnosequences.com>
//  */
// public abstract class Neo4jNode implements org.neo4j.graphdb.Node, Node {
    
//     /*
//       The key used for storing the node type
//       TODO: migrate to neo4j labels
//     */
//     public static String NODE_TYPE_PROPERTY = "node_type";
    
//     /*
//       the wrapped Neo4j node
//     */
//     protected org.neo4j.graphdb.Node node;
//     private Neo4jNode(org.neo4j.graphdb.Node n){ node = n; }
    
//     public org.neo4j.graphdb.Node getNode(){ return node; }
    
//     @Override
//     public String getType(){
//       return String.valueOf(node.getProperty(NODE_TYPE_PROPERTY));
//     }    
    
//     protected void setType(String type) { node.setProperty(NODE_TYPE_PROPERTY, type); }
//     protected Neo4jNode(org.neo4j.graphdb.Node n, String type) { node = n; setType(type); }

//     // Vertex-specific

//     @Override
//     public Iterable<com.tinkerpop.blueprints.Vertex> getVertices(com.tinkerpop.blueprints.Direction direction, String... labels) {
//         return vertex.getVertices(direction, labels);
//     }

//     @Override
//     public Iterable<com.tinkerpop.blueprints.Edge> getEdges(com.tinkerpop.blueprints.Direction direction, String... labels) {
//         return vertex.getEdges(direction, labels);
//     }
    
//     @Override
//     public com.tinkerpop.blueprints.Edge addEdge(String label, com.tinkerpop.blueprints.Vertex inVertex) {
//         return vertex.addEdge(label, inVertex);
//     }

//     @Override
//     public com.tinkerpop.blueprints.VertexQuery query() {
//         return vertex.query();
//     }

//     // Element

//     @Override
//     public <T> T getProperty(String string) {
//         return vertex.getProperty(string);
//     }

//     @Override
//     public Set<String> getPropertyKeys() {
//         return vertex.getPropertyKeys();
//     }

//     @Override
//     public void setProperty(String string, Object o) {
//         vertex.setProperty(string, o);
//     }

//     @Override
//     public <T> T removeProperty(String string) {
//         return vertex.removeProperty(string);
//     }

//     @Override
//     public Object getId() {
//         return vertex.getId();
//     }
    
//     @Override
//     public void remove(){
//         vertex.remove();
//     }

// public long getId() { node.getId(); }
// public void delete() { node.delete(); }
// public Iterable<Relationship> getRelationships() { node.getRelationships(); }
// public boolean hasRelationship() { node.hasRelationship(); }
// public Iterable<Relationship> getRelationships( RelationshipType... types ) {
//     node.getRelationships(types);
// }
// public Iterable<Relationship> getRelationships( Direction direction, RelationshipType... types ) {
//     node.getRelationships(direction, types);
// }
// public boolean hasRelationship( RelationshipType... types ) {
//     node.hasRelationship(types)
// }
// public boolean hasRelationship( Direction direction, RelationshipType... types ) {
//     node.hasRelationship(Direction direction, RelationshipType... types);
// }
// public Iterable<Relationship> getRelationships( Direction dir );

// /**
//  * Returns <code>true</code> if there are any relationships in the given
//  * direction attached to this node, <code>false</code> otherwise. If
//  * {@link Direction#BOTH BOTH} is passed in as a direction, relationships of
//  * both directions are matched (effectively turning this into
//  * <code>hasRelationships()</code>).
//  *
//  * @param dir the given direction, where <code>Direction.OUTGOING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getStartNode() start node} and <code>
//  * Direction.INCOMING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getEndNode() end node}
//  * @return <code>true</code> if there are any relationships in the given
//  *         direction attached to this node, <code>false</code> otherwise
//  */
// public boolean hasRelationship( Direction dir );

// /**
//  * Returns all relationships with the given type and direction that are
//  * attached to this node. If there are no matching relationships, an empty
//  * iterable will be returned.
//  *
//  * @param type the given type
//  * @param dir the given direction, where <code>Direction.OUTGOING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getStartNode() start node} and <code>
//  * Direction.INCOMING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getEndNode() end node}
//  * @return all relationships attached to this node that match the given type
//  *         and direction
//  */
// public Iterable<Relationship> getRelationships( RelationshipType type,
//         Direction dir );

// /**
//  * Returns <code>true</code> if there are any relationships of the given
//  * relationship type and direction attached to this node, <code>false</code>
//  * otherwise.
//  *
//  * @param type the given type
//  * @param dir the given direction, where <code>Direction.OUTGOING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getStartNode() start node} and <code>
//  * Direction.INCOMING</code>
//  *            means all relationships that have this node as
//  *            {@link Relationship#getEndNode() end node}
//  * @return <code>true</code> if there are any relationships of the given
//  *         relationship type and direction attached to this node,
//  *         <code>false</code> otherwise
//  */
// public boolean hasRelationship( RelationshipType type, Direction dir );

// /**
//  * Returns the only relationship of a given type and direction that is
//  * attached to this node, or <code>null</code>. This is a convenience method
//  * that is used in the commonly occuring situation where a node has exactly
//  * zero or one relationships of a given type and direction to another node.
//  * Typically this invariant is maintained by the rest of the code: if at any
//  * time more than one such relationships exist, it is a fatal error that
//  * should generate an unchecked exception. This method reflects that
//  * semantics and returns either:
//  * <p>
//  * <ol>
//  * <li><code>null</code> if there are zero relationships of the given type
//  * and direction,</li>
//  * <li>the relationship if there's exactly one, or
//  * <li>throws an unchecked exception in all other cases.</li>
//  * </ol>
//  * <p>
//  * This method should be used only in situations with an invariant as
//  * described above. In those situations, a "state-checking" method (e.g.
//  * <code>hasSingleRelationship(...)</code>) is not required, because this
//  * method behaves correctly "out of the box."
//  *
//  * @param type the type of the wanted relationship
//  * @param dir the direction of the wanted relationship (where
//  *            <code>Direction.OUTGOING</code> means a relationship that has
//  *            this node as {@link Relationship#getStartNode() start node}
//  *            and <code>
//  * Direction.INCOMING</code> means a relationship that has
//  *            this node as {@link Relationship#getEndNode() end node}) or
//  *            {@link Direction#BOTH} if direction is irrelevant
//  * @return the single relationship matching the given type and direction if
//  *         exactly one such relationship exists, or <code>null</code> if
//  *         exactly zero such relationships exists
//  * @throws RuntimeException if more than one relationship matches the given
//  *             type and direction
//  */
// public Relationship getSingleRelationship( RelationshipType type,
//         Direction dir );

// /**
//  * Creates a relationship between this node and another node. The
//  * relationship is of type <code>type</code>. It starts at this node and
//  * ends at <code>otherNode</code>.
//  * <p>
//  * A relationship is equally well traversed in both directions so there's no
//  * need to create another relationship in the opposite direction (in regards
//  * to traversal or performance).
//  *
//  * @param otherNode the end node of the new relationship
//  * @param type the type of the new relationship
//  * @return the newly created relationship
//  */
// public Relationship createRelationshipTo( Node otherNode,
//         RelationshipType type );

// /* Expansion, tentatively added - save this for a later refactoring
// Expansion<Relationship> expandAll();

// Expansion<Relationship> expand( RelationshipType type );

// Expansion<Relationship> expand( RelationshipType type, Direction direction );

// Expansion<Relationship> expand( Direction direction );

// Expansion<Relationship> expand( RelationshipExpander expander );
// */

// // Traversal
// /**
//  * Instantiates a traverser that will start at this node and traverse
//  * according to the given order and evaluators along the specified
//  * relationship type and direction. If the traverser should traverse more
//  * than one <code>RelationshipType</code>/<code>Direction</code> pair, use
//  * one of the overloaded variants of this method. The created traverser will
//  * iterate over each node that can be reached from this node by the spanning
//  * tree formed by the given relationship types (with direction) exactly
//  * once. For more information about traversal, see the {@link Traverser}
//  * documentation.
//  * 
//  * @param traversalOrder the traversal order
//  * @param stopEvaluator an evaluator instructing the new traverser about
//  *            when to stop traversing, either a predefined evaluator such as
//  *            {@link StopEvaluator#END_OF_GRAPH} or a custom-written
//  *            evaluator
//  * @param returnableEvaluator an evaluator instructing the new traverser
//  *            about whether a specific node should be returned from the
//  *            traversal, either a predefined evaluator such as
//  *            {@link ReturnableEvaluator#ALL} or a customer-written
//  *            evaluator
//  * @param relationshipType the relationship type that the traverser will
//  *            traverse along
//  * @param direction the direction in which the relationships of type
//  *            <code>relationshipType</code> will be traversed
//  * @return a new traverser, configured as above
//  * @deprecated because of an unnatural and too tight coupling with
//  *             {@link Node}. Also because of the introduction of a new
//  *             traversal framework. The new way of doing traversals is by
//  *             creating a new {@link TraversalDescription} from
//  *             {@link Traversal#traversal()}, add rules and behaviors to it
//  *             and then calling
//  *             {@link TraversalDescription#traverse(Node...)}
//  */
// public Traverser traverse( Traverser.Order traversalOrder,
//         StopEvaluator stopEvaluator,
//         ReturnableEvaluator returnableEvaluator,
//         RelationshipType relationshipType, Direction direction );

// /**
//  * Instantiates a traverser that will start at this node and traverse
//  * according to the given order and evaluators along the two specified
//  * relationship type and direction pairs. If the traverser should traverse
//  * more than two <code>RelationshipType</code>/<code>Direction</code> pairs,
//  * use the overloaded
//  * {@link #traverse(Traverser.Order, StopEvaluator, ReturnableEvaluator, Object...)
//  * varargs variant} of this method. The created traverser will iterate over
//  * each node that can be reached from this node by the spanning tree formed
//  * by the given relationship types (with direction) exactly once. For more
//  * information about traversal, see the {@link Traverser} documentation.
//  * 
//  * @param traversalOrder the traversal order
//  * @param stopEvaluator an evaluator instructing the new traverser about
//  *            when to stop traversing, either a predefined evaluator such as
//  *            {@link StopEvaluator#END_OF_GRAPH} or a custom-written
//  *            evaluator
//  * @param returnableEvaluator an evaluator instructing the new traverser
//  *            about whether a specific node should be returned from the
//  *            traversal, either a predefined evaluator such as
//  *            {@link ReturnableEvaluator#ALL} or a customer-written
//  *            evaluator
//  * @param firstRelationshipType the first of the two relationship types that
//  *            the traverser will traverse along
//  * @param firstDirection the direction in which the first relationship type
//  *            will be traversed
//  * @param secondRelationshipType the second of the two relationship types
//  *            that the traverser will traverse along
//  * @param secondDirection the direction that the second relationship type
//  *            will be traversed
//  * @return a new traverser, configured as above
//  * @deprecated because of an unnatural and too tight coupling with
//  * {@link Node}. Also because of the introduction of a new traversal
//  * framework. The new way of doing traversals is by creating a
//  * new {@link TraversalDescription} from
//  * {@link Traversal#traversal()}, add rules and
//  * behaviours to it and then calling
//  * {@link TraversalDescription#traverse(Node...)}
//  */
// public Traverser traverse( Traverser.Order traversalOrder,
//         StopEvaluator stopEvaluator,
//         ReturnableEvaluator returnableEvaluator,
//         RelationshipType firstRelationshipType, Direction firstDirection,
//         RelationshipType secondRelationshipType, Direction secondDirection );

// /**
//  * Instantiates a traverser that will start at this node and traverse
//  * according to the given order and evaluators along the specified
//  * relationship type and direction pairs. Unlike the overloaded variants of
//  * this method, the relationship types and directions are passed in as a
//  * "varargs" variable-length argument which means that an arbitrary set of
//  * relationship type and direction pairs can be specified. The
//  * variable-length argument list should be every other relationship type and
//  * direction, starting with relationship type, e.g:
//  * <p>
//  * <code>node.traverse( BREADTH_FIRST, stopEval, returnableEval,
//  * MyRels.REL1, Direction.OUTGOING, MyRels.REL2, Direction.OUTGOING,
//  * MyRels.REL3, Direction.BOTH, MyRels.REL4, Direction.INCOMING );</code>
//  * <p>
//  * Unfortunately, the compiler cannot enforce this so an unchecked exception
//  * is raised if the variable-length argument has a different constitution.
//  * <p>
//  * The created traverser will iterate over each node that can be reached
//  * from this node by the spanning tree formed by the given relationship
//  * types (with direction) exactly once. For more information about
//  * traversal, see the {@link Traverser} documentation.
//  * 
//  * @param traversalOrder the traversal order
//  * @param stopEvaluator an evaluator instructing the new traverser about
//  *            when to stop traversing, either a predefined evaluator such as
//  *            {@link StopEvaluator#END_OF_GRAPH} or a custom-written
//  *            evaluator
//  * @param returnableEvaluator an evaluator instructing the new traverser
//  *            about whether a specific node should be returned from the
//  *            traversal, either a predefined evaluator such as
//  *            {@link ReturnableEvaluator#ALL} or a customer-written
//  *            evaluator
//  * @param relationshipTypesAndDirections a variable-length list of
//  *            relationship types and their directions, where the first
//  *            argument is a relationship type, the second argument the first
//  *            type's direction, the third a relationship type, the fourth
//  *            its direction, etc
//  * @return a new traverser, configured as above
//  * @throws RuntimeException if the variable-length relationship type /
//  *             direction list is not as described above
//  * @deprecated because of an unnatural and too tight coupling with
//  * {@link Node}. Also because of the introduction of a new traversal
//  * framework. The new way of doing traversals is by creating a
//  * new {@link TraversalDescription} from
//  * {@link Traversal#traversal()}, add rules and
//  * behaviours to it and then calling
//  * {@link TraversalDescription#traverse(Node...)}
//  */
// public Traverser traverse( Traverser.Order traversalOrder,
//         StopEvaluator stopEvaluator,
//         ReturnableEvaluator returnableEvaluator,
//         Object... relationshipTypesAndDirections );
// }
