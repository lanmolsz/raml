package org.raml.schema.model.impl;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;
import org.raml.schema.model.ICollectionSchemaType;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.JAXBClassMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by E355 on 2016/4/23.
 */
public class CollectionModelImpl extends TypeModelImpl implements ICollectionSchemaType {
    public CollectionModelImpl(
            ISchemaType elementType,
            String name,
            String classQualifiedName,
            Map<String, String> namespaces,
            List<IAnnotationModel> annotations) {
        super(name,classQualifiedName,namespaces,annotations);
        this.elementType = elementType;
    }


    private ISchemaType elementType;

    @Override
    public ISchemaType getElementType() {
        return this.elementType;
    }

}
