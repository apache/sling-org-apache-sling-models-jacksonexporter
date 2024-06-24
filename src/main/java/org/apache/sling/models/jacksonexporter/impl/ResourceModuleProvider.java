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

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.jacksonexporter.ModuleProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(service = ModuleProvider.class)
@Designate(ocd = ResourceModuleProvider.Config.class)
public class ResourceModuleProvider implements ModuleProvider {

    private static final int DEFAULT_MAX_RECURSION_LEVELS = -1;

    @ObjectClassDefinition(
            name = "Apache Sling Models Jackson Exporter - Resource object support",
            description =
                    "Provider of a Jackson Module which enables support for proper serialization of Resource objects")
    static @interface Config {

        @AttributeDefinition(
                name = "Maximum Recursion Levels",
                description =
                        "Maximum number of levels of child resources which will be exported for each resource. Specify -1 for infinite.")
        int max_recursion_levels() default DEFAULT_MAX_RECURSION_LEVELS;
    }

    private SimpleModule moduleInstance;

    @Activate
    private void activate(Config config) {
        final int maxRecursionLevels = config.max_recursion_levels();
        this.moduleInstance = new SimpleModule();
        ModelSkippingSerializers serializers = new ModelSkippingSerializers();
        serializers.addSerializer(Resource.class, new ResourceSerializer(maxRecursionLevels));
        moduleInstance.setSerializers(serializers);
    }

    @Override
    public Module getModule() {
        return moduleInstance;
    }
}
