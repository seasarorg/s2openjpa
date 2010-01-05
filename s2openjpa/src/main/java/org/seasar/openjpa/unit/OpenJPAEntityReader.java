/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.strats.RelationFieldStrategy;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Table;
import org.apache.openjpa.meta.FieldMetaData;
import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.BigDecimalType;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.openjpa.metadata.OpenJPAAttributeDesc;
import org.seasar.openjpa.metadata.OpenJPAEntityDesc;
import org.seasar.openjpa.util.OpenJpaUtil;


/**
 * OpenJPA用の{@link EntityReader}実装です。
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityReader implements EntityReader {
    
    private OpenJPAEntityDesc entityDesc;
    
    /** データセット */
    protected final DataSet dataSet = new DataSetImpl();
    
    /**
     * コンストラクタ
     */
    protected OpenJPAEntityReader() {
    }

    /**
     * コンストラクタ
     * @param entity 対象となるEntityオブジェクト
     * @param entityDesc entityに対応するOpenJPAEntityDescオブジェクト
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
        for (Table table : getEntityDesc().getTables()) {
            if (!dataSet.hasTable(table.getName())) {
                dataSet.addTable(table.getName());
            }
        }
        setupAttributeColumns();
    }

    /**
     * entityDescからdataSetのカラム定義を生成します。
     */
    protected void setupAttributeColumns() {
        
        for (Table table : getEntityDesc().getTables()) {
            DataTable dataTable = dataSet.getTable(table.getName());
            for (Column c : table.getColumns()) {
                int sqlType = c.getType();
                String columnName = c.getName();
                if (!dataTable.hasColumn(columnName)) {
                    dataTable.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
                }
            }
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
                    setRow(attribute.getValue(entity), rowMap, childDesc.getFieldMetaData());
                }
            } else if (attribute.isAssociation()) {
                FieldMapping fm = (FieldMapping) attribute.getFieldMetaData();
                if (fm.getStrategy() instanceof RelationFieldStrategy) {
                    RelationFieldStrategy rfs = (RelationFieldStrategy) fm.getStrategy();
                    Object asso = attribute.getValue(entity);
                    OpenJPAEntityReader reader = (OpenJPAEntityReader) EntityReaderFactory.getEntityReader(asso);
                    OpenJPAAttributeDesc idDesc = reader.getEntityDesc().getIdAttributeDesc();
                    if (idDesc.isComponent()) {
                        for (OpenJPAAttributeDesc idChild : idDesc.getChildAttributeDescs()) {
                            for (Column c : rfs.getColumns()) {
                                setData(idDesc.getValue(asso), rowMap, idChild.getFieldMetaData(), c);
                            }
                        }
                    } else {
                        for (Column c : rfs.getColumns()) {
                            setData(asso, rowMap, idDesc.getFieldMetaData(), c);                            
                        }
                    }
                }
            } else {
                setRow(entity, rowMap, attribute.getFieldMetaData());
            }
        }
        DataTable table = dataSet.getTable(getEntityDesc().getPrimaryTableName());
        DataRow row = rowMap.get(table.getTableName());
        if (row == null) {
            row = table.addRow();
            rowMap.put(table.getTableName(), row);
        }
        if (getEntityDesc().hasDiscriminatorColumn()) {
            row.setValue(getEntityDesc().getDiscriminatorColumnName(), getEntityDesc().getDiscriminatorValue());
        }
        ClassMapping cm = (ClassMapping) getEntityDesc().getClassMetaData();
        for (FieldMapping fMapping : cm.getPrimaryKeyFieldMappings()) {
            if (fMapping.getEmbeddedMetaData() != null) {
                ClassMapping embCm = fMapping.getEmbeddedMapping();
                Object embedded = OpenJpaUtil.getValue(fMapping, entity);
                for (FieldMapping fm2 : embCm.getFieldMappings()) {                    
                    setIdRow(embedded, table, row, fm2);
                }
            } else {
                setIdRow(entity, table, row, fMapping);
            }
        }
        for (String key : rowMap.keySet()) {
            rowMap.get(key).setState(RowStates.UNCHANGED);
        }
    }

    /**
     * IDデータを取得して対象DataRowにセットします。
     * @param entity 対象Entityオブジェクト
     * @param table 対象DataTableオブジェクト
     * @param row 対象DataRowオブジェクト
     * @param fMapping 対象カラム情報を保持するFieldMappingオブジェクト
     */
    protected void setIdRow(final Object entity, DataTable table, DataRow row,
            FieldMapping fMapping) {
        for (Column c : fMapping.getColumns()) {
            Object value = OpenJpaUtil.getValue(fMapping, entity);
            value = convertValue(c, table, value);
            row.setValue(c.getName(), value);
        }
    }

    /**
     * 引数のmetaオブジェクトに関連するカラムデータを対象entityから取得してDataRowにセットします。
     * @param entity 対象Entityオブジェクト
     * @param rowMap DataRow保持Mapオブジェクト
     * @param meta 対象カラムデータを保持するFieldMetaDataオブジェクト
     */
    protected void setRow(final Object entity, Map<String, DataRow> rowMap,
            FieldMetaData meta) {
        if (meta instanceof FieldMapping) {
            FieldMapping mapping = FieldMapping.class.cast(meta);
            for (Column c : mapping.getColumns()) {
                setData(entity, rowMap, meta, c);
                
            }
        }
    }

    /**
     * Columnオブジェクトで定義されたカラムのデータをentityから取得して対象DataRowにセットします。
     * @param entity 対象Entityオブジェクト
     * @param rowMap DataRow保持Mapオブジェクト
     * @param meta 対象カラムデータを保持するFieldMetaDataオブジェクト
     * @param c 対象カラムデータを保持するColumnオブジェクト
     */
    protected void setData(final Object entity, Map<String, DataRow> rowMap,
            FieldMetaData meta, Column c) {
        DataTable table = dataSet.getTable(c.getTableName());
        DataRow row = rowMap.get(table.getTableName());
        if (row == null) {
            row = table.addRow();
            rowMap.put(table.getTableName(), row);
        }
        Object value = OpenJpaUtil.getValue(meta, entity);
        value = convertValue(c, table, value);

        row.setValue(c.getName(), value);
    }

    /**
     * データをカラムの型定義に基づいて型変換を行います。
     * @param c 対象カラムデータを保持するColumnオブジェクト
     * @param table データセット対象DataTableオブジェクト
     * @param value 型変換対象データ
     * @return 型変換したデータ
     */
    protected Object convertValue(Column c, DataTable table, Object value) {
        if (value != null) {
            if (value instanceof Enum) {
                DataColumn column = table.getColumn(c.getName());
                ColumnType type = column.getColumnType();
                if (type instanceof BigDecimalType) {
                    value = Enum.class.cast(value).ordinal();
                }
            }
        }
        return value;
    }
    
    /**
     * OpenJPAEntityDescオブジェクトを返します。
     * @return OpenJPAEntityDescオブジェクト
     */
    protected OpenJPAEntityDesc getEntityDesc() {
        return entityDesc;
    }


    /**
     * @see org.seasar.extension.dataset.DataReader#read()
     */
    public DataSet read() {
        return dataSet;
    }

}
