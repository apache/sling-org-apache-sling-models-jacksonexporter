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

import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * This "mixin" interface instructs the Jackson ObjectMapper what properties should be included in JSON view of a HttpServletRequest object.
 * Without it, the auto-detection may lead to unexpected results, e.g. StackOverflow errors.
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public interface HttpServletRequestMixin extends ServletRequestMixin, HttpServletRequest {

    @JsonGetter
    @Override
    public String getAuthType();

    @JsonGetter
    @Override
    public Cookie[] getCookies();

    @JsonGetter
    @Override
    public String getMethod();

    @JsonGetter
    @Override
    public String getPathInfo();

    @JsonGetter
    @Override
    public String getPathTranslated();

    @JsonGetter
    @Override
    public String getContextPath();

    @JsonGetter
    @Override
    public String getQueryString();

    @JsonGetter
    @Override
    public  String getRemoteUser();

    @JsonGetter
    @Override
    public String getRemoteHost();

    @JsonGetter
    @Override
    public Principal getUserPrincipal();

    @JsonGetter
    @Override
    public String getRequestedSessionId();

    @JsonGetter
    @Override
    public String getRequestURI();

    @JsonGetter
    @Override
    public boolean isRequestedSessionIdFromCookie();

    @JsonGetter
    @Override
    public boolean isRequestedSessionIdFromURL();

    @JsonGetter
    @Override
    public boolean isRequestedSessionIdValid();

    @JsonGetter
    @Override
    public boolean isRequestedSessionIdFromUrl();

    @JsonGetter
    @Override
    public Enumeration<String> getHeaderNames();

    @JsonGetter
    @Override
    public String getServletPath();
}
