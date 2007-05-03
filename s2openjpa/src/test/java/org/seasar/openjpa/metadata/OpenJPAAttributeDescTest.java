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

import java.util.Set;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.openjpa.entity.Customer;
import org.seasar.openjpa.entity.Product;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAAttributeDescTest extends S2TestCase {

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getElementType()} のためのテスト・メソッド。
     */
    public void testGetElementType() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        AttributeDesc aDesc = desc.getAttributeDesc("products");
        assertEquals(Product.class, aDesc.getElementType());
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getName()} のためのテスト・メソッド。
     */
    public void testGetName() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getSqlType()} のためのテスト・メソッド。
     */
    public void testGetSqlType() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getTemporalType()} のためのテスト・メソッド。
     */
    public void testGetTemporalType() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getType()} のためのテスト・メソッド。
     */
    public void testGetType() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        AttributeDesc aDesc = desc.getAttributeDesc("products");
        Class<?> clazz = aDesc.getType();
        assertEquals(Set.class, clazz);
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#getValue(java.lang.Object)} のためのテスト・メソッド。
     */
    public void testGetValue() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#isAssociation()} のためのテスト・メソッド。
     */
    public void testIsAssociation() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#isCollection()} のためのテスト・メソッド。
     */
    public void testIsCollection() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#isComponent()} のためのテスト・メソッド。
     */
    public void testIsComponent() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#isId()} のためのテスト・メソッド。
     */
    public void testIsId() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#isVersion()} のためのテスト・メソッド。
     */
    public void testIsVersion() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAAttributeDesc#setValue(java.lang.Object, java.lang.Object)} のためのテスト・メソッド。
     */
    public void testSetValue() {
        fail("まだ実装されていません。");
    }

}
