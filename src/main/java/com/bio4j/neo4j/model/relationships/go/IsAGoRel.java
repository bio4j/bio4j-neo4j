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

package com.bio4j.neo4j.model.relationships.go;

import com.bio4j.neo4j.BasicRelationship;
import org.neo4j.graphdb.Relationship;

/**
 *
 * GO term 'is_a' relationship
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class IsAGoRel extends BasicRelationship{

    public static final String NAME = "IS_A_GO";

    public static final String OBOXML_RELATIONSHIP_NAME = "is_a";

    public static final String IS_A_REL_INDEX = "is_a_rel_index";

    public IsAGoRel(Relationship rel){
        super(rel);
    }

    @Override
    public String name() {
        return NAME;
    }

}
