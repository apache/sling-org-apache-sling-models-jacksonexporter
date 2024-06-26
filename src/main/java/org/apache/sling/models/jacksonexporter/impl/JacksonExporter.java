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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.sling.models.export.spi.ModelExporter;
import org.apache.sling.models.factory.ExportException;
import org.apache.sling.models.jacksonexporter.ModuleProvider;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ModelExporter.class)
@Designate(ocd = JacksonExporter.Config.class)
public class JacksonExporter implements ModelExporter {

    private static final Logger log = LoggerFactory.getLogger(JacksonExporter.class);

    private static final String SERIALIZATION_FEATURE_PREFIX = SerializationFeature.class.getSimpleName() + ".";

    private static final int SERIALIZATION_FEATURE_PREFIX_LENGTH = SERIALIZATION_FEATURE_PREFIX.length();

    private static final String MAPPER_FEATURE_PREFIX = MapperFeature.class.getSimpleName() + ".";

    private static final int MAPPER_FEATURE_PREFIX_LENGTH = MAPPER_FEATURE_PREFIX.length();

    @ObjectClassDefinition(
            name = "Apache Sling Models Jackson Exporter",
            description = "Configures the Jackson JSON Exporter for Sling Models.")
    static @interface Config {

        @AttributeDefinition(
                name = "Mapping options",
                description =
                        "Mapping options to override default Jackson settings. "
                                + "Mapping options that are passed via request parameters have higher precedence. "
                                + "Example: SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS=true, MapperFeature.SORT_PROPERTIES_ALPHABETICALLY=true")
        String[] mapping_options();
    }

    @Reference(
            service = ModuleProvider.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC)
    private volatile Collection<ModuleProvider> moduleProviders;

    private Map<String, String> mappingOptions;

    @Override
    public boolean isSupported(@NotNull Class<?> clazz) {
        return clazz.equals(String.class) || clazz.equals(Map.class);
    }

    @Override
    @SuppressWarnings({"null", "unchecked"})
    public <T> T export(@NotNull Object model, @NotNull Class<T> clazz, @NotNull Map<String, String> options)
            throws ExportException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> mergedOptions = mergeWithConfiguredOptions(options);
        for (Map.Entry<String, String> optionEntry : mergedOptions.entrySet()) {
            String key = optionEntry.getKey();
            if (key.startsWith(SERIALIZATION_FEATURE_PREFIX)) {
                String enumName = key.substring(SERIALIZATION_FEATURE_PREFIX_LENGTH);
                try {
                    SerializationFeature feature = SerializationFeature.valueOf(enumName);
                    mapper.configure(feature, Boolean.valueOf(optionEntry.getValue()));
                } catch (IllegalArgumentException e) {
                    log.warn("Bad SerializationFeature option: {}", enumName);
                }
            } else if (key.startsWith(MAPPER_FEATURE_PREFIX)) {
                String enumName = key.substring(MAPPER_FEATURE_PREFIX_LENGTH);
                try {
                    MapperFeature feature = MapperFeature.valueOf(enumName);
                    mapper.configure(feature, Boolean.valueOf(optionEntry.getValue()));
                } catch (IllegalArgumentException e) {
                    log.warn("Bad MapperFeature option: {}", enumName);
                }
            }
        }
        for (ModuleProvider moduleProvider : moduleProviders) {
            mapper.registerModule(moduleProvider.getModule());
        }

        if (clazz.equals(Map.class)) {
            return (T) mapper.convertValue(model, Map.class);
        } else if (clazz.equals(String.class)) {
            final JsonFactory f = new JsonFactory();
            f.setCharacterEscapes(new EscapeCloseScriptBlocks());
            StringWriter writer = new StringWriter();
            JsonGenerator jgen;
            final boolean printTidy;
            if (mergedOptions.containsKey("tidy")) {
                printTidy = Boolean.valueOf(mergedOptions.get("tidy"));
            } else {
                printTidy = false;
            }
            try {
                jgen = f.createGenerator(writer);
                if (printTidy) {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(jgen, model);
                } else {
                    mapper.writeValue(jgen, model);
                }
            } catch (final IOException e) {
                throw new ExportException(e);
            }
            return (T) writer.toString();
        } else {
            return null;
        }
    }

    private Map<String, String> mergeWithConfiguredOptions(@NotNull Map<String, String> options) {
        Map<String, String> mergedMap = new LinkedHashMap<>();
        mergedMap.putAll(mappingOptions);
        mergedMap.putAll(options);
        return mergedMap;
    }

    @Activate
    private void activate(Config config) {
        this.mappingOptions = PropertiesUtil.toMap(config.mapping_options());
    }

    @Override
    public @NotNull String getName() {
        return "jackson";
    }

    private static class EscapeCloseScriptBlocks extends CharacterEscapes {
        private static final long serialVersionUID = 384022064440034138L;

        private final int[] escapes;

        EscapeCloseScriptBlocks() {
            int[] baseEscapes = standardAsciiEscapesForJSON();
            baseEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
            baseEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
            escapes = baseEscapes;
        }

        @Override
        public int[] getEscapeCodesForAscii() {
            return escapes;
        }

        @Override
        public SerializableString getEscapeSequence(final int arg0) {
            return null;
        }
    }
}
