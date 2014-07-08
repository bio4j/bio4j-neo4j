package com.bio4j.neo4j.model.go;

import com.bio4j.model.go.GoGraph;
import com.ohnosequences.typedGraphs.neo4j.Neo4jTypedGraph;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created by ppareja on 7/3/2014.
 */
public class Neo4jGoGraph implements
		Neo4jTypedGraph,
		GoGraph{

	protected GraphDatabaseService rawGraph;

	Neo4jGoGraph(GraphDatabaseService rawGraph) {

		this.rawGraph = rawGraph;
	}

	@Override
	public GraphDatabaseService rawGraph() {
		return rawGraph;
	}


}
