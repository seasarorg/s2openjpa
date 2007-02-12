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
package org.seasar.openjpa.impl;

import javax.persistence.EntityManagerFactory;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.PersistenceUnitProvider;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2OpenJPAPersistenceUnitProviderTest extends S2TestCase {

    private PersistenceUnitProvider persistenceUnitProvider;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.openjpa.impl.S2OpenJPAPersistenceUnitProvider#createEntityManagerFactory(java.lang.String)} のためのテスト・メソッド。
     */
    public void testCreateEntityManagerFactory() {
        EntityManagerFactory emf = persistenceUnitProvider.createEntityManagerFactory("persistenceUnit");
        assertNotNull(emf);
    }

}
