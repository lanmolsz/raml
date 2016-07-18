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
package org.raml.jaxrs.codegen.model;

import com.mulesoft.jaxrs.raml.annotation.model.IParameterModel;

/**
 * <p>ParameterModel class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class ParameterModel extends MemberModel implements IParameterModel{

	/**
	 * <p>Constructor for ParameterModel.</p>
	 */
	public ParameterModel() {
	}
	
	
	private String type;

	private String parameterActualType;

	
	private boolean required;





	/**
	 * <p>Setter for the field <code>required</code>.</p>
	 *
	 * @param required a boolean.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * <p>required.</p>
	 *
	 * @return a boolean.
	 */
	public boolean required() {
		return required;
	}


	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getParameterType() {
		return type;
	}


	public void setActualType(String parameterActualType){
		this.parameterActualType = parameterActualType;
	}

	@Override
	public String getParameterActualType() {
		return parameterActualType;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (required ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParameterModel other = (ParameterModel) obj;
		if (required != other.required)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
