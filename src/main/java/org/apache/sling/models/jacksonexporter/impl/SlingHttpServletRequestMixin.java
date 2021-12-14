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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * This "mixin" interface instructs the Jackson ObjectMapper what properties should be included in JSON view of a SlingHttpServletRequest object.
 * Without it, the auto-detection may lead to unexpected results, e.g. StackOverflow errors.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public interface SlingHttpServletRequestMixin extends HttpServletRequestMixin, SlingHttpServletRequest {

    @JsonGetter
    @Override
    public Resource getResource();

    @JsonGetter
    @Override
    public RequestPathInfo getRequestPathInfo();

    @JsonGetter
    @Override
    public String getResponseContentType();

    @JsonGetter()
    @Override
    public Enumeration<String> getResponseContentTypes();

}
