/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.jaxrs.codegen.maven;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;

/**
 * <p>TypeModelRegistry class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class TypeModelRegistry {

	private final Map<String,ITypeModel> templateMap = new HashMap<String, ITypeModel>();
	private final Map<String,ITypeModel> targetMap = new HashMap<String,ITypeModel>();

	public void registerType(ITypeModel type) {
		if(type==null){
			return;
		}
		String qualifiedName = type.getFullyQualifiedName();
		if(qualifiedName==null){
			return;
		}
		templateMap.put(qualifiedName, type);
	}

	public Collection<ITypeModel> getTargetTypes() {
		return  targetMap.values();
	}

	public void registerTargetType(ITypeModel type){
		if(type==null){
			return;
		}
		String qualifiedName = type.getFullyQualifiedName();
		if(qualifiedName==null){
			return;
		}
		targetMap.put(qualifiedName, type);
	}

	public ITypeModel getType(String qualifiedName) {
		return templateMap.get(qualifiedName);
	}
}
