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

import java.util.Collections;
import java.util.Map;

import org.apache.sling.models.factory.ExportException;
import org.apache.sling.models.jacksonexporter.impl.example.ExamplePojo;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContext;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests passing in and configuring mapping options for {@link JacksonExporter}.
 */
@ExtendWith(OsgiContextExtension.class)
class JacksonExporterMappingOptionsTest {

    private OsgiContext context = new OsgiContext();

    private ExamplePojo pojoWithData;

    @BeforeEach
    void setUp() {
        pojoWithData = new ExamplePojo().stringProp("value1").numberProp(1).booleanProp(true);
    }

    @Test
    void testDefaultConfig() throws ExportException {
        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String, String> options = Collections.emptyMap();

        String expectedJson = "{\"stringProp\":\"value1\",\"numberProp\":1,\"booleanProp\":true}";
        assertEquals(expectedJson, underTest.export(pojoWithData, String.class, options));
    }

    @Test
    void testPassedInOptions() throws ExportException {
        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String, String> options = Collections.singletonMap("MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", "true");

        String expectedJson = "{\"booleanProp\":true,\"numberProp\":1,\"stringProp\":\"value1\"}";
        assertEquals(expectedJson, underTest.export(pojoWithData, String.class, options));
    }

    @Test
    void testConfiguredOptions() throws ExportException {
        JacksonExporter underTest =
                context.registerInjectActivateService(JacksonExporter.class, "mapping.options", new String[] {
                    "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY=true"
                });
        Map<String, String> options = Collections.emptyMap();

        String expectedJson = "{\"booleanProp\":true,\"numberProp\":1,\"stringProp\":\"value1\"}";
        assertEquals(expectedJson, underTest.export(pojoWithData, String.class, options));
    }

    @Test
    void testPassedInOptionsOverlayConfiguredOptions() throws ExportException {
        JacksonExporter underTest =
                context.registerInjectActivateService(JacksonExporter.class, "mapping.options", new String[] {
                    "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY=true"
                });
        Map<String, String> options = Collections.singletonMap("MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", "false");

        String expectedJson = "{\"stringProp\":\"value1\",\"numberProp\":1,\"booleanProp\":true}";
        assertEquals(expectedJson, underTest.export(pojoWithData, String.class, options));
    }
}
