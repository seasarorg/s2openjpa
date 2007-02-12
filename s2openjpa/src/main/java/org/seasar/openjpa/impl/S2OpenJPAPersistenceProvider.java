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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityManagerFactory;

import org.apache.openjpa.kernel.Bootstrap;
import org.apache.openjpa.kernel.BrokerFactory;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.PersistenceExceptions;
import org.apache.openjpa.persistence.PersistenceProductDerivation;
import org.apache.openjpa.persistence.PersistenceProviderImpl;
import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceTraversal;
import org.seasar.openjpa.AutoDetectorFactory;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class S2OpenJPAPersistenceProvider extends PersistenceProviderImpl {

    private AutoDetectorFactory autoDetectorFactory;

    /**
     * @param autoDetectorFactory
     *            設定する autoDetectorFactory
     */
    public void setAutoDetectorFactory(AutoDetectorFactory autoDetectorFactory) {
        this.autoDetectorFactory = autoDetectorFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.openjpa.persistence.PersistenceProviderImpl#createEntityManagerFactory(java.lang.String,
     *      java.lang.String, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public EntityManagerFactory createEntityManagerFactory(String name,
            String resource, Map m) {
        PersistenceProductDerivation pd = new PersistenceProductDerivation();
        try {
            ConfigurationProvider cp = pd.load(resource, name, m);
            if (cp == null)
                return null;
            if (autoDetectorFactory != null) {

                Map map = cp.getProperties();
                String value = String.class
                        .cast(map.get("openjpa.MetaDataFactory"));
                StringBuilder metaData = new StringBuilder();

                if (value != null) {
                    Scanner scanner = new Scanner(value);
                    scanner.useDelimiter(", ");
                    boolean types = false;
                    boolean maps = false;
                    for (int i = 0; scanner.hasNext(); i++) {
                        String v = scanner.next();
                        if (v.startsWith("Types=")) {
                            StringBuilder values = new StringBuilder(v);

                            v = addClassNames(name, values);
                            types = true;
                        } else if (v.startsWith("Resources=")) {
                            StringBuilder values = new StringBuilder(v);

                            v = addMapFiles(name, values);
                            maps = true;

                        }
                        if (i != 0) {
                            metaData.append(", ");
                            metaData.append(v);
                        }
                    }
                    if (!types) {
                        String addValue = addClassNames(name, null);
                        if (addValue != null && addValue.length() > 0) {
                            metaData.append(", " + addClassNames(name, null));
                        }
                    }
                    if (!maps) {
                        String addValue = addMapFiles(name, null);
                        if (addValue != null && addValue.length() > 0) {
                            metaData.append(", " + addMapFiles(name, null));
                        }
                    }
                } else {
                     String addValue = addClassNames(name, null);
                     if (addValue != null && addValue.length() > 0) {
                         metaData.append(addClassNames(name, null));
                     }
                     String addValue2 = addMapFiles(name, null);
                     if (addValue2 != null && addValue2.length() > 0) {
                         if (metaData.length() > 0) {
                             metaData.append(", ");
                         }
                         metaData.append(addMapFiles(name, null));
                     }
                }
                map.put("openjpa.MetaDataFactory", metaData.toString());
            }

            BrokerFactory factory = Bootstrap.newBrokerFactory(cp, null);
            return OpenJPAPersistence.toEntityManagerFactory(factory);
        } catch (Exception e) {
            throw PersistenceExceptions.toPersistenceException(e);
        }
    }

    private String addClassNames(String name, StringBuilder values) {
        List<String> classList = getPersistenceClassNames(null);
        if (name != null) {
            classList.addAll(getPersistenceClassNames(name));
        }
        for (int j = 0; j < classList.size(); j++) {
            if (values != null) {
                values.append(";");
            } else {
                values = new StringBuilder("Types=");
            }
            values.append(classList.get(j));
        }
        return values != null ? values.toString() : null;
    }

    private String addMapFiles(String name, StringBuilder values) {
        List<String> fileList = getMappingFileNames(null);
        if (name != null) {
            fileList.addAll(getMappingFileNames(name));
        }
        for (int j = 0; j < fileList.size(); j++) {
            if (values != null) {
                values.append(";");
            } else {
                values = new StringBuilder("Resources=");
            }
            values.append(fileList.get(j));
        }
        return values != null ? values.toString() : null;
    }

    public List<String> getPersistenceClassNames(String unitName) {
        final List<String> classList = new ArrayList<String>();
        List<ClassAutoDetector> list = autoDetectorFactory
                .getClassAutoDetectorList(unitName);
        if (list != null) {
            for (ClassAutoDetector cad : list) {
                cad.detect(new ClassTraversal.ClassHandler() {

                    public void processClass(String packageName,
                            String shortClassName) {
                        classList.add(ClassUtil.concatName(packageName,
                                shortClassName));
                    }

                });
            }
        }
        return classList;
    }

    public List<String> getMappingFileNames(String unitName) {
        final List<String> fileList = new ArrayList<String>();
        List<ResourceAutoDetector> list = autoDetectorFactory
                .getResourceAutoDetectorList(unitName);
        if (list != null) {
            for (ResourceAutoDetector rad : list) {
                rad.detect(new ResourceTraversal.ResourceHandler() {

                    public void processResource(String path, InputStream is) {
                        fileList.add(path);
                    }

                });

            }
        }
        return fileList;
    }

}
