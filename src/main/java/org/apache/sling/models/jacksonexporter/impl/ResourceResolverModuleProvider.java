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

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.jacksonexporter.ModuleProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Component(service = ModuleProvider.class)
@Designate(ocd = ResourceResolverModuleProvider.Config.class)
public class ResourceResolverModuleProvider implements ModuleProvider {
    
    @ObjectClassDefinition(name = "Apache Sling Models Jackson Exporter - ResourceResolver support",
            description = "Provider of a Jackson Module which enables/disables support for serializing a ResourceResolver")
    static @interface Config {

        @AttributeDefinition(name ="disable serialization of the ResourceResolver",
                description = "if enabled, ResourceResolver instances are not longer deserialized as JSON")
        boolean disable_serialization() default false;

    }
    
    SimpleModule moduleInstance;
    
    
    @Activate
    private void activate(Config config) {
        this.moduleInstance = new SimpleModule();
        if (config.disable_serialization()) {
            moduleInstance.setMixInAnnotation(ResourceResolver.class, IgnoringResourceResolverMixin.class);
        } else {
            moduleInstance.setMixInAnnotation(ResourceResolver.class, WarningResourceResolverMixin.class);
        }
    }
    

    @Override
    public Module getModule() {
        return moduleInstance;
    }

}
