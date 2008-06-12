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

import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.unit.Seasar2;
import org.seasar.openjpa.entity.Customer;
import org.seasar.openjpa.entity.JoindSample;
import org.seasar.openjpa.entity.JoindSampleChild;
import org.seasar.openjpa.entity.Product;

import static org.seasar.framework.unit.S2Assert.*;

/**
 * @author Hidenoshin Yoshida
 *
 */
@RunWith(Seasar2.class)
public class OpenJPAEntityDescTest {
    
    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeDesc(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getAttributeDesc("id").getName(), "id");
        assertEquals(desc.getAttributeDesc("name").getName(), "name");
        assertEquals(desc.getAttributeDesc("address").getName(), "address");
        assertEquals(desc.getAttributeDesc("phone").getName(), "phone");
        assertEquals(desc.getAttributeDesc("age").getName(), "age");
        assertEquals(desc.getAttributeDesc("birthday").getName(), "birthday");
        assertEquals(desc.getAttributeDesc("sex").getName(), "sex");
        assertEquals(desc.getAttributeDesc("version").getName(), "version");
        assertEquals(desc.getAttributeDesc("products").getName(), "products");
        assertNull(desc.getAttributeDesc(null));
        assertNull(desc.getAttributeDesc(""));
        
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getAttributeDesc("id").getName(), "id");
        assertEquals(desc.getAttributeDesc("name").getName(), "name");
        assertEquals(desc.getAttributeDesc("version").getName(), "version");
        assertEquals(desc.getAttributeDesc("customer").getName(), "customer");
        assertNull(desc.getAttributeDesc(null));
        assertNull(desc.getAttributeDesc(""));
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeDescs()} のためのテスト・メソッド。
     */
    public void testGetAttributeDescs() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(9, desc.getAttributeDescs().length);
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(4, desc.getAttributeDescs().length);
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getAttributeNames()} のためのテスト・メソッド。
     */
    public void testGetAttributeNames() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        String[] names = desc.getAttributeNames();
        assertEquals(9, names.length);
        List<String> list = Arrays.asList(names);
        list.contains("id");
        list.contains("name");
        list.contains("address");
        list.contains("phone");
        list.contains("age");
        list.contains("birthday");
        list.contains("sex");
        list.contains("version");
        list.contains("products");
        desc = EntityDescFactory.getEntityDesc(Product.class);
        names = desc.getAttributeNames();
        assertEquals(4, names.length);
        list = Arrays.asList(names);
        list.contains("id");
        list.contains("name");
        list.contains("version");
        list.contains("customer");
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getEntityClass()} のためのテスト・メソッド。
     */
    public void testGetEntityClass() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getEntityClass(), Customer.class);
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getEntityClass(), Product.class);
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getEntityName()} のためのテスト・メソッド。
     */
    public void testGetEntityName() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getEntityName(), Customer.class.getSimpleName());
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getEntityName(), Product.class.getSimpleName());
    }

    /**
     * {@link org.seasar.openjpa.metadata.OpenJPAEntityDesc#getIdAttributeDesc()} のためのテスト・メソッド。
     */
    public void testGetIdAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getIdAttributeDesc(), desc.getAttributeDesc("id"));
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getIdAttributeDesc(), desc.getAttributeDesc("id"));
    }
    
    public void testGetTableNames() {
        OpenJPAEntityDesc desc = OpenJPAEntityDesc.class.cast(EntityDescFactory.getEntityDesc(JoindSampleChild.class));
        assertNotNull(desc);
        assertEquals(2, desc.getTables().length);
        assertEquals(JoindSample.class.getSimpleName(), desc.getTables()[0].getName());
        assertEquals("JOIND_SAMPLE_CHILD", desc.getTables()[1].getName());
    }

}
