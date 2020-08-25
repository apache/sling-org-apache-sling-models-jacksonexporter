/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.models.jacksonexporter.impl;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.jacksonexporter.ModuleProvider;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component(property=Constants.SERVICE_RANKING+":Integer=0", service=ModuleProvider.class)
public class RequestModuleProvider implements ModuleProvider {

    private final SimpleModule moduleInstance;

    public RequestModuleProvider() {
        this.moduleInstance = new SimpleModule();
        moduleInstance.setMixInAnnotation(SlingHttpServletRequest.class, SlingHttpServletRequestMixin.class);
        moduleInstance.setMixInAnnotation(HttpServletRequest.class, HttpServletRequestMixin.class);
        moduleInstance.setMixInAnnotation(ServletRequest.class, ServletRequestMixin.class);
        moduleInstance.addSerializer(Enumeration.class, new EnumerationSerializer());
    }

    @Override
    public Module getModule() {
        return moduleInstance;
    }
}
