/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.junit.runner.RunWith;
import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.unit.Seasar2;
import org.seasar.openjpa.entity.Customer;

import static org.seasar.framework.unit.S2Assert.*;

/**
 * @author Hidenoshin Yoshida
 * 
 */
@RunWith(Seasar2.class)
public class S2OpenJPADialectTest {

    private Dialect dialect;

    private EntityManager entityManager;

    /**
     * {@link org.seasar.openjpa.impl.S2OpenJPADialect#getConnection(javax.persistence.EntityManager)}
     * のためのテスト・メソッド。
     * 
     * @throws SQLException
     */
    public void testGetConnection() throws SQLException {
        Connection con = dialect.getConnection(entityManager);
        try {
            assertNotNull(con);
        } finally {
            con.close();
        }
    }

    public void testDetach() throws Exception {
        Customer cust1 = entityManager.find(Customer.class, 1);
        dialect.detach(entityManager, cust1);
        assertFalse(entityManager.contains(cust1));
        Customer cust2 = entityManager.find(Customer.class, 1);
        assertNotSame(cust1, cust2);
    }

}
