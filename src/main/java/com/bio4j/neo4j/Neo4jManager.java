/*
 * Copyright (C) 2010-2012  "Oh no sequences!"
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.bio4j.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author ppareja
 */
public class Neo4jManager {

    protected static GraphDatabaseService graphService = null;

    public Neo4jManager(String dbFolder, boolean createServices, boolean readOnly, Map<String, String> config) {
        
        if (createServices) {
            if(!readOnly){
                if(config != null){                	                	
                    graphService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbFolder).setConfig(config).newGraphDatabase();
                }else{
                    graphService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbFolder).newGraphDatabase();
                }
                
            }else{
            	
                if(config != null){
                	config.put( "read_only", "true" );
                	graphService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbFolder).setConfig(config).newGraphDatabase();
                }else{
                	config = new HashMap<>();
                	config.put( "read_only", "true" );
                    graphService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbFolder).setConfig(config).newGraphDatabase();
                }                
            }            
        }        
    }
    
    public Node createNode() {
        return graphService.createNode();
    }

    public Transaction beginTransaction() {
        return graphService.beginTx();
    }
   

    public void shutDown() {
        graphService.shutdown();
    }

    public GraphDatabaseService getGraphService() {
        return graphService;
    }
}
