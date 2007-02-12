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

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2OpenJPAPersistenceUnitProvider implements
        PersistenceUnitProvider {

    /**
     * PersistenceUnitManagerオブジェクト
     */
    protected PersistenceUnitManager persistenceUnitManager;
    
    /**
     * PersistenceProviderオブジェクト
     */
    protected PersistenceProvider persistenceProvider;
    
    
    /**
     * @param persistenceProvider 設定する persistenceProvider
     */
    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    
    /**
     * @param persistenceUnitManager 設定する persistenceUnitManager
     */
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }
    
    /**
     * PersistenceUnitManagerにこのオブジェクトを登録します。
     */
    @InitMethod
    public void register() {
        persistenceUnitManager.addProvider(this);
    }

    /**
     * PersistenceUnitManagerからこのオブジェクトを削除します。
     */
    @DestroyMethod
    public void unregister() {
        persistenceUnitManager.removeProvider(this);
    }

    /**
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(java.lang.String)
     */
    public EntityManagerFactory createEntityManagerFactory(String unitName) {
        return persistenceProvider.createEntityManagerFactory(unitName, null);
    }

}
