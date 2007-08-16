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

import java.sql.Connection;

import javax.persistence.EntityManager;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.jpa.DialectManager;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class S2OpenJPADialect implements Dialect {

    /**
     * DialectManagerオブジェクト
     */
    @Binding(bindingType = BindingType.MUST)
    protected DialectManager dialectManager;

    /**
     * dialectManagerにこのオブジェクトを登録します。
     */
    @InitMethod
    public void initialize() {
        dialectManager.addDialect(OpenJPAEntityManager.class, this);
    }

    /**
     * dialectManagerからこのオブジェクトを削除します。
     */
    @DestroyMethod
    public void destroy() {
        dialectManager.removeDialect(OpenJPAEntityManager.class);
    }

    public Connection getConnection(EntityManager em) {
        Object delegate = em.getDelegate();
        OpenJPAEntityManager ojpaEm = OpenJPAEntityManager.class.cast(delegate);
        return Connection.class.cast(ojpaEm.getConnection());
    }

    public void detach(EntityManager em, Object managedEntity) {
        Object delegate = em.getDelegate();
        OpenJPAEntityManager ojpaEm = OpenJPAEntityManager.class.cast(delegate);
        ojpaEm.release(managedEntity);
    }

}
