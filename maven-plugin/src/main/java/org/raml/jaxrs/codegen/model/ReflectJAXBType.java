package org.raml.jaxrs.codegen.model;


import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import com.mulesoft.jaxrs.raml.jaxb.JAXBRegistry;
import com.mulesoft.jaxrs.raml.jaxb.JAXBType;


public class ReflectJAXBType extends JAXBType {
    public ReflectJAXBType(ITypeModel model, JAXBRegistry r) {
        super(model, r);
    }
}
