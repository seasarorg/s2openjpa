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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.FieldStrategy;
import org.apache.openjpa.jdbc.meta.ValueMappingInfo;
import org.apache.openjpa.jdbc.meta.strats.ContainerFieldStrategy;
import org.apache.openjpa.jdbc.meta.strats.RelationFieldStrategy;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.util.TemporalTypeUtil;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class OpenJPAAttributeDesc implements AttributeDesc {

    private FieldMetaData fieldMetaData;
    
    private ValueMappingInfo mappingInfo;
    
    private OpenJPAEntityManagerFactorySPI openJPAEntityManagerFactory;

    private OpenJPAEntityDesc embeddedEntityDesc;
    
    public OpenJPAAttributeDesc(FieldMetaData fieldMetaData,
            OpenJPAEntityManagerFactorySPI openJPAEntityManagerFactory) {
        this.fieldMetaData = fieldMetaData;
        this.openJPAEntityManagerFactory = openJPAEntityManagerFactory;
        if (fieldMetaData.getEmbeddedMetaData() != null) {
            embeddedEntityDesc = new OpenJPAEntityDesc(
                fieldMetaData.getEmbeddedMetaData(),
                openJPAEntityManagerFactory);
        }
        if (fieldMetaData instanceof FieldMapping) {
            mappingInfo = FieldMapping.class.cast(fieldMetaData).getValueInfo();
        }
    }
    
    public FieldMetaData getFieldMetaData() {
        return fieldMetaData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getElementType()
     */
    public Class<?> getElementType() {
        if (isCollection()) {
            Member member = fieldMetaData.getBackingMember();
            Type type = null;
            if (member instanceof Field) {
                Field field = Field.class.cast(member);
                type = field.getGenericType();
            } else if (member instanceof Method) {
                Method method = Method.class.cast(member);
                type = method.getGenericReturnType();
            }
            if (type != null && type instanceof ParameterizedType) {
                ParameterizedType pt = ParameterizedType.class.cast(type);
                if (pt.getActualTypeArguments().length > 0) {
                    return (Class<?>) pt.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getName()
     */
    public String getName() {
        return fieldMetaData.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getSqlType()
     */
    public int getSqlType() {
        if (mappingInfo != null) {
            @SuppressWarnings("unchecked")
            List cols = mappingInfo.getColumns();
            if (cols != null && cols.size() == 1) {
                Column col = (Column) cols.get(0);
                return col.getType();
            }
        }
        OpenJPAConfiguration oConf = openJPAEntityManagerFactory.getConfiguration();
        if (oConf instanceof JDBCConfiguration) {
            JDBCConfiguration conf = JDBCConfiguration.class.cast(oConf);
            int typeCode = fieldMetaData.getTypeCode();
            return conf.getDBDictionaryInstance().getJDBCType(typeCode, false);
        }
        return Types.OTHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getTemporalType()
     */
    public TemporalType getTemporalType() {
        Class<?> type = fieldMetaData.getType();
        if (type == Date.class || type == Calendar.class) {
            return TemporalTypeUtil.fromSqlTypeToTemporalType(getSqlType());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getType()
     */
    public Class<?> getType() {
        return fieldMetaData.getDeclaredType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getValue(java.lang.Object)
     */
    public Object getValue(Object entity) {
        Member member = fieldMetaData.getBackingMember();
        if (member instanceof Field) {
            Field field = Field.class.cast(member);
            boolean access = field.isAccessible();
            try {
                field.setAccessible(true);
                return field.get(entity);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(field.getClass(), e);
            } finally {
                field.setAccessible(access);
            }
        } else if (member instanceof Method) {
            Method method = Method.class.cast(member);
            try {
                return method.invoke(entity, (Object) null);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(method.getClass(), e);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetRuntimeException(method.getClass(), e);
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isAssociation()
     */
    public boolean isAssociation() {
        if (fieldMetaData instanceof FieldMapping) {
            FieldStrategy strategy = FieldMapping.class.cast(fieldMetaData).getStrategy();
            return strategy instanceof RelationFieldStrategy
                || strategy instanceof ContainerFieldStrategy;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isCollection()
     */
    public boolean isCollection() {
        Class<?> clazz = getType();
        return Collection.class.isAssignableFrom(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isComponent()
     */
    public boolean isComponent() {
        return fieldMetaData.getEmbeddedMetaData() != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isId()
     */
    public boolean isId() {
        return fieldMetaData.isPrimaryKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isVersion()
     */
    public boolean isVersion() {
        return fieldMetaData.isVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#setValue(java.lang.Object,
     *      java.lang.Object)
     */
    public void setValue(Object entity, Object value) {
        Member member = fieldMetaData.getBackingMember();
        if (member instanceof Field) {
            Field field = Field.class.cast(member);
            boolean access = field.isAccessible();
            try {
                field.setAccessible(true);
                field.set(entity, value);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(field.getClass(), e);
            } finally {
                field.setAccessible(access);
            }
        } else if (member instanceof Method) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(getName(), entity.getClass());
                Method method = pd.getWriteMethod();
                try {
                    method.invoke(entity, (Object) null);
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessRuntimeException(method.getClass(), e);
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetRuntimeException(method.getClass(), e);
                }
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getChildAttributeDesc(java.lang.String)
     */
    public OpenJPAAttributeDesc getChildAttributeDesc(String name) {
        if (embeddedEntityDesc != null) {
            return embeddedEntityDesc.getAttributeDesc(name);
        }
        return null;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getChildAttributeDescs()
     */
    public OpenJPAAttributeDesc[] getChildAttributeDescs() {
        if (embeddedEntityDesc != null) {
            return embeddedEntityDesc.getAttributeDescs();
        }
        return null;
    }

}
