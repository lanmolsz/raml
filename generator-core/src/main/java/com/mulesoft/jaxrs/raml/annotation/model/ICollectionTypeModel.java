package com.mulesoft.jaxrs.raml.annotation.model;

/**
 * Created by E355 on 2016/4/21.
 */
public interface ICollectionTypeModel extends ITypeModel{
    ITypeModel getElementTypeModel();
}
