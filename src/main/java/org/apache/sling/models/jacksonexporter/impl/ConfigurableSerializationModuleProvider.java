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

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.jacksonexporter.ModuleProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ModuleProvider.class)
@Designate(ocd = ConfigurableSerializationModuleProvider.Config.class)
public class ConfigurableSerializationModuleProvider implements ModuleProvider {

    @ObjectClassDefinition(
            name = "Apache Sling Models Jackson Exporter - Serialization Blocker",
            description = "Provider of a Jackson Module which can disable the serialization of classes")
    static @interface Config {

        @AttributeDefinition(
                name = "disable serialization",
                description = "provide a list of the full classnames which should not get serialized; currently only \""
                        + RESOURCERESOLVER + "\" is supported")
        String[] disable_serialization() default {};

        @AttributeDefinition(
                name = "warn on serialization",
                description =
                        "provide a list of the full classnames for which a warning should be written when serialized; currently only \""
                                + RESOURCERESOLVER + "\" is supported")
        String[] enable_warn_logging() default {ConfigurableSerializationModuleProvider.RESOURCERESOLVER};
    }

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableSerializationModuleProvider.class);
    protected static final String RESOURCERESOLVER = "org.apache.sling.api.resource.ResourceResolver";

    boolean ignoringRR = false;

    SimpleModule moduleInstance;

    @Activate
    private void activate(Config config) {
        this.moduleInstance = new SimpleModule();

        List<String> disabled = Arrays.asList(config.disable_serialization());
        List<String> logging = Arrays.asList(config.enable_warn_logging());

        // Currently only the Sling ResourceResolver is supported
        for (String type : disabled) {
            if (RESOURCERESOLVER.equals(type)) {
                moduleInstance.setMixInAnnotation(ResourceResolver.class, IgnoringResourceResolverMixin.class);
                ignoringRR = true;
            } else {
                LOG.warn("Support to disable the serialization of type {} is not implemented", type);
            }
        }

        for (String type : logging) {
            if (RESOURCERESOLVER.equals(type)) {
                if (!ignoringRR) {
                    moduleInstance.setMixInAnnotation(ResourceResolver.class, WarningResourceResolverMixin.class);
                }
            } else {
                LOG.warn("Support to log any serialization of type {} is not implemented", type);
            }
        }
    }

    @Override
    public Module getModule() {
        return moduleInstance;
    }
}
