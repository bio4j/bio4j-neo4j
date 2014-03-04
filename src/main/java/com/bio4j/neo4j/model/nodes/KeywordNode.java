/*
 * Copyright (C) 2010-2011  "Bio4j"
 *
 * This file is part of Bio4j
 *
 * Bio4j is free software: you can redistribute it and/or modify
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

package com.bio4j.neo4j.model.nodes;

import com.bio4j.neo4j.model.relationships.protein.ProteinKeywordRel;
import com.bio4j.neo4j.BasicEntity;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Keyword
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class KeywordNode extends BasicEntity{

    public static final String KEYWORD_ID_INDEX = "keyword_id_index";
    public static final String KEYWORD_NAME_INDEX = "keyword_name_index";

    public static final String NODE_TYPE = KeywordNode.class.getCanonicalName();

    /** Keyword id **/
    public static final String ID_PROPERTY = "keyword_id";
    /** Keyword name **/
    public static final String NAME_PROPERTY = "keyword_name";


    public KeywordNode(Node n){
        super(n);
    }
    
    public List<ProteinNode> getAssociatedProteins(){
        List<ProteinNode> proteins = new LinkedList<ProteinNode>();
        
        Iterator<Relationship> iterator = node.getRelationships(new ProteinKeywordRel(null), Direction.INCOMING).iterator();
        while(iterator.hasNext()){
            ProteinNode protein = new ProteinNode(iterator.next().getStartNode());
            proteins.add(protein);                        
        }
        return proteins;  
    }


    public String getId(){  return String.valueOf(node.getProperty(ID_PROPERTY));}
    public String getName(){    return String.valueOf(node.getProperty(NAME_PROPERTY));}


    public void setId(String value){    node.setProperty(ID_PROPERTY, value);}
    public void setName(String value){  node.setProperty(NAME_PROPERTY, value);}


    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof KeywordNode){
            KeywordNode other = (KeywordNode) obj;
            return this.node.equals(other.node);
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return "id = " + getId() + "\n" +
                "name = " + getName();
    }

}
