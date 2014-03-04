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

package com.era7.bioinfo.bioinfoneo4j;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author ppareja
 */
public abstract class BasicRelationship implements RelationshipType{
    
    protected Relationship relationship = null;

    public BasicRelationship(Relationship rel){
        relationship = rel;
    }

    @Override
    public int hashCode(){
        return relationship.hashCode();
    }

    public Node getEndNode(){
        return relationship.getEndNode();
    }
    public Node getStartNode(){
        return relationship.getStartNode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicRelationship other = (BasicRelationship) obj;
        if (this.relationship != other.relationship && (this.relationship == null || !this.relationship.equals(other.relationship))) {
            return false;
        }
        return true;
    }
    
    public Relationship getRelationship(){
        return relationship;
    }

}
