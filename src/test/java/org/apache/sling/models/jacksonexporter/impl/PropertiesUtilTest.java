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

import static org.apache.sling.models.jacksonexporter.impl.PropertiesUtil.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PropertiesUtilTest {

    @Test
    void testToMap_EmptyArrray() {
        assertEquals(Collections.emptyMap(), toMap(new String[0]));
    }

    @Test
    void testToMap_VariousEntries() {
        String[] input = new String[] {
          "prop1=value1",
          "  prop.2  =  value.2 ",
          "prop3=",
          "prop4",
          "",
          null,
          "prop5=value5a=value5b"
        };

        Map<String,String> expectedOutput = new LinkedHashMap<>();
        expectedOutput.put("prop1", "value1");
        expectedOutput.put("prop.2", "value.2");
        expectedOutput.put("prop5", "value5a=value5b");

        assertEquals(expectedOutput, toMap(input));
    }

}
