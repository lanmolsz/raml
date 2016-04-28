package org.raml.jaxrs.codegen.model;

import com.mulesoft.jaxrs.raml.annotation.model.IMapTypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import org.raml.jaxrs.codegen.maven.TypeModelRegistry;

/**
 * Created by E355 on 2016/4/21.
 */
public class MapTypeModel extends TypeModel implements IMapTypeModel {
    private ITypeModel keyType;
    private ITypeModel valueType;

    public MapTypeModel(TypeModelRegistry registry,Class<?> actualClass,String genericName) {
        super(registry,actualClass,genericName);
    }

    @Override
    public ITypeModel getKeyType() {
        return keyType;
    }

    public void setKeyType(ITypeModel keyType) {
        this.keyType = keyType;
    }

    @Override
    public ITypeModel getValueType() {
        return valueType;
    }

    public void setValueType(ITypeModel valueType) {
        this.valueType = valueType;
    }
}
