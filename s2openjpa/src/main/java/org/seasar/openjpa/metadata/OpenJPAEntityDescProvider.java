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

import javax.persistence.EntityManagerFactory;

import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.metadata.EntityDescProvider;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityDescProvider implements EntityDescProvider {
    
    private EntityManagerFactory entityManagerFactory;

    /**
     * @param entityManagerFactory 設定する entityManagerFactory
     */
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * EntityDescFactoryにこのオブジェクトを追加します。
     */
    @InitMethod
    public void register() {
        EntityDescFactory.addProvider(this);
    }

    /**
     * EntityDescFactoryからこのオブジェクトを削除します。
     */
    @DestroyMethod
    public void unregister() {
        EntityDescFactory.removeProvider(this);
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.metadata.EntityDescProvider#createEntityDesc(java.lang.Class)
     */
    public EntityDesc createEntityDesc(Class<?> entityClass) {
        ClassMetaData metaData = OpenJPAPersistence.getMetaData(entityManagerFactory, entityClass);
        if (metaData != null) {
            OpenJPAEntityManagerFactory factory = OpenJPAEntityManagerFactory.class.cast(entityManagerFactory);
            return new OpenJPAEntityDesc(metaData, factory);
        }
        return null;
    }

}
