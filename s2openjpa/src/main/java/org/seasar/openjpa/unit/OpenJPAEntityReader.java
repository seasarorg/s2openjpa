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
package org.seasar.openjpa.unit;

import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.FieldMetaData;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.openjpa.metadata.OpenJPAAttributeDesc;
import org.seasar.openjpa.metadata.OpenJPAEntityDesc;


/**
 * @author hidenoshin
 *
 */
public class OpenJPAEntityReader implements EntityReader {
    
    private OpenJPAEntityDesc entityDesc;
    
    /** データセット */
    protected final DataSet dataSet = new DataSetImpl();

    /**
     * @param entityDesc
     */
    public OpenJPAEntityReader(Object entity, OpenJPAEntityDesc entityDesc) {
        this.entityDesc = entityDesc;
        setupColumns();
    }
    
    /**
     * カラムを設定します。
     */
    protected void setupColumns() {
        for (String tableName : entityDesc.getTableNames()) {
            if (!dataSet.hasTable(tableName)) {
                dataSet.addTable(tableName);
            }
        }
        setupAttributeColumns();
        setupDiscriminatorColumn();
    }

    /**
     * entityDescからdataSetのカラム定義を生成します。
     */
    protected void setupAttributeColumns() {
        
        for (OpenJPAAttributeDesc attribute : entityDesc.getAttributeDescs()) {
            setColumn(attribute);
            if (attribute.getChildAttributeDescs() != null) {
                for (OpenJPAAttributeDesc child : attribute.getChildAttributeDescs()) {
                    setColumn(child);
                }
            }
        }
    }

    protected void setColumn(OpenJPAAttributeDesc attribute) {
        FieldMetaData field = attribute.getFieldMetaData();
        if (field instanceof FieldMapping) {
            FieldMapping mapping = FieldMapping.class.cast(field);
            for (Column c : mapping.getColumns()) {
                DataTable table = dataSet.getTable(c.getTableName());
                int sqlType = c.getType();
                String columnName = c.getName();
                if (!table.hasColumn(columnName)) {
                    table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
                }
            }
        }
    }

    protected void setupDiscriminatorColumn() {
//        if (!entityDesc.hasDiscriminatorColumn()) {
//            return;
//        }
//        InheritancePolicy inheritancePolicy = getEntityDesc().getInheritancePolicy();
//        DatabaseField field = inheritancePolicy.getClassIndicatorField();
//        DataTable table = dataSet.getTable(field.getTableName());
//        
//        ServerSession serverSession = getEntityDesc().getServerSession();
//        DatabasePlatform platform = serverSession.getPlatform();
//        
//        int sqlType = platform.getJDBCType(field);
//        String columnName = field.getName();
//        if (!table.hasColumn(columnName)) {
//            table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
//        }
    }


    /* (non-Javadoc)
     * @see org.seasar.extension.dataset.DataReader#read()
     */
    public DataSet read() {
        return dataSet;
    }

}
