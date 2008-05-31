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
package org.seasar.openjpa.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * @author hidenoshin
 *
 */
@Entity
@PrimaryKeyJoinColumn
@Table(name = "JOIND_SAMPLE_CHILD")
public class JoindSampleChild extends JoindSample {

    /**
     * 
     */
    private static final long serialVersionUID = 6153848700524459008L;

    private String childName;

    
    public String getChildName() {
        return childName;
    }

    
    public void setChildName(String childName) {
        this.childName = childName;
    }
}
