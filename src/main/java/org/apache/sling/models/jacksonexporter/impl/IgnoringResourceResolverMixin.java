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

import org.apache.sling.api.resource.ResourceResolver;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * This mixin disables the serialization of the ResourceResolver; if it's still requested, a ERROR log message is written.
 *
 */

@JsonIgnoreType
public abstract interface IgnoringResourceResolverMixin extends ResourceResolver {

    
    /**
     * TODO: find a way how we can both ignore this type and write a warning when trying to serialize a ResourceResolver.
     */    
    
}