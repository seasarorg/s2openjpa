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

import java.util.Map;

import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.FieldMetaData;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.util.tiger.CollectionsUtil;
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
    
    protected OpenJPAEntityReader() {
    }

    /**
     * @param entityDesc
     */
    public OpenJPAEntityReader(Object entity, OpenJPAEntityDesc entityDesc) {
        this.entityDesc = entityDesc;
        setupColumns();
        setupRow(entity);
    }
    
    /**
     * カラムを設定します。
     */
    protected void setupColumns() {
        for (String tableName : getEntityDesc().getTableNames()) {
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
        
        for (OpenJPAAttributeDesc attribute : getEntityDesc().getAttributeDescs()) {
            setColumn(attribute);
            for (OpenJPAAttributeDesc child : attribute.getChildAttributeDescs()) {
                setColumn(child);
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
        if (!getEntityDesc().hasDiscriminatorColumn()) {
            return;
        }
        final String tableName = getEntityDesc().getPrimaryTableName();
        final String columnName = getEntityDesc().getDiscriminatorColumnName();
        final DataTable table = dataSet.getTable(tableName);
        if (!table.hasColumn(columnName)) {
            final int sqlType = getEntityDesc().getDiscriminatorSqlType();
            table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
        }
    }
    
    /**
     * 行を設定します。
     * 
     * @param entity
     *            エンティティ
     */
    protected void setupRow(final Object entity) {
        Map<String, DataRow> rowMap = CollectionsUtil.newHashMap();
        for (OpenJPAAttributeDesc attribute : getEntityDesc().getAttributeDescs()) {
            if (attribute.isComponent()) {
                for (OpenJPAAttributeDesc childDesc : attribute.getChildAttributeDescs()) {
                    setRow(entity, rowMap, childDesc);
                }
            } else {
                setRow(entity, rowMap, attribute);
            }
        }
        if (getEntityDesc().hasDiscriminatorColumn()) {
            DataTable table = dataSet.getTable(getEntityDesc().getPrimaryTableName());
            DataRow row = rowMap.get(table.getTableName());
            if (row == null) {
                row = table.addRow();
                rowMap.put(table.getTableName(), row);
            }
            row.setValue(getEntityDesc().getDiscriminatorColumnName(), getEntityDesc().getDiscriminatorValue());
        }
        for (String key : rowMap.keySet()) {
            rowMap.get(key).setState(RowStates.UNCHANGED);
        }
    }

    protected void setRow(final Object entity, Map<String, DataRow> rowMap,
            OpenJPAAttributeDesc attribute) {
        FieldMetaData meta = attribute.getFieldMetaData();
        if (meta instanceof FieldMapping) {
            FieldMapping mapping = FieldMapping.class.cast(meta);
            for (Column c : mapping.getColumns()) {
                DataTable table = dataSet.getTable(c.getTableName());
                DataRow row = rowMap.get(table.getTableName());
                if (row == null) {
                    row = table.addRow();
                    rowMap.put(table.getTableName(), row);
                }
                Object value = attribute.getValue(entity);
                row.setValue(c.getName(), value);
                
            }
        }
    }
    
    protected OpenJPAEntityDesc getEntityDesc() {
        return entityDesc;
    }


    /* (non-Javadoc)
     * @see org.seasar.extension.dataset.DataReader#read()
     */
    public DataSet read() {
        return dataSet;
    }

}
