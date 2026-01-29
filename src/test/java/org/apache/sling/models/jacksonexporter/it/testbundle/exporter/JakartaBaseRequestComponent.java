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
package org.apache.sling.models.jacksonexporter.it.testbundle.exporter;

import javax.inject.Inject;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.sling.api.SlingJakartaHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(
        adaptables = {SlingJakartaHttpServletRequest.class},
        resourceType = "sling/exp-request/baseJakarta")
@Exporter(
        name = "jackson",
        extensions = "json",
        options = {@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true")})
public class JakartaBaseRequestComponent {

    @Inject
    @SlingObject
    private Resource resource;

    @Inject
    @Via("resource")
    private String sampleValue;

    @Inject
    private Map<String, Object> testBindingsObject;

    @Inject
    private Map<String, Object> testBindingsObject2;

    private final SlingJakartaHttpServletRequest request;

    public JakartaBaseRequestComponent(SlingJakartaHttpServletRequest request) {
        this.request = request;
    }

    public String getId() {
        return this.resource.getPath();
    }

    public String getSampleValue() {
        return sampleValue;
    }

    @JsonProperty(value = "UPPER")
    public String getSampleValueToUpperCase() {
        return sampleValue.toUpperCase();
    }

    public Resource getResource() {
        return resource;
    }

    public Map<String, Object> getTestBindingsObject() {
        return testBindingsObject;
    }

    public Map<String, Object> getTestBindingsObject2() {
        return testBindingsObject2;
    }

    @JsonIgnore
    public SlingJakartaHttpServletRequest getSlingHttpServletRequest() {
        return request;
    }

    @JsonIgnore
    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    @JsonIgnore
    public ServletRequest getServletRequest() {
        return request;
    }
}
