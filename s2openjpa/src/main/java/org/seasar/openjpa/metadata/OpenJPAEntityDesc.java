/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.seasar.framework.jpa.metadata.EntityDesc;


/**
 * OpenJPA用の{@link EntityDesc}実装です。
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityDesc implements EntityDesc {
    
    private ClassMetaData classMetaData;
    
    private OpenJPAAttributeDesc idAttributeDesc;
    
    private OpenJPAAttributeDesc[] attributeDescs;
    
    private String[] attributeNames;
    
    /** テーブルの配列 */
    protected final Table[] tables;
    
    private Map<String, OpenJPAAttributeDesc> attributeDescMap;
    
    /** 主テーブル名 */
    protected final String primaryTableName;

    /** 識別カラム */
    protected final String discriminatorColumnName;
    
    /** 識別値 */
    protected final String discriminatorValue;

    /** 識別カラムの{@link Types SQL型}が表す値 */
    protected final int discriminatorSqlType;

    /**
     * コンストラクタ
     * @param classMetaData 対象クラスのClassMetaDataオブジェクト
     * @param factory OpenJPAEntityManagerFactorySPIオブジェクト
     */
    public OpenJPAEntityDesc(ClassMetaData classMetaData,
            OpenJPAEntityManagerFactorySPI factory) {
        this.classMetaData = classMetaData;
        List<OpenJPAAttributeDesc> list = new ArrayList<OpenJPAAttributeDesc>();
        List<String> attributeNameList = new ArrayList<String>();
        Set<Table> tableSet = new LinkedHashSet<Table>();
        attributeDescMap = new HashMap<String, OpenJPAAttributeDesc>();
        for (FieldMetaData data : classMetaData.getFields()) {
            OpenJPAAttributeDesc desc = new OpenJPAAttributeDesc(data, factory);
            if (desc.isId()) {
                idAttributeDesc = desc;
            }
            list.add(desc);
            attributeNameList.add(data.getName());
            attributeDescMap.put(data.getName(), desc);
            setTable(tableSet, data);
            for (OpenJPAAttributeDesc childAttr : desc.getChildAttributeDescs()) {
                setTable(tableSet, childAttr.getFieldMetaData());
            }
        }
        attributeDescs = list.toArray(new OpenJPAAttributeDesc[list.size()]);
        attributeNames = attributeNameList.toArray(new String[attributeNameList.size()]);
        tables = tableSet.toArray(new Table[tableSet.size()]);
        if (classMetaData instanceof ClassMapping) {
            ClassMapping mapping = ClassMapping.class.cast(classMetaData);
            String columnName = null;
            String columnValue = null;
            Integer sqlType = null;
            Discriminator discriminator = mapping.getDiscriminator();
            if (discriminator != null) {
                Object obj = discriminator.getValue();
                if (obj != null) {
                    columnValue = String.valueOf(obj);
                }
                if (discriminator.getColumns().length >= 1) {
                    Column c = discriminator.getColumns()[0];
                    columnName = c.getName();
                    sqlType = c.getType();
                } else {
                    ClassMapping superMap = mapping.getPCSuperclassMapping();
                    if (superMap != null) {
                        Discriminator superDis = superMap.getDiscriminator();
                        if (superDis.getColumns().length >= 1) {
                            Column c = superDis.getColumns()[0];
                            columnName = c.getName();
                            sqlType = c.getType();
                        }
                    }
                }
            }
            primaryTableName = mapping.getTable().getName();
            discriminatorColumnName = columnName;
            discriminatorSqlType = sqlType != null ? sqlType : Types.OTHER;
            discriminatorValue = columnValue;
        } else {
            primaryTableName = null;
            discriminatorColumnName = null;
            discriminatorSqlType = Types.OTHER;
            discriminatorValue = null;
        }
    }

    private void setTable(Set<Table> tableSet, FieldMetaData data) {
        if (data instanceof FieldMapping) {
            FieldMapping mapping = FieldMapping.class.cast(data);
            for (Column c : mapping.getColumns()) {
                tableSet.add(c.getTable());
            }
        }
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDesc(java.lang.String)
     */
    public OpenJPAAttributeDesc getAttributeDesc(String attributeName) {
        return attributeDescMap.get(attributeName);
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDescs()
     */
    public OpenJPAAttributeDesc[] getAttributeDescs() {
        return attributeDescs;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeNames()
     */
    public String[] getAttributeNames() {
        return attributeNames;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityClass()
     */
    public Class<?> getEntityClass() {
        return classMetaData.getDescribedType();
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityName()
     */
    public String getEntityName() {
        return classMetaData.getTypeAlias();
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getIdAttributeDesc()
     */
    public OpenJPAAttributeDesc getIdAttributeDesc() {
        
        return idAttributeDesc;
    }

    
    /**
     * テーブル一覧を返します。
     * @return テーブル一覧
     */
    public Table[] getTables() {
        return tables;
    }
    
    /**
     * ClassMetaDataオブジェクトを返します。
     * @return ClassMetaDataオブジェクト
     */
    public ClassMetaData getClassMetaData() {
        return classMetaData;
    }

    /**
     * 主テーブルの名前を返します。
     * 
     * @return 主テーブルの名前
     */
    public String getPrimaryTableName() {
        return primaryTableName;
    }
    
    /**
     * 識別カラム名を持っている場合<code>true</code>を返します。
     * 
     * @return 識別カラム名を持っている場合<code>true</code>、そうでない場合<code>false</code>
     */
    public boolean hasDiscriminatorColumn() {
        return discriminatorColumnName != null;
    }

    /**
     * 識別カラム名を返します。
     * 
     * @return 識別カラム名
     */
    public String getDiscriminatorColumnName() {
        return discriminatorColumnName;
    }

    /**
     * 識別値を返します。
     * 
     * @return 識別値
     */
    public String getDiscriminatorValue() {
        return discriminatorValue;
    }

    /**
     * 識別カラムの{@link Types SQL型}が表す値を返します。
     * 
     * @return 識別カラムの{@link Types SQL型}が表す値
     */
    public int getDiscriminatorSqlType() {
        return discriminatorSqlType;
    }


}
