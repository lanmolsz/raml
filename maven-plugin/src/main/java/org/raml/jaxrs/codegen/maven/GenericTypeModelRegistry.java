package org.raml.jaxrs.codegen.maven;

import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by E355 on 2016/4/22.
 */
public class GenericTypeModelRegistry extends TypeModelRegistry{
    private final LinkedHashMap<String,ITypeModel> genericTypeMap = new LinkedHashMap<String, ITypeModel>();
    public void registerType(ITypeModel type) {
        if(type==null || type.getFullyQualifiedName()==null){
            return;
        }
        if(type.getGenericName() == null){
            return;
        }
        genericTypeMap.put(type.getGenericName(), type);
        super.registerType(type);
    }

    public ITypeModel getType(String qualifiedName){
        return genericTypeMap.get(qualifiedName);
    }
}
