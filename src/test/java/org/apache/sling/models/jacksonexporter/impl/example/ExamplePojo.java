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
package org.apache.sling.models.jacksonexporter.impl.example;

public class ExamplePojo {

    private String stringProp;
    private Integer numberProp;
    private Boolean booleanProp;

    public String getStringProp() {
        return stringProp;
    }

    public ExamplePojo stringProp(String value) {
        this.stringProp = value;
        return this;
    }

    public Integer getNumberProp() {
        return numberProp;
    }

    public ExamplePojo numberProp(Integer value) {
        this.numberProp = value;
        return this;
    }

    public Boolean getBooleanProp() {
        return booleanProp;
    }

    public ExamplePojo booleanProp(Boolean value) {
        this.booleanProp = value;
        return this;
    }

}
