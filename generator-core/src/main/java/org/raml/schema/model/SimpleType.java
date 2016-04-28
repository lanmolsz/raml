package org.raml.schema.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;

public enum SimpleType implements ISchemaType {

    INTEGER("Integer",JAXBClassMapping.INTEGER),
    LONG("Long",JAXBClassMapping.LONG),
    SHORT("Short",JAXBClassMapping.SHORT),
    BYTE("Byte",JAXBClassMapping.BYTE),
    DOUBLE("Double",JAXBClassMapping.DOUBLE),
    FLOAT("Float",JAXBClassMapping.FLOAT),
    BOOLEAN("Boolean",JAXBClassMapping.BOOLEAN),
    CHARACTER("Character",JAXBClassMapping.CHARACTER),
    STRING("String",JAXBClassMapping.STRING);

    SimpleType(String name,JAXBClassMapping mapping) {
        this.name = name;
        this.mapping = mapping;
    }
    final private JAXBClassMapping mapping;
    final private String name;

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public List<ISchemaProperty> getProperties() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, String> getNamespaces() {
        return null;
    }

    @Override
    public String getQualifiedPropertyName(ISchemaProperty prop) {
        return prop.getName();
    }

    @Override
    public String getGenericName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return this.name;
    }

    public String getClassQualifiedName() {
        return this.name;
    }

    @Override
    public JAXBClassMapping getMapping() {
        return mapping;
    }

    @Override
    public List<IAnnotationModel> getAnnotations() {
        return new ArrayList<IAnnotationModel>();
    }

    @Override
    public IAnnotationModel getAnnotation(String name) {
        return null;
    }

    public void addProperty(ISchemaProperty property) {
    }

}
