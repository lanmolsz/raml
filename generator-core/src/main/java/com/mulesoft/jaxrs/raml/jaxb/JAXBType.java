package com.mulesoft.jaxrs.raml.jaxb;

import com.mulesoft.jaxrs.raml.annotation.model.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.*;

public class JAXBType extends JAXBModelElement {
    protected String className;
    protected String genericName;
    protected JAXBType superClass;
    protected ArrayList<JAXBProperty> properties = new ArrayList<JAXBProperty>();
    protected HashMap<String, String> namespaces = new HashMap<String, String>();


    public String getXMLName() {
        return elementName;
    }

    public String getClassName() {
        return className;
    }

    public String getGenericName() {
        return genericName;
    }

    public ITypeModel originalModel(){
        return (ITypeModel)originalModel;
    }


    public HashMap<String, String> getNamespaces() {
        return namespaces;
    }

    public ArrayList<JAXBProperty> getAllProperties() {
        LinkedHashMap<String, JAXBProperty> map = new LinkedHashMap<String, JAXBProperty>();
        JAXBType type = this;
        while (type != null) {
            if (type.properties != null) {
                for (JAXBProperty prop : type.properties) {
                    String propName = prop.name();
                    if (!map.containsKey(propName)) {
                        map.put(propName, prop);
                    }
                }
            }
            type = superClass;
        }
        return new ArrayList<JAXBProperty>(map.values());
    }

    public JAXBType(ITypeModel model, JAXBRegistry r) {
        this(model,model,r);
    }

    public JAXBType(ITypeModel model,ITypeModel ownerType, JAXBRegistry r) {
        super(model, ownerType, r);

        this.elementName = model.getName().toLowerCase();
        this.className = model.getFullyQualifiedName();
        this.genericName = model.getGenericName();
        if (this.className.equals("java.lang.Object")) {
            return;
        }

        ITypeModel superClazz = model.getSuperClass();
        if (superClazz != null) {
            superClass = this.registry.getJAXBModel(superClazz);
        }

        fillProperties(model);
        fillNamespaceMap(0,new HashSet<String>());
    }

    private void fillProperties(ITypeModel model) {
        HashMap<String, List<IMethodModel>> methodMapping = new HashMap<String, List<IMethodModel>>();
        IMethodModel[] methods = model.getMethods();
        for (IMethodModel m : methods) {
            String name = m.getName();
            List<IMethodModel> list = methodMapping.get(name);
            if (list == null) {
                list = new ArrayList<IMethodModel>();
                methodMapping.put(name, list);
            }
            list.add(m);
        }

        XmlAccessType type = extractType();
        for (IMethodModel m : methods) {
            if (!needToConsume(type, m)) {
                continue;
            }
            if (m.getReturnedType() != null) {
                String returnType = m.getReturnedType().getGenericName();
                if (!returnType.equals("void") && !returnType.equals("java.lang.Void") && m.getParameters().length == 0) {
                    boolean get = m.getName().startsWith("get");
                    boolean is = m.getName().startsWith("is");
                    if (get || is) {
                        String methodName = get ? m.getName().substring(3) : m.getName().substring(2);
                        if (!methodName.isEmpty()) {
                            List<IMethodModel> setters = methodMapping.get("set" + methodName);
                            IMethodModel setter = getSetterMethod(model, setters, returnType);
                            if (setter != null || m.hasAnnotation(XmlElement.class.getSimpleName())
                                    || m.hasAnnotation(XmlAttribute.class.getSimpleName())
                                    || m.hasAnnotation(XmlAnyAttribute.class.getSimpleName())) {
                                properties.add(createProperty(methodName, m, setter, model));
                            }
                        }
                    }
                }
            }
        }
        for (IFieldModel f : model.getFields()) {
            if (!needToConsume(type, f)) {
                continue;
            }
            properties.add(createProperty(f.getName(), f, null, model));
        }
    }

    private IMethodModel getSetterMethod(ITypeModel model, List<IMethodModel> setters, String qName) {
        if (setters != null) {
            for (IMethodModel setter : setters) {
                ITypeModel rt = setter.getReturnedType();
                if (rt == null || rt.getFullyQualifiedName().equals("void") || rt.getFullyQualifiedName().equals("java.lang.Void")) {
                    IParameterModel[] params = setter.getParameters();
                    if (params != null && params.length == 1) {
                        String paramType = params[0].getParameterActualType();
                        if (paramType.equals(qName)) {
                            return setter;
                        }
                        ITypeModel paramModel = model.resolveClass(paramType);
                        if (paramModel != null) {
                            if (paramModel.getFullyQualifiedName().equals(qName)) {
                                return setter;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean needToConsume(XmlAccessType type, IMember m) {
        if (!m.isStatic() && !m.hasAnnotation(XmlTransient.class.getSimpleName())) {
            if (type == XmlAccessType.PUBLIC_MEMBER && m.isPublic()) {
                return true;
            }
            if (type == XmlAccessType.PROPERTY && m instanceof IMethodModel) {
                return true;
            }
            if (type == XmlAccessType.FIELD && m instanceof IFieldModel) {
                return true;
            }
            boolean isElement = m.hasAnnotation(XmlElement.class.getSimpleName());
            boolean isAttribute = m.hasAnnotation(XmlAttribute.class.getSimpleName());
            boolean isValue = m.hasAnnotation(XmlValue.class.getSimpleName());
            if (isElement || isAttribute || isValue) {
                return true;
            }
        }
        return false;
    }

    private XmlAccessType extractType() {
        String value = super.value(XmlAccessorType.class, "value");
        if (value != null) {
            try {
                int ind = value.lastIndexOf('.');
                if (ind != -1) {
                    return XmlAccessType.valueOf(value.substring(ind + 1));
                }

                return XmlAccessType.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
                return XmlAccessType.NONE;
            }
        }
        return XmlAccessType.PUBLIC_MEMBER;
    }

    private JAXBProperty createProperty(String string, IMember m, IMethodModel setter, ITypeModel ownerType) {
        boolean isNotElement = !m.hasAnnotation(XmlElement.class.getSimpleName());

        boolean isAttribute = m.hasAnnotation(XmlAttribute.class.getSimpleName())
                || m.hasAnnotation(XmlAnyAttribute.class.getSimpleName())
                || m.hasAnnotation(javax.xml.bind.annotation.XmlValue.class.getSimpleName());

        if (isNotElement && isAttribute ) {
            return new JAXBAttributeProperty(m, setter, ownerType, registry, string);
        }
        return new JAXBElementProperty(m, setter, ownerType, registry, string);
    }


    private int fillNamespaceMap(int n, Set<String> processed) {
        for (JAXBProperty p : properties) {
            if (p.namespace != null) {
                namespaces.put(p.namespace, "n" + (n++));
            }
            JAXBType type = p.getType();
            if (type == null) {
                continue;
            }
            String qName = type.className;
            if (!processed.contains(qName)) {
                processed.add(qName);
                if (type != null) {
                    n = type.fillNamespaceMap(n, processed);
                }
                processed.remove(qName);
            }
        }
        return n;
    }



}
