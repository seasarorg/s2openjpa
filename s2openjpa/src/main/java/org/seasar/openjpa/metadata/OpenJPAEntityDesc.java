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
import java.util.List;
import java.util.Map;

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
    
    private Map<String, OpenJPAAttributeDesc> attributeDescMap;
    
    public OpenJPAEntityDesc(ClassMetaData classMetaData,
            OpenJPAEntityManagerFactorySPI factory) {
        this.classMetaData = classMetaData;
        List<OpenJPAAttributeDesc> list = new ArrayList<OpenJPAAttributeDesc>();
        List<String> attributeNameList = new ArrayList<String>();
        attributeDescMap = new HashMap<String, OpenJPAAttributeDesc>();
        for (FieldMetaData data : classMetaData.getFields()) {
            OpenJPAAttributeDesc desc = new OpenJPAAttributeDesc(data, factory);
            if (desc.isId()) {
                idAttributeDesc = desc;
            }
            list.add(desc);
            attributeNameList.add(data.getName());
            attributeDescMap.put(data.getName(), desc);
        }
        attributeDescs = list.toArray(new OpenJPAAttributeDesc[list.size()]);
        attributeNames = attributeNameList.toArray(new String[attributeNameList.size()]);
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDesc(java.lang.String)
     */
    public AttributeDesc getAttributeDesc(String attributeName) {
        return attributeDescMap.get(attributeName);
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDescs()
     */
    public AttributeDesc[] getAttributeDescs() {
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

}
