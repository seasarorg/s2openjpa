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

import java.util.Collection;

import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderProvider;
import org.seasar.openjpa.metadata.OpenJPAEntityDesc;


/**
 * @author hidenoshin
 *
 */
public class OpenJPAEntityReaderProvider implements EntityReaderProvider {

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.unit.EntityReaderProvider#createEntityReader(java.lang.Object)
     */
    public OpenJPAEntityReader createEntityReader(Object entity) {
        if (entity == null) {
            return null;
        }
        final OpenJPAEntityDesc entityDesc = getEntityDesc(entity.getClass());
        if (entityDesc == null) {
            return null;
        }
        return new OpenJPAEntityReader(entity, entityDesc);
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.unit.EntityReaderProvider#createEntityReader(java.util.Collection)
     */
    public EntityReader createEntityReader(Collection<?> entities) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }
    
    /**
     * 指定したentityClassに対応するOpenJPAEntityDescオブジェクトを返します。
     * 
     * @param entityClass
     *            Entityクラス
     * @return entityClassに対応するOpenJPAEntityDesc
     */
    protected OpenJPAEntityDesc getEntityDesc(final Class<?> entityClass) {
        final EntityDesc entityDesc = EntityDescFactory
                .getEntityDesc(entityClass);
        if (entityDesc == null || !(entityDesc instanceof OpenJPAEntityDesc)) {
            return null;
        }
        return OpenJPAEntityDesc.class.cast(entityDesc);
    }

}
