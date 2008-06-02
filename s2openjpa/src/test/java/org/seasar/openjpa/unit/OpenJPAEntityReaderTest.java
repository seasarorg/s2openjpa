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
package org.seasar.openjpa.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;
import org.seasar.openjpa.entity.Customer;
import org.seasar.openjpa.entity.Dangeon;
import org.seasar.openjpa.entity.Enemy;
import org.seasar.openjpa.entity.Sex;

import static org.seasar.framework.unit.S2Assert.*;


/**
 * @author hidenoshin
 *
 */
@RunWith(Seasar2.class)
public class OpenJPAEntityReaderTest {
    
    private TestContext ctx;


    public void testCustomer() throws ParseException {
        Customer c = new Customer();
        c.setId(1);
        c.setName("小泉");
        c.setAddress("東京");
        c.setPhone("03-3333-3333");
        c.setAge(35);
        c.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("1900/01/02"));
        c.setSex(Sex.WOMAN);
        c.setVersion(0);
        
        assertEntityEquals(ctx.getExpected(), c);
    }

    public void testCustomer2() throws ParseException {
        List<Customer> list = new ArrayList<Customer>();
        Customer c = new Customer();
        c.setId(1);
        c.setName("小泉");
        c.setAddress("神奈川");
        c.setPhone("03-3333-3333");
        c.setAge(35);
        c.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("1900/01/02"));
        c.setSex(Sex.WOMAN);
        c.setVersion(0);
        list.add(c);
        c = new Customer();
        c.setId(2);
        c.setName("安倍");
        c.setAddress("山口");
        c.setPhone("02-1234-5678");
        c.setAge(30);
        c.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("1982/03/21"));
        c.setSex(Sex.MAN);
        c.setVersion(1);
        list.add(c);
        c = new Customer();
        c.setId(3);
        c.setName("前川");
        c.setAddress("東京");
        c.setPhone("01-2345-9876");
        c.setAge(35);
        c.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("1964/05/01"));
        c.setSex(Sex.WOMAN);
        c.setVersion(3);
        list.add(c);
        
        assertEntityEquals(ctx.getExpected(), list);
    }
    
    public void testEnemy() throws ParseException {
        
        Enemy e = new Enemy();
        e.setId(1);
        e.setName("小泉");
        e.setAddress("神奈川");
        e.setPhone("03-3333-3333");
        e.setAge(35);
        e.setBirthday(new SimpleDateFormat("yyyy/MM/dd").parse("1900/01/02"));
        e.setSex(Sex.WOMAN);
        e.setVersion(0);
        Dangeon d = new Dangeon();
        d.setDangeonName("ロンダルキア");
        d.setDangeonLevel(10);
        e.setDangeon(d);
        assertEntityEquals(ctx.getExpected(), e);
    }
}
