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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityDesc implements EntityDesc {
    
    private ClassMetaData classMetaData;
    
    private OpenJPAAttributeDesc idAttributeDesc;
    
    private OpenJPAAttributeDesc[] attributeDescs;
    
    private String[] attributeNames;
    
    /** テーブル名の配列 */
    protected final String[] tableNames;
    
    private Map<String, OpenJPAAttributeDesc> attributeDescMap;
    
    public OpenJPAEntityDesc(ClassMetaData classMetaData,
            OpenJPAEntityManagerFactorySPI factory) {
        this.classMetaData = classMetaData;
        List<OpenJPAAttributeDesc> list = new ArrayList<OpenJPAAttributeDesc>();
        List<String> attributeNameList = new ArrayList<String>();
        Set<String> tableNameSet = new LinkedHashSet<String>();
        attributeDescMap = new HashMap<String, OpenJPAAttributeDesc>();
        for (FieldMetaData data : classMetaData.getFields()) {
            OpenJPAAttributeDesc desc = new OpenJPAAttributeDesc(data, factory);
            if (desc.isId()) {
                idAttributeDesc = desc;
            }
            list.add(desc);
            attributeNameList.add(data.getName());
            attributeDescMap.put(data.getName(), desc);
            setTable(tableNameSet, data);
            if (desc.getChildAttributeDescs() != null) {
                for (OpenJPAAttributeDesc childAttr : desc.getChildAttributeDescs()) {
                    setTable(tableNameSet, childAttr.getFieldMetaData());
                }
            }
        }
        attributeDescs = list.toArray(new OpenJPAAttributeDesc[list.size()]);
        attributeNames = attributeNameList.toArray(new String[attributeNameList.size()]);
        tableNames = tableNameSet.toArray(new String[tableNameSet.size()]);
    }

    private void setTable(Set<String> tableNameSet, FieldMetaData data) {
        if (data instanceof FieldMapping) {
            FieldMapping mapping = FieldMapping.class.cast(data);
            for (Column c : mapping.getColumns()) {
                tableNameSet.add(c.getTableName());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDesc(java.lang.String)
     */
    public OpenJPAAttributeDesc getAttributeDesc(String attributeName) {
        return attributeDescMap.get(attributeName);
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDescs()
     */
    public OpenJPAAttributeDesc[] getAttributeDescs() {
        return attributeDescs;
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeNames()
     */
    public String[] getAttributeNames() {
        return attributeNames;
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityClass()
     */
    public Class<?> getEntityClass() {
        return classMetaData.getDescribedType();
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityName()
     */
    public String getEntityName() {
        return classMetaData.getTypeAlias();
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getIdAttributeDesc()
     */
    public AttributeDesc getIdAttributeDesc() {
        
        return idAttributeDesc;
    }

    
    public String[] getTableNames() {
        return tableNames;
    }
    
    public ClassMetaData getClassMetaData() {
        return classMetaData;
    }

}
