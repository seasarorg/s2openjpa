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

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.openjpa.entity.Customer;
import org.seasar.openjpa.entity.Enemy;
import org.seasar.openjpa.entity.Product;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityDescTest extends S2TestCase {

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeDesc(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Enemy.class);
        AttributeDesc aDesc = desc.getAttributeDesc("dangeon");
        assertEquals("dangeon", aDesc.getName());
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeDescs()} のためのテスト・メソッド。
     */
    public void testGetAttributeDescs() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeNames()} のためのテスト・メソッド。
     */
    public void testGetAttributeNames() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        String[] names = desc.getAttributeNames();
        assertEquals(9, names.length);
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getEntityClass()} のためのテスト・メソッド。
     */
    public void testGetEntityClass() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(Customer.class, desc.getEntityClass());
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getEntityName()} のためのテスト・メソッド。
     */
    public void testGetEntityName() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals("Customer", desc.getEntityName());
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getIdAttributeDesc()} のためのテスト・メソッド。
     */
    public void testGetIdAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Product.class);
        AttributeDesc aDesc = desc.getIdAttributeDesc();
        assertEquals("id", aDesc.getName());
    }

}
