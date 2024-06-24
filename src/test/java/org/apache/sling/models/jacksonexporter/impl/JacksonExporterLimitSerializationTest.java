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

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ExportException;
import org.apache.sling.models.jacksonexporter.impl.example.PojoWithResourceResolver;
import org.apache.sling.models.jacksonexporter.impl.util.LogCapture;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContext;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OsgiContextExtension.class)
class JacksonExporterLimitSerializationTest {

    private OsgiContext context = new OsgiContext();

    @Test
    void testWarnLogWhenSerializingResourceResolver() throws Exception {

        LogCapture capture = new LogCapture(JacksonExporter.class.getName(), false);

        PojoWithResourceResolver pojo = new PojoWithResourceResolver("text", new EmptyResourceResolver());

        context.registerInjectActivateService(new ConfigurableSerializationModuleProvider());
        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String, String> options = Collections.emptyMap();

        String json = underTest.export(pojo, String.class, options);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.readTree(json);
        assertTrue(result.path("resolver").isContainerNode());
        assertTrue(capture.anyMatch(event -> {
            return event.getFormattedMessage().equals(WarningResourceResolverMixin.MESSAGE)
                    && event.getLevel().equals(Level.WARN);
        }));
    }

    @Test
    void testNotSerializingResourceResolverWhenDisabled() throws Exception {

        LogCapture capture = new LogCapture(ConfigurableSerializationModuleProvider.class.getName(), false);

        PojoWithResourceResolver pojo = new PojoWithResourceResolver("text", new EmptyResourceResolver());
        Map<String, Object> config =
                Collections.singletonMap("disable.serialization", ResourceResolver.class.getName());
        context.registerInjectActivateService(new ConfigurableSerializationModuleProvider(), config);

        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String, String> options = Collections.emptyMap();

        String expectedJson = "{\"msg\":\"text\"}";
        assertEquals(expectedJson, underTest.export(pojo, String.class, options));
        // no log is written in this case
        assertEquals(0, capture.list.size());
    }

    @Test
    void test_givenInvalidTypes_whenActivate_thenWarnLogStatements() throws ExportException {
        LogCapture capture = new LogCapture(ConfigurableSerializationModuleProvider.class.getName(), false);

        Map<String, Object> config = new HashMap<>();
        config.put("disable.serialization", "foo.bar.disable");
        config.put("enable.warn.logging", "foo.bar.logging");

        context.registerInjectActivateService(new ConfigurableSerializationModuleProvider(), config);

        assertTrue(capture.anyMatch(event -> {
            return event.getFormattedMessage()
                            .equals("Support to disable the serialization of type foo.bar.disable is not implemented")
                    && event.getLevel().equals(Level.WARN);
        }));
        assertTrue(capture.anyMatch(event -> {
            return event.getFormattedMessage()
                            .equals("Support to log any serialization of type foo.bar.logging is not implemented")
                    && event.getLevel().equals(Level.WARN);
        }));
    }

    /**
     * A very simple ResourceResolver implementation which does not lead to any issues with any mocking framework
     * when trying to export it with Jackson.
     */
    public class EmptyResourceResolver implements ResourceResolver {

        @Override
        public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            return null;
        }

        @Override
        public Resource resolve(HttpServletRequest request, String absPath) {
            return null;
        }

        @Override
        public Resource resolve(String absPath) {
            return null;
        }

        @Override
        public Resource resolve(HttpServletRequest request) {
            return null;
        }

        @Override
        public String map(String resourcePath) {
            return null;
        }

        @Override
        public String map(HttpServletRequest request, String resourcePath) {
            return null;
        }

        @Override
        public Resource getResource(String path) {
            return null;
        }

        @Override
        public Resource getResource(Resource base, String path) {
            return null;
        }

        @Override
        public String[] getSearchPath() {
            return null;
        }

        @Override
        public Iterator<Resource> listChildren(Resource parent) {
            return null;
        }

        @Override
        public Iterable<Resource> getChildren(Resource parent) {
            return null;
        }

        @Override
        public Iterator<Resource> findResources(String query, String language) {
            return null;
        }

        @Override
        public Iterator<Map<String, Object>> queryResources(String query, String language) {
            return null;
        }

        @Override
        public ResourceResolver clone(Map<String, Object> authenticationInfo) throws LoginException {
            return null;
        }

        @Override
        public boolean isLive() {
            return false;
        }

        @Override
        public void close() {
            // do nothing
        }

        @Override
        public String getUserID() {
            return null;
        }

        @Override
        public Iterator<String> getAttributeNames() {
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public void delete(Resource resource) throws PersistenceException {
            // do nothing
        }

        @Override
        public Resource create(Resource parent, String name, Map<String, Object> properties)
                throws PersistenceException {
            return null;
        }

        @Override
        public void revert() {
            // do nothing
        }

        @Override
        public void commit() throws PersistenceException {
            // do nothing
        }

        @Override
        public boolean hasChanges() {
            return false;
        }

        @Override
        public String getParentResourceType(Resource resource) {
            return null;
        }

        @Override
        public String getParentResourceType(String resourceType) {
            return null;
        }

        @Override
        public boolean isResourceType(Resource resource, String resourceType) {
            return false;
        }

        @Override
        public void refresh() {
            // do nothing
        }
    }
}
