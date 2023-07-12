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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ExportException;
import org.apache.sling.models.jacksonexporter.impl.example.PojoWithResourceResolver;
import org.apache.sling.models.jacksonexporter.impl.util.LogCapture;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContext;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContextExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ch.qos.logback.classic.Level;

@ExtendWith(OsgiContextExtension.class)
class JacksonExporterLimitSerializationTest {

    private OsgiContext context = new OsgiContext();
    
    
    
    @Test
    void testWarnLogWhenSerializingResourceResolver() throws ExportException {
        
        LogCapture capture = new LogCapture(JacksonExporter.class.getName(),false);
        
        PojoWithResourceResolver pojo = new PojoWithResourceResolver("text", new EmptyResourceResolver());
        
        context.registerInjectActivateService(new ConfigurableSerializationModuleProvider());
        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String,String> options = Collections.emptyMap();

        String expectedJson = "{\"msg\":\"text\",\"resolver\":{";
        assertTrue(underTest.export(pojo, String.class, options).contains(expectedJson));
        assertTrue(capture.anyMatch(event -> {
           return event.getFormattedMessage().equals(WarningResourceResolverMixin.MESSAGE) && 
                   event.getLevel().equals(Level.WARN);
        }));
    }
    
    
    @Test
    void testNotSerializingResourceResolverWhenDisabled() throws ExportException {
        
        LogCapture capture = new LogCapture(IgnoringResourceResolverMixin.class.getName(),false);        
        PojoWithResourceResolver pojo = new PojoWithResourceResolver("text",new EmptyResourceResolver());
        
        Map<String,Object> config = Collections.singletonMap("disable.serialization", ResourceResolver.class.getName());
        context.registerInjectActivateService(new ConfigurableSerializationModuleProvider(),config);
        
        
        JacksonExporter underTest = context.registerInjectActivateService(JacksonExporter.class);
        Map<String,String> options = Collections.emptyMap();

        String expectedJson = "{\"msg\":\"text\"}";
        assertEquals(expectedJson, underTest.export(pojo, String.class, options));
        
//        assertTrue(capture.anyMatch(p -> p.getFormattedMessage().contains(IgnoringResourceResolverMixin.MESSAGE)));
    }
    
    
    /**
     * A very simple ResourceResolver implementation which does not lead to any issues with any mocking framework
     * when trying to export it with Jackson.
     */
    public class EmptyResourceResolver implements ResourceResolver {
        
//        public static final String SERIALIZED_STRING = "\"resolver\":{\"live\":false,\"userID\":null,\"searchPath\":null,\"propertyMap\":null,\"userID\":false}";
        

        @Override
        public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource resolve(HttpServletRequest request, String absPath) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource resolve(String absPath) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource resolve(HttpServletRequest request) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String map(String resourcePath) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String map(HttpServletRequest request, String resourcePath) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource getResource(String path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource getResource(Resource base, String path) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String[] getSearchPath() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterator<Resource> listChildren(Resource parent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterable<Resource> getChildren(Resource parent) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterator<Resource> findResources(String query, String language) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterator<Map<String, Object>> queryResources(String query, String language) {
            // TODO Auto-generated method stub
            return null;
        }


        @Override
        public ResourceResolver clone(Map<String, Object> authenticationInfo) throws LoginException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isLive() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void close() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String getUserID() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterator<String> getAttributeNames() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void delete(Resource resource) throws PersistenceException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Resource create(Resource parent, String name, Map<String, Object> properties)
                throws PersistenceException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void revert() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void commit() throws PersistenceException {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean hasChanges() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String getParentResourceType(Resource resource) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getParentResourceType(String resourceType) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isResourceType(Resource resource, String resourceType) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void refresh() {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    
}
