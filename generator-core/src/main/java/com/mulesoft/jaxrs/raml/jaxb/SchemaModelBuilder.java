package com.mulesoft.jaxrs.raml.jaxb;

import com.mulesoft.jaxrs.raml.annotation.model.*;
import org.raml.schema.model.ISchemaProperty;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.JAXBClassMapping;
import org.raml.schema.model.SimpleType;
import org.raml.schema.model.impl.*;

import java.util.*;

public class SchemaModelBuilder {

    public SchemaModelBuilder(JAXBRegistry registry, IRamlConfig config) {
        super();
        this.registry = registry;
        if (config != null) {
            for (IResourceVisitorExtension ext : config.getExtensions()) {
                if (ext instanceof ISchemaModelBuilderExtension) {
                    this.extensions.add((ISchemaModelBuilderExtension) ext);
                }
            }
        }
    }

    protected JAXBRegistry registry;

    protected List<ISchemaModelBuilderExtension> extensions = new ArrayList<ISchemaModelBuilderExtension>();

    protected HashMap<String, TypeModelImpl> jaxbTypeMap = new HashMap<String, TypeModelImpl>();

    public ISchemaType buildSchemaModel(JAXBType jaxbType) {
        ISchemaType schemaType = getType(jaxbType, jaxbType.getNamespaces());
        if(schemaType instanceof TypeModelImpl) {
            for (JAXBProperty p : jaxbType.getAllProperties()) {
                writeProperty(schemaType, p);
            }

            for (ISchemaModelBuilderExtension ext : this.extensions) {
                ext.processType(schemaType);
                ext.processModel(schemaType);
            }
        }
        return schemaType;
    }




    protected ISchemaType getType(JAXBType jaxbType, HashMap<String, String> namespaces) {
        String genericName = jaxbType.getGenericName();
        String xmlName = jaxbType.getXMLName();

        ISchemaType primitive = getPrimitiveType(genericName);
        if (primitive != null) {
            return primitive;
        }

        TypeModelImpl type = this.jaxbTypeMap.get(xmlName);
        if (type != null) {
            return type;
        }

        List<IAnnotationModel> annotations = jaxbType.getAnnotations();
        JAXBClassMapping mapping = JAXBClassMapping.getMapping(genericName);
        if (mapping != null) {
            primitive = getPrimitiveType(mapping.getMappingClass());
            type = new TypeModelImpl(xmlName, primitive.getClassQualifiedName(), namespaces, mapping, annotations);
        } else if(jaxbType.originalModel().isMap()){
            IMapTypeModel model = (IMapTypeModel)jaxbType.originalModel();
            ISchemaType keyType = buildSchemaModel(registry.getJAXBModel(model.getKeyType()));
            ISchemaType valueType = buildSchemaModel(registry.getJAXBModel(model.getValueType()));
            type = new MapModelImpl(keyType,valueType,xmlName, genericName, namespaces, annotations);
        } else if(jaxbType.originalModel().isCollection()){
            ICollectionTypeModel model = (ICollectionTypeModel)jaxbType.originalModel();
            ISchemaType elementType = buildSchemaModel(registry.getJAXBModel(model.getElementTypeModel()));
            type = new CollectionModelImpl(elementType,xmlName,genericName,namespaces,annotations);
        } else if (jaxbType.originalModel().isEnum()) {
            Class<Enum> enumClass = (Class<Enum>) jaxbType.originalModel().getActualClass();
            Set<String> values = new HashSet<String>();
            for (Object e : EnumSet.allOf(enumClass)) {
                values.add(((Enum)e).name());
            }
            type = new EnumSchemaType(values, xmlName, genericName, namespaces, annotations);
        } else {
            type = new TypeModelImpl(xmlName, genericName, namespaces, annotations);
        }

        this.jaxbTypeMap.put(genericName,       type);

        return type;
    }

    protected void writeProperty(ISchemaType typeModel, JAXBProperty p) {
        String name = p.name();
        if (name == null || name.length() == 0) {
            return;
        }

        boolean isRequired = p.isRequired();
        boolean isAttribute = p instanceof JAXBAttributeProperty;
        String namespace = p.getNamespace();
        List<IAnnotationModel> annotations = p.getAnnotations();
        ISchemaType propertyType = buildSchemaModel(p.getType());

        ISchemaProperty prop = new PropertyModelImpl(name, propertyType,
                isRequired, isAttribute, namespace, annotations);

        for (ISchemaModelBuilderExtension ext : this.extensions) {
            prop = ext.processProperty(p, prop);
        }
        typeModel.addProperty(prop);
    }


    public ISchemaType getPrimitiveType(String qualifiedName) {

        String name = qualifiedName;
        if (qualifiedName.startsWith("java.lang.")) {
            name = qualifiedName.substring("java.lang.".length());
        }
        name = name.toUpperCase();
        if (name.equals("CHAR")) {
            name = "CHARACTER";
        }
        if (name.equals("INT")) {
            name = "INTEGER";
        }

        try {
            return SimpleType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }
}
