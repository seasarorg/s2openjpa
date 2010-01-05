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
package org.seasar.openjpa.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.apache.openjpa.meta.FieldMetaData;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;


/**
 * OpenJPA内のAPIを利用する為のユーティリティクラスです。
 * @author Hidenoshin Yoshida
 *
 */
public class OpenJpaUtil {
    
    /**
     * FieldMetaDataオブジェクトに定義されたカラムのデータをentityオブジェクトから取得します。
     * @param fieldMetaData 対象カラムデータを保持するFieldMetaDetaオブジェクト
     * @param entity データ取得元となるEntityオブジェクト
     * @return entityから取得した対象カラムデータ
     */
    public static Object getValue(FieldMetaData fieldMetaData, Object entity) {
        if (entity == null) {
            return null;
        }
        Member member = fieldMetaData.getBackingMember();
        if (member instanceof Field) {
            Field field = Field.class.cast(member);
            boolean access = field.isAccessible();
            try {
                field.setAccessible(true);
                return field.get(entity);
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(field.getClass(), e);
            } finally {
                field.setAccessible(access);
            }
        } else if (member instanceof Method) {
            Method method = Method.class.cast(member);
            try {
                return method.invoke(entity, new Object[]{});
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(method.getClass(), e);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetRuntimeException(method.getClass(), e);
            }
        }
        return null;
    }

}
