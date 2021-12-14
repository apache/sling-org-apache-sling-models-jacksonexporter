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
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * This "mixin" interface instructs the Jackson ObjectMapper what properties should be included in JSON view of a ServletRequest object.
 * Without it, the auto-detection may lead to unexpected results, e.g. StackOverflow errors.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public interface ServletRequestMixin extends ServletRequest {

    @JsonGetter("parameters")
    @Override
    public Map<String,String[]> getParameterMap();

    @JsonGetter
    @Override
    public Locale getLocale();

    @JsonGetter
    @Override
    public String getContentType();

    @JsonGetter
    @Override
    public int getContentLength();

    @JsonGetter
    @Override
    public int getRemotePort();

    @JsonGetter
    @Override
    public String getRemoteAddr();

    @JsonGetter
    @Override
    public int getServerPort();

    @JsonGetter
    @Override
    public String getServerName();

    @JsonGetter
    @Override
    public boolean isSecure();

    @Override
    public Enumeration<Locale> getLocales();

    @Override
    public String getCharacterEncoding();

    @Override
    public int getLocalPort();

    @Override
    public String getLocalAddr();

    @Override
    public String getLocalName();

    @Override
    public String getProtocol();

    @Override
    public String getScheme();
}
