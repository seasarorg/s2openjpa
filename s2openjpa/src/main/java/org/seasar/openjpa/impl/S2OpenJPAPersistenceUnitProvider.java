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
import javax.persistence.spi.PersistenceProvider;

import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.impl.AbstractPersistenceUnitProvider;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class S2OpenJPAPersistenceUnitProvider extends
        AbstractPersistenceUnitProvider {

    /**
     * PersistenceUnitManagerオブジェクト
     */
    protected PersistenceUnitManager persistenceUnitManager;

    /**
     * PersistenceProviderオブジェクト
     */
    protected PersistenceProvider persistenceProvider;

    /**
     * @param persistenceProvider
     *            設定する persistenceProvider
     */
    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    /**
     * @param persistenceUnitManager
     *            設定する persistenceUnitManager
     */
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    /**
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(java.lang.String)
     */
    public EntityManagerFactory createEntityManagerFactory(String unitName) {
        return persistenceProvider.createEntityManagerFactory(unitName, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(java.lang.String,
     *      java.lang.String)
     */
    public EntityManagerFactory createEntityManagerFactory(
            String abstractUnitName, String concreteUnitName) {
        // TODO 自動生成されたメソッド・スタブ
        return persistenceProvider.createEntityManagerFactory(concreteUnitName,
                null);
    }

}
