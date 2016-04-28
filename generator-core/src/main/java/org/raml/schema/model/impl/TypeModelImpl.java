package org.raml.schema.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.raml.schema.model.ISchemaProperty;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.JAXBClassMapping;
import org.raml.schema.model.SchemaModelElement;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;

public class TypeModelImpl extends SchemaModelElement implements ISchemaType {

    public TypeModelImpl(
            String name,
            String genericName,
            Map<String, String> namespaces,
            List<IAnnotationModel> annotations) {
        super(annotations);
        this.name = name;
        this.namespaces = namespaces;
        this.genericName = genericName;
    }

    public TypeModelImpl(
            String name,
            String genericName,
            Map<String, String> namespaces,
            JAXBClassMapping mapping,
            List<IAnnotationModel> annotations) {
        super(annotations);
        this.name = name;
        this.namespaces = namespaces;
        this.genericName = genericName;
        this.mapping = mapping;
        this.isSimple = true;
    }


    private String name;

    private String genericName;

    private Map<String, String> namespaces;

    private boolean isSimple = false;

    private List<ISchemaProperty> properties;

    private JAXBClassMapping mapping;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSimple() {
        return this.isSimple;
    }

    @Override
    public boolean isComplex() {
        return !this.isSimple;
    }

    @Override
    public List<ISchemaProperty> getProperties() {
        return this.properties;
    }

    @Override
    public void addProperty(ISchemaProperty property) {
        if (this.properties == null) {
            this.properties = new ArrayList<ISchemaProperty>();
        }
        this.properties.add(property);
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    @Override
    public String getQualifiedPropertyName(ISchemaProperty prop) {

        String namespace = prop.getNamespace();
        if (namespace != null && this.namespaces != null) {
            String pref = this.namespaces.get(namespace);
            if (pref != null) {
                return pref + ":" + prop.getName();
            }
        }
        return prop.getName();
    }

    @Override
    public String getGenericName() {
        return genericName;
    }

    public String getClassQualifiedName() {
        String classQualifiedName = genericName;
        if(genericName.contains("<")) {
            classQualifiedName = genericName.substring(0,genericName.indexOf("<"));
        }
        return classQualifiedName.replaceAll("\\[\\]","");
    }

    public String getClassName() {
        String qualifiedName = getClassQualifiedName();
        int ind = qualifiedName.lastIndexOf(".");
        if (ind < 0) {
            return genericName;
        }
        return genericName.substring(ind + 1);
    }

    @Override
    public JAXBClassMapping getMapping() {
        return this.mapping;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
