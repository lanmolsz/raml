package org.raml.schema.model;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;

import java.util.List;
import java.util.Map;

public interface IMapSchemaType extends ISchemaType{

    ISchemaType getKeyType();
    ISchemaType getValueType();

}
