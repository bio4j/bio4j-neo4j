package com.bio4j.neo4j.model.go.nodes;

import com.bio4j.model.go.GoGraph;
import com.bio4j.model.go.nodes.GoTerm;
import com.bio4j.neo4j.model.go.Neo4jGoGraph;
import com.ohnosequences.typedGraphs.neo4j.Neo4jNode;

import java.util.List;

/**
 * Created by ppareja on 7/7/2014.
 */
public class Neo4jGoTerm extends
		Neo4jNode<Neo4jGoTerm, Neo4jGoTerm.Neo4jGoTermType>
		implements GoTerm<Neo4jGoTerm, Neo4jGoTerm.Neo4jGoTermType>

{

//	@Override
//	public String id(){ return get(goGraph.goTermT.id);}
//	@Override
//	public String name(){   return get(goGraph.goTermT.name);}
//	@Override
//	public String comment(){   return get(goGraph.goTermT.comment);}
//	@Override
//	public String synonym(){   return get(goGraph.goTermT.synonym);}
//	@Override
//	public String definition(){   return get(goGraph.goTermT.definition);}
//	@Override
//	public String obsolete(){   return get(goGraph.goTermT.obsolete);}

	public Neo4jGoTerm(Neo4jNode vertex, Neo4jGoGraph goGraph) {

		super(vertex);
		this.goGraph = goGraph;
	}

	Neo4jGoGraph goGraph;

	@Override
	public Neo4jGoTermType type() {

		return goGraph.goTermT;
	}


	public static final class Neo4jGoTermType
			implements
			Neo4jNode.Type<Neo4jGoTerm, Neo4jGoTerm.Neo4jGoTermType>,
			GoGraph.TermType<Neo4jGoTerm, Neo4jGoTermType> {

		public Neo4jGoTermType(Neo4jGoGraph goGraph) {
			this.goGraph = goGraph;
		}

		Neo4jGoGraph goGraph;



}

