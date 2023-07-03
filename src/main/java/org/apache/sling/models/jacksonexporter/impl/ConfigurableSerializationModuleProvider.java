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

import java.util.Arrays;
import java.util.List;

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
@Designate(ocd = ConfigurableSerializationModuleProvider.Config.class)
public class ConfigurableSerializationModuleProvider implements ModuleProvider {
    
    @ObjectClassDefinition(name = "Apache Sling Models Jackson Exporter - Serialization Blocker",
            description = "Provider of a Jackson Module which can disable the serialization of classes")
    static @interface Config {

        @AttributeDefinition(name ="disable serialization",
                description = "provide a list of the full classnames which should not get serialized")
        String[] disable_serialization() default {};
        
        @AttributeDefinition(name ="warn on serialization",
                description = "provide a list of the full classnames for which a warning should be written when serialized")
        String[] enable_warn_logging() default {ConfigurableSerializationModuleProvider.RESOURCERESOLVER};
        

    }
   
    protected static final String RESOURCERESOLVER = "org.apache.sling.api.resource.ResourceResolver";
    
    
    SimpleModule moduleInstance;
    
    
   
    
    @Activate
    private void activate(Config config) {
        this.moduleInstance = new SimpleModule();
        
        // Currently only the Sling ResourceResolver is supported to be disabled, other classes tbd.
        List<String> disabled = Arrays.asList(config.disable_serialization());
        List<String> logging = Arrays.asList(config.enable_warn_logging());
        
        if (disabled.contains(RESOURCERESOLVER)) {
            moduleInstance.setMixInAnnotation(ResourceResolver.class, IgnoringResourceResolverMixin.class);
        } else if (logging.contains(RESOURCERESOLVER)) {
            moduleInstance.setMixInAnnotation(ResourceResolver.class, WarningResourceResolverMixin.class);
        }
    }
    

    @Override
    public Module getModule() {
        return moduleInstance;
    }

}
