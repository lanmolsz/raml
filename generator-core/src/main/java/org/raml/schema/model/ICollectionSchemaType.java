package org.raml.schema.model;

/**
 * Created by E355 on 2016/4/23.
 */
public interface ICollectionSchemaType extends ISchemaType{
    ISchemaType getElementType();
}
