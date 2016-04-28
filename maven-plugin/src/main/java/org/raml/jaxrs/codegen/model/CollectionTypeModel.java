package org.raml.jaxrs.codegen.model;

import com.mulesoft.jaxrs.raml.annotation.model.ICollectionTypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import org.raml.jaxrs.codegen.maven.TypeModelRegistry;

/**
 * Created by E355 on 2016/4/21.
 */
public class CollectionTypeModel extends TypeModel implements ICollectionTypeModel {

    private ITypeModel elementTypeModel;

    public CollectionTypeModel(TypeModelRegistry registry,Class<?> actualClass,String genericName) {
        super(registry,actualClass,genericName);
    }
    @Override
    public ITypeModel getElementTypeModel() {
        return elementTypeModel;
    }

    public void setElementTypeModel(ITypeModel elementTypeModel){
        this.elementTypeModel = elementTypeModel;
    }

}
