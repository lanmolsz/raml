package org.raml.jaxrs.codegen.model;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.mulesoft.jaxrs.raml.annotation.model.IMember;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;

/**
 * Created by Administrator on 2016/5/4.
 */
public abstract class MemberModel extends BasicModel implements IMember{
    private ITypeModel declaredType;

    public ITypeModel getDeclaredType() {
        return declaredType;
    }

    public void setDeclaredType(ITypeModel declaredType){
        this.declaredType = declaredType;
    }
}
