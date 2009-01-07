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
package org.seasar.openjpa.unit;

import java.util.Collection;
import java.util.Map;

import org.seasar.openjpa.metadata.OpenJPAEntityDesc;


/**
 * OpenJPA用の{@link org.seasar.framework.jpa.unit.EntityReader}実装です（Collection用）。
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJPAEntityCollectionReader extends OpenJPAEntityReader {

    /**
     * Entityクラスと対応するEntityDescのMap
     */
    protected Map<Class<?>, OpenJPAEntityDesc> entityDescs;

    /**
     * EntityReader生成時に一時的にEntityClassを保持するフィールド
     */
    protected Class<?> processingClass;

    /**
     * コンストラクタ
     * @param entities 対象となるEntityのList
     * @param entityDescs Entityクラスと対応するEntityDescのMap
     */
    public OpenJPAEntityCollectionReader(final Collection<?> entities,
            final Map<Class<?>, OpenJPAEntityDesc> entityDescs) {

        this.entityDescs = entityDescs;

        for (final Object entity : entities) {
            processingClass = entity.getClass();
            setupColumns();
            setupRow(entity);
            processingClass = null;
        }
    }

    /**
     * @see org.seasar.openjpa.unit.OpenJPAEntityReader#getEntityDesc()
     */
    @Override
    protected OpenJPAEntityDesc getEntityDesc() {
        return entityDescs.get(processingClass);
    }


}
