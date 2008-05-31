/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.openjpa.metadata;

import java.util.Iterator;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.jdbc.meta.DiscriminatorMappingInfo;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.MappingInfo;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.openjpa.entity.Enemy;
import org.seasar.openjpa.entity.JoindSampleChild;
import org.seasar.openjpa.entity.JoindSampleChild2;


/**
 * @author hidenoshin
 *
 */
public class TestTest extends S2TestCase {
    
    public void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }
    
    public void testTest() {
        
        OpenJPAEntityDesc desc = OpenJPAEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Enemy.class));
        ClassMetaData meta = desc.getClassMetaData();
        ClassMapping map = ClassMapping.class.cast(meta);
        Discriminator d = map.getDiscriminator();
        DiscriminatorMappingInfo m = d.getMappingInfo();
        for (Iterator<?> it = m.getColumns().iterator(); it.hasNext();) {
            Column c = (Column) it.next();
            System.out.println(c.getName());
            System.out.println(c.getTableName());
        }
        System.out.println(d.getValue());
        System.out.println();
        
        for (OpenJPAAttributeDesc attr : desc.getAttributeDescs()) {
            attribute(attr);
            if (attr.getChildAttributeDescs() != null) {
                for (OpenJPAAttributeDesc child : attr.getChildAttributeDescs()) {
                    attribute(child);
                }                
            }
        }
    }

    private void attribute(OpenJPAAttributeDesc attr) {
        FieldMetaData meta = attr.getFieldMetaData();
        FieldMapping mapping = FieldMapping.class.cast(meta);
        for (Column c : mapping.getColumns()) {
            System.out.println(c.getName());
            System.out.println(c.getTableName());
            System.out.println(ColumnTypes.getColumnType(c.getType()));
        }
    }

}
