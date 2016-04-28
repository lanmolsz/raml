package org.raml.schema.model.impl;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import org.raml.schema.model.IEnumSchemaType;
import org.raml.schema.model.ISchemaType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/4/26.
 */
public class EnumSchemaType extends TypeModelImpl implements IEnumSchemaType{
    public EnumSchemaType(
            Set<String> values,
            String name,
            String classQualifiedName,
            Map<String, String> namespaces,
            List<IAnnotationModel> annotations) {
        super(name,classQualifiedName,namespaces,annotations);
        this.values = values;
    }

    private Set<String> values;

    @Override
    public Set<String> values() {
        return values;
    }
}
