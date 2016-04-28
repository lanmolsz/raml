package org.raml.schema.model.impl;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;
import org.raml.schema.model.ICollectionSchemaType;
import org.raml.schema.model.IMapSchemaType;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.JAXBClassMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by E355 on 2016/4/23.
 */
public class MapModelImpl extends TypeModelImpl implements IMapSchemaType {
    public MapModelImpl(
            ISchemaType keyType,
            ISchemaType valueType,
            String name,
            String classQualifiedName,
            Map<String, String> namespaces,
            List<IAnnotationModel> annotations) {
        super(name,classQualifiedName,namespaces,annotations);
        this.keyType = keyType;
        this.valueType = valueType;
    }


    private ISchemaType keyType;
    private ISchemaType valueType;

    @Override
    public ISchemaType getKeyType() {
        return keyType;
    }

    @Override
    public ISchemaType getValueType() {
        return valueType;
    }
}
