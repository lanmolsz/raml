package com.mulesoft.jaxrs.raml.jaxb;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.mulesoft.jaxrs.raml.annotation.model.IMember;
import com.mulesoft.jaxrs.raml.annotation.model.IMethodModel;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;
import com.mulesoft.jaxrs.raml.annotation.model.reflection.ReflectionType;

/**
 * <p>JAXBElementProperty class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class JAXBElementProperty extends JAXBProperty {

    /**
     * <p>Constructor for JAXBElementProperty.</p>
     *
     * @param model a {@link com.mulesoft.jaxrs.raml.annotation.model.IBasicModel} object.
     * @param r     a {@link com.mulesoft.jaxrs.raml.jaxb.JAXBRegistry} object.
     * @param name  a {@link java.lang.String} object.
     */
    public JAXBElementProperty(IMember model, IMethodModel setter, ITypeModel ownerType, JAXBRegistry r, String name) {
        super(model, setter, ownerType, r, name);
    }

    /**
     * <p>asJavaType.</p>
     *
     * @return a {@link java.lang.Class} object.
     */
    public Class<?> asJavaType() {
        if (originalModel instanceof IMember) {
            IMember or = (IMember) originalModel;
            return or.getJavaType();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPropertyAnnotation() {
        return XmlElement.class.getSimpleName();
    }

}
