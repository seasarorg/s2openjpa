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
package org.seasar.openjpa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 * @author Hidenoshin Yoshida
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TablePerClassSample {
    
    @Id
    private Integer id;
    
    private String name;

    
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    
    /**
     * @param id 設定する id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name 設定する name
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
