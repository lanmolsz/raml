package com.mulesoft.jaxrs.raml.annotation.model;

import java.util.List;

/**
 * <p>IMember interface.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public interface IMember extends IBasicModel{

	/**
	 * <p>isStatic.</p>
	 *
	 * @return a boolean.
	 */
	boolean isStatic();
	/**
	 * <p>isPublic.</p>
	 *
	 * @return a boolean.
	 */
	boolean isPublic();
	
	/**
	 * <p>getDeclaredType.</p>
	 *
	 * @return a {@link com.mulesoft.jaxrs.raml.annotation.model.ITypeModel} object.
	 */
	ITypeModel getDeclaredType();
	/**
	 * <p>getJavaType.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	Class<?> getJavaType();

}
