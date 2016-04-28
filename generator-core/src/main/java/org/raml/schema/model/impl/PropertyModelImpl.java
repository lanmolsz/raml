package org.raml.schema.model.impl;

import java.util.Arrays;
import java.util.List;

import org.raml.schema.model.ISchemaProperty;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.SchemaModelElement;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;

public class PropertyModelImpl extends SchemaModelElement implements ISchemaProperty {

    public PropertyModelImpl(
            String name,
            ISchemaType type,
            boolean required,
            boolean isAttribute,
            String namespace,
            List<IAnnotationModel> annotations) {
        super(annotations);
        this.name = name;
        this.type = type;
        this.required = required;
        this.isAttribute = isAttribute;
        this.namespace = namespace;
    }

    private String namespace;

    private ISchemaType type;

    private String name;

    private boolean required;

    private boolean isAttribute;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ISchemaType getType() {
        return this.type;
    }

    public void setType(TypeModelImpl model) {
        this.type = model;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public boolean isAttribute() {
        return this.isAttribute;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }
}
