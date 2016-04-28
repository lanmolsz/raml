package com.mulesoft.jaxrs.raml.jaxb;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;

import com.mulesoft.jaxrs.raml.annotation.model.*;

/**
 * <p>Abstract JAXBProperty class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public abstract class JAXBProperty extends JAXBModelElement {

    String propertyName;
    boolean required;
    private boolean isCollection;
    private boolean isMap;
    protected IMethodModel setter;
    protected ITypeModel type;

    /**
     * <p>Constructor for JAXBProperty.</p>
     *
     * @param model a {@link com.mulesoft.jaxrs.raml.annotation.model.IBasicModel} object.
     * @param r     a {@link com.mulesoft.jaxrs.raml.jaxb.JAXBRegistry} object.
     * @param name  a {@link java.lang.String} object.
     */
    public JAXBProperty(IMember model, IMethodModel setter, ITypeModel ownerType, JAXBRegistry r, String name) {
        super(model, ownerType, r);
        this.propertyName = name;
        if(model instanceof IFieldModel) {
            IFieldModel field = (IFieldModel) model;
            this.type = field.getType();
            this.isCollection = field.getType().isCollection();
            this.isMap = field.getType().isMap();
        } else if(model instanceof IMethodModel){
            IMethodModel method = (IMethodModel) model;
            this.type = method.getReturnedType();
            this.isCollection = method.getReturnedType().isCollection();
            this.isMap = method.getReturnedType().isMap();
        }
        this.setter = setter;
        IAnnotationModel annotation = model.getAnnotation(getPropertyAnnotation());
        if (annotation != null) {
            String value = annotation.getValue("required");
            if (value != null && value.equals("true")) {
                this.required = true;
            }
        }
        if (setter != null) {
            IAnnotationModel[] setterAnnotations = setter.getAnnotations();
            if (setterAnnotations != null) {
                this.annotations.addAll(Arrays.asList(setterAnnotations));
            }
        }
    }

    /**
     * <p>getPropertyAnnotation.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    protected abstract String getPropertyAnnotation();

    JAXBType getType() {
        return registry.getJAXBModel(type,ownerType);
    }

    /**
     * <p>name.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String name() {
        return elementName != null ? elementName : propertyName;
    }

    /**
     * <p>asJavaType.</p>
     *
     * @return a {@link java.lang.Class} object.
     */
    public Class<?> asJavaType() {
        return type == null ? null : type.getActualClass();
    }

    public StructureType getStructureType() {
        if (isCollection) {
            return StructureType.COLLECTION;
        } else if (isMap) {
            return StructureType.MAP;
        } else {
            return StructureType.COMMON;
        }
    }

    @Override
    public String value(Class<? extends Annotation> cl, String name) {
        String superValue = super.value(cl, name);
        if (superValue != null) {
            return superValue;
        }
        if (this.setter != null) {
            IAnnotationModel annotation = originalModel.getAnnotation(cl.getSimpleName());
            if (annotation != null) {
                annotation = setter.getAnnotation(cl.getSimpleName());
                if (annotation != null) {
                    return annotation.getValue(name);
                }
            }
        }
        return null;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public ITypeModel getOwnerType(){
        return ownerType;
    }
}
