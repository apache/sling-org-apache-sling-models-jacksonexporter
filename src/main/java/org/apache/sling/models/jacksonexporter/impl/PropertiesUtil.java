/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.models.jacksonexporter.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

final class PropertiesUtil {

    private PropertiesUtil() {
        // static methods only
    }

    /**
     * Returns the parameter as a map with string keys and string values.
     *
     * The parameter is considered as a collection whose entries are of the form
     * key=value. The conversion has following rules
     * <ul>
     *     <li>Entries are of the form key=value</li>
     *     <li>key and value are trimmed.</li>
     *     <li>Malformed entries like 'foo','foo=' are ignored</li>
     *     <li>Map entries maintain the input order</li>
     * </ul>
     *
     * @param arrayValue The array to be converted to map.
     * @return Map value
     */
    static Map<String, String> toMap(String @NotNull [] stringArray) {
        //in property values
        Map<String, String> result = new LinkedHashMap<>();
        for (String kv : stringArray) {
            if (kv == null) {
                continue;
            }
            int indexOfEqual = kv.indexOf('=');
            if (indexOfEqual > 0) {
                String key = StringUtils.trimToNull(kv.substring(0, indexOfEqual));
                String value = StringUtils.trimToNull(kv.substring(indexOfEqual + 1));
                if (key != null && value != null) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

}
