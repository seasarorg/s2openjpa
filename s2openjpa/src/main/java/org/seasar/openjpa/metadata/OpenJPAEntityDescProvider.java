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
package org.seasar.openjpa.metadata;

import javax.persistence.EntityManagerFactory;

import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;

/**
 * OpenJPA用の{@link EntityDescProvider}実装です。
 * @author Hidenoshin Yoshida
 * 
 */
public class OpenJPAEntityDescProvider implements EntityDescProvider {

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDescProvider#createEntityDesc(javax.persistence.EntityManagerFactory,
     *      java.lang.Class)
     */
    public EntityDesc createEntityDesc(EntityManagerFactory emf,
            Class<?> entityClass) {
        ClassMetaData metaData = JPAFacadeHelper.getMetaData(emf,
                entityClass);
        if (metaData != null) {
            OpenJPAEntityManagerFactorySPI factory = OpenJPAEntityManagerFactorySPI.class
                    .cast(emf);
            return new OpenJPAEntityDesc(metaData, factory);
        }
        return null;
    }

}
