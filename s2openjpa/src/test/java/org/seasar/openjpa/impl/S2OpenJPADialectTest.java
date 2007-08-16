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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.Dialect;
import org.seasar.openjpa.entity.Customer;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class S2OpenJPADialectTest extends S2TestCase {

    private Dialect dialect;

    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.openjpa.impl.S2OpenJPADialect#getConnection(javax.persistence.EntityManager)}
     * のためのテスト・メソッド。
     * 
     * @throws SQLException
     */
    public void testGetConnectionTx() throws SQLException {
        Connection con = dialect.getConnection(entityManager);
        try {
            assertNotNull(con);
            Statement st = con.createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT * FROM CUSTOMER");
                try {
                    while (rs.next()) {
                        System.out.print(rs.getObject(1));
                        System.out.print("," + rs.getObject(2));
                        System.out.println("," + rs.getObject(3));
                    }
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } finally {
            con.close();
        }

        System.out.println(entityManager.createNamedQuery("countCustomer")
                .getSingleResult());
    }

    public void testDetachTx() throws Exception {
        Customer cust1 = entityManager.find(Customer.class, 1);
        dialect.detach(entityManager, cust1);
        assertFalse(entityManager.contains(cust1));
        Customer cust2 = entityManager.find(Customer.class, 1);
        assertNotSame(cust1, cust2);
    }

}
