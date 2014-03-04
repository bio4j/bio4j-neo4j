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

/**
 *
 * @author ppareja
 */
public abstract class BasicEntity {

    public static final String NODE_TYPE_PROPERTY = "nodeType";


    protected Node node = null;


    public BasicEntity(Node n){
        node = n;
    }

    @Override
    public int hashCode(){
        return node.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicEntity other = (BasicEntity) obj;
        if (this.node != other.node && (this.node == null || !this.node.equals(other.node))) {
            return false;
        }
        return true;
    }

    public Node getNode(){
        return node;
    }

    public String getNodeType(){
        return String.valueOf(node.getProperty(NODE_TYPE_PROPERTY));
    }
    
    public void setNodeType(String value){
        node.setProperty(NODE_TYPE_PROPERTY, value);
    }

}
