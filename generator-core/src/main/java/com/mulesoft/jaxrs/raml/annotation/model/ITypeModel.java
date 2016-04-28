package com.mulesoft.jaxrs.raml.annotation.model;

import java.util.List;

/**
 *
 * Model of the type
 *
 * @author kor
 * @version $Id: $Id
 */
public interface ITypeModel extends IBasicModel, IGenericElement{

	
	/**
	 * <p>getMethods.</p>
	 *
	 * @return methods declared in this type
	 */
	IMethodModel[] getMethods();
	
	/**
	 * <p>getFields.</p>
	 *
	 * @return an array of {@link com.mulesoft.jaxrs.raml.annotation.model.IFieldModel} objects.
	 */
	IFieldModel[] getFields();
	
	/**
	 * <p>getFullyQualifiedName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getFullyQualifiedName();

	String getGenericName();

	Class<?> getActualClass();
	
	/**
	 * <p>getSuperClass.</p>
	 * 
	 * @return a {@link com.mulesoft.jaxrs.raml.annotation.model.ITypeModel} object.
	 */
	ITypeModel getSuperClass();
	
	
	/**
	 * <p>getImplementedInterfaces</p>
	 * 
	 * @return an array of {@link com.mulesoft.jaxrs.raml.annotation.model.ITypeModel} objects.
	 */
	List<ITypeModel> getImplementedInterfaces();
	
	
	/**
	 * <p>resolveClass</p>
	 * 
	 * @param qualifiedName class qualified name
	 * @return class model object
	 */
	ITypeModel resolveClass(String qualifiedName);


	/**
	 * @return whether the model type is collection
	 */
	boolean isCollection();

	/**
	 * @return whether the model type is map
	 */
	boolean isMap();

	/**
	 * @return whether the model type is map
	 */
	boolean isEnum();
}
