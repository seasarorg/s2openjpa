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
package org.seasar.openjpa.ee;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.openjpa.ee.AbstractManagedRuntime;
import org.seasar.extension.jta.SingletonTransactionManagerProxy;


/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2ManagedRuntime extends AbstractManagedRuntime {

    /* (non-Javadoc)
     * @see org.apache.openjpa.ee.ManagedRuntime#getTransactionManager()
     */
    public TransactionManager getTransactionManager() throws Exception {
        return new SingletonTransactionManagerProxy();
    }

    /* (non-Javadoc)
     * @see org.apache.openjpa.ee.ManagedRuntime#getRollbackCause()
     */
    public Throwable getRollbackCause() throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.openjpa.ee.ManagedRuntime#setRollbackOnly(java.lang.Throwable)
     */
    public void setRollbackOnly(Throwable cause) throws Exception {
//        getTransactionManager().getTransaction().setRollbackOnly();
        Transaction tx = getTransactionManager().getTransaction();
        if (tx.getStatus() != Status.STATUS_MARKED_ROLLBACK) {
            tx.setRollbackOnly();
        }
    }

}
