/*
 * Copyright 2013 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.jaxrs.codegen.spoon;

import com.mulesoft.jaxrs.raml.annotation.model.*;
import org.raml.jaxrs.codegen.maven.GenericTypeModelRegistry;
import org.raml.jaxrs.codegen.maven.MetadataModelRegistry;
import org.raml.jaxrs.codegen.maven.TypeModelRegistry;
import org.raml.jaxrs.codegen.model.*;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <p>SpoonProcessor class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class SpoonProcessor {

    private static final String API_OPERATION = "ApiOperation";

    private static final String SWAGGER_API = "Api";

    private static final String JAVAX_CONSUMES = "Consumes";

    private GenericTypeModelRegistry genericRegistry = new GenericTypeModelRegistry();

    private Factory factory;

    public SpoonProcessor(Factory factory) {
        this.factory = factory;
    }

    public TypeModelRegistry getRegistry() {
        return genericRegistry;
    }


    private boolean isIgnoreType(String paramType) {
        if(ignoreClasses.contains(paramType)){
            return true;
        }
        for(String ignorePackage : ignorePackages) {
            if(paramType.startsWith(ignorePackage)) {
                return true;
            }
        }
        return false;
    }

    private static HashSet<String> ignoreClasses = new HashSet<String>(Arrays.asList(
            "byte", "java.lang.Byte",
            "short", "java.lang.Short",
            "int", "java.lang.Integer",
            "long", "java.lang.Long",
            "float", "java.lang.Float",
            "double", "java.lang.Double",
            "character", "java.lang.Character",
            "boolean", "java.lang.Boolean",
            "java.math.BigDecimal","java.math.BigInteger",
            "java.lang.String","java.lang.Object",
            "java.lang.Thread","java.lang.ThreadLocal"
    ));


    private List<String> ignorePackages = new ArrayList<String>(Arrays.asList(
            "javax.servlet","javax.ws.rs","org.apache.cxf.jaxrs"
    ));

    private final ITypeModel OBJECT_MODEL = new TypeModel(genericRegistry,Object.class,"java.lang.Object");

    /**
     * <p>process.</p>
     *
     * @param packages a {@link Collection} object.
     */
    public void process(Collection<CtPackage> packages,List<String> ignorePackages){
        if(packages==null){
            return;
        }
        this.ignorePackages.addAll(ignorePackages);
        MetadataModelRegistry.registerSpoon(this);
        for(CtPackage pack : packages){
            processPackage(pack);
        }
    }

    private void processPackage(CtPackage pack) {
        Set<CtPackage> subPackages = pack.getPackages();
        if(subPackages!=null){
            for(CtPackage subPackage:subPackages){
                processPackage(subPackage);
            }
        }
        for(CtType<?> type : pack.getTypes()){
            ITypeModel typeModel = processType(type);
            genericRegistry.registerTargetType(typeModel);
            MetadataModelRegistry.register(type);
        }
    }



    public ITypeModel processType(CtType<?> classElement)
    {
        String genericName = getGenericName(classElement);
        TypeModel type = (TypeModel) genericRegistry.getType(genericName);
        if(type!=null){
            return type;
        }

        type = new TypeModel(genericRegistry,classElement.getActualClass(),genericName);
        fillBasic(type,classElement);

        CtTypeReference<?> superClass = classElement.getSuperclass();
        if(superClass!=null){
            type.setSuperClass(processTypeReference(superClass));
        }
        Set<CtTypeReference<?>> interfaces = classElement.getSuperInterfaces();
        if(interfaces!=null && interfaces.size() > 0){
            for(CtTypeReference<?> ref: interfaces){
                type.addImplementedInterface(this.processTypeReference(ref));
            }
        }

        Set<CtMethod<?>> methods = classElement.getMethods();
        for(CtMethod<?> m : methods){
            type.addMethod(processMethod(m,type));
        }

        Collection<CtField<?>> fields = classElement.getFields();
        for(CtField<?> m : fields){
            type.addField(processField(m,type));
        }

        return type;
    }

    private String getGenericName(CtType<?> classElement){
        String qualifiedName = classElement.getQualifiedName();
        List<CtTypeReference<?>> ftp = classElement.getFormalTypeParameters();
        if(ftp==null||ftp.isEmpty()){
            return qualifiedName;
        }
        StringBuilder genericName = new StringBuilder(qualifiedName).append("<");
        for( int i = 0; i < ftp.size(); i++ ){
            genericName.append(ftp.get(i).toString());
            if(i > 0) {
                genericName.append(",");
            }
        }
        return genericName.append(">").toString();
    }

    private void fillBasic(BasicModel model, CtNamedElement element) {
        String simpleName = element.getSimpleName();
        String docComment = element.getDocComment();

        model.setName(simpleName);
        model.setDocumentation(docComment);
        if (element instanceof CtModifiable) {
            CtModifiable ctModifiable = (CtModifiable) element;
            Set<ModifierKind> modifiers = ctModifiable.getModifiers();
            for (ModifierKind mod : modifiers) {
                if (mod == ModifierKind.STATIC) {
                    model.setStatic(true);
                }
                if (mod == ModifierKind.PUBLIC) {
                    model.setPublic(true);
                }
            }
        }

        for(CtAnnotation<? extends Annotation> a : element.getAnnotations() ){
            model.addAnnotation(transformAnnotation(a));
        }
    }


    private IMethodModel processMethod(CtMethod<?> m, TypeModel declareType) {

        MethodModel method = new MethodModel();
        fillBasic(method, m);
        fillType(declareType, method, m.getType());

        List<CtParameter<?>> parameters = m.getParameters();
        for(CtParameter<?> p : parameters){
            method.addParameter(processParameter(declareType,p));
        }

        return method;
    }

    private IFieldModel processField(CtField<?> m, TypeModel declareType) {
        FieldModel field = new FieldModel();
        fillBasic(field , m);
        fillType(declareType, field, m.getType());
        return field;
    }

    private IParameterModel processParameter(TypeModel declareType, CtParameter<?> paramElement) {

        ParameterModel param = new ParameterModel();

        CtTypeReference<?> paramType = paramElement.getType();
        String qualifiedName = paramType.getQualifiedName();
        param.setType(qualifiedName);
        param.setActualType(paramType.toString());
        param.setRequired(paramType.isPrimitive());

        fillBasic(param, paramElement);
        if(param.hasAnnotation("Context")){
            return param;
        }
        fillType(declareType,param,paramType);
        return param;
    }

    private ITypeModel processTypeReference(CtTypeReference<?> typeReference) {
        String qualifiedName = typeReference.getQualifiedName();
        String genericName = typeReference.toString();
        ITypeModel reference = genericRegistry.getType(genericName);
        if(reference != null){
            return reference;
        }
        if(genericName.equals(typeReference.getQualifiedName())) {
            ITypeModel template = genericRegistry.getType(qualifiedName);
            if(template == null) {
                CtType<Object> ctType = factory.Class().get(qualifiedName);
                if(ctType == null) {
                    ctType = factory.Type().get(qualifiedName);
                }
                if(ctType!=null){
                    template = processType(ctType);
                }
            }
            if(template != null) {
                return template;
            }
        }

        TypeModel type  = newTypeModel(typeReference);

        if(isIgnoreType(genericName) || typeReference.getActualClass() == Object.class) {
            return type;
        }

        if(type instanceof CollectionTypeModel) {
            CollectionTypeModel collection = (CollectionTypeModel) type;
            if(typeReference.getActualTypeArguments().size() == 1) {
                CtTypeReference typeReference1 = typeReference.getActualTypeArguments().get(0);
                collection.setElementTypeModel(processTypeReference(typeReference1));
            } else if(typeReference.getActualClass().isArray()){
                CtArrayTypeReference arrayTypeReference = (CtArrayTypeReference)typeReference;
                collection.setElementTypeModel(processTypeReference(arrayTypeReference.getComponentType()));
            } else {
                collection.setElementTypeModel(OBJECT_MODEL);
            }
            return collection;
        }

        if(type instanceof MapTypeModel) {
            MapTypeModel map = (MapTypeModel)type;
            if(typeReference.getActualTypeArguments().size() == 2) {
                CtTypeReference keyType = typeReference.getActualTypeArguments().get(0);
                CtTypeReference valueType = typeReference.getActualTypeArguments().get(1);
                map.setKeyType(processTypeReference(keyType));
                map.setValueType(processTypeReference(valueType));
            } else {
                map.setKeyType(OBJECT_MODEL);
                map.setValueType(OBJECT_MODEL);
            }
            return map;
        }

        Map<CtTypeReference<?>,CtTypeReference<?>> mapping = getTypeMapping(typeReference);

        Collection<CtFieldReference<?>> fields = typeReference.getDeclaredFields();
        for(CtFieldReference<?> m : fields){
            type.addField(processFieldReference(type,m,mapping));
        }

        Collection<CtExecutableReference<?>> methods = typeReference.getDeclaredExecutables();
        for(CtExecutableReference<?> m : methods){
            type.addMethod(processMethodReference(type,m,mapping));
        }
        return type;
    }

    private TypeModel newTypeModel(CtTypeReference<?> typeReference) {
        TypeModel type;
        Class<?> actualClass = typeReference.getActualClass();
        if(actualClass != null && (Collection.class.isAssignableFrom(actualClass) || actualClass.isArray())) {
            type = new CollectionTypeModel(genericRegistry,actualClass,typeReference.toString());
        } else if(actualClass != null &&  Map.class.isAssignableFrom(actualClass)) {
            type = new MapTypeModel(genericRegistry,actualClass,typeReference.toString());
        } else {
            type = new TypeModel(genericRegistry,actualClass,typeReference.toString());
        }
        fillReference(type, typeReference);
        return type;
    }

    private IMethodModel processMethodReference(TypeModel declareType,CtExecutableReference<?> m,Map<CtTypeReference<?>,CtTypeReference<?>> mapping) {

        MethodModel method = new MethodModel();
        fillReference(method,m);
        List<CtTypeReference<?>> parameters = m.getParameters();
        Method actualMethod = m.getActualMethod();
        if(parameters.isEmpty()) {
            parameters = m.getActualTypeArguments();
        }
        for(CtTypeReference<?> p : parameters){
            method.addParameter(processParameterReference(p,mapping));
        }
        if(actualMethod!=null){
            fillType(declareType, method, getActualType(mapping,m.getType()));
            adjustModifiers(method, actualMethod);
        }
        return method;
    }

    private IParameterModel processParameterReference(CtTypeReference<?> paramTypeReference,Map<CtTypeReference<?>,CtTypeReference<?>> mapping) {

        ParameterModel parameterModel = new ParameterModel();
        CtTypeReference<?> actualType = getActualType(mapping,paramTypeReference);
        parameterModel.setType(paramTypeReference.getQualifiedName());
        parameterModel.setActualType(actualType.toString());
        parameterModel.setRequired(paramTypeReference.isPrimitive());
        parameterModel.setName(actualType.getSimpleName());

        if(isNotGenericType(actualType)) {
            fillReference(parameterModel,actualType);
        }
        return parameterModel;
    }

    private void fillReference( BasicModel model, CtReference ref) {
        model.setName(ref.getSimpleName());
        try{
            if(ref.getDeclaration() !=null) {
                List<CtAnnotation<? extends Annotation>> annotations = ref.getDeclaration().getAnnotations();
                if(annotations!=null){
                    for(CtAnnotation a : annotations){
                        model.addAnnotation(transformAnnotation(a));
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    private IFieldModel processFieldReference(TypeModel declareType,CtFieldReference<?> m,Map<CtTypeReference<?>,CtTypeReference<?>> mapping) {
        FieldModel fm=new FieldModel();
        fillReference(fm, m);
        Member actualField = m.getActualField();
        if(actualField!=null){
            fillType(declareType, fm,getActualType(mapping, m.getType()));
            adjustModifiers(fm, actualField);
        }
        return fm;
    }

    private void fillType(TypeModel declareType, BasicModel fm, CtTypeReference<?> type) {
        if(type==null){
            return;
        }
        ITypeModel processedType = processTypeReference(type);
        fm.setJavaClass(type.getActualClass());
        fm.setType(processedType);
        if(fm instanceof MethodModel) {
            MethodModel method = (MethodModel) fm;
            method.setReturnedType(processedType);
            adjustBodyType(method,hasGlobalConsumes(declareType));
        }
    }


    private void adjustModifiers(BasicModel model, Member member) {
        int modifiers = member.getModifiers();
        if(Modifier.isPublic(modifiers)){
            model.setPublic(true);
        }
        if(Modifier.isStatic(modifiers)){
            model.setStatic(true);
        }
    }

    private Map<CtTypeReference<?>,CtTypeReference<?>> getTypeMapping(CtTypeReference<?> typeReference) {
        Map<CtTypeReference<?>,CtTypeReference<?>> mapping = new HashMap<CtTypeReference<?>, CtTypeReference<?>>();
        List<CtTypeReference<?>> actualType = typeReference.getActualTypeArguments();
        if(actualType==null || actualType.size() == 0) {
            return mapping;
        }
        CtType<Object> ctType = factory.Class().get(typeReference.getQualifiedName());
        if(ctType == null) {
            ctType = factory.Type().get(typeReference.getQualifiedName());
        }
        if(ctType != null) {
            List<CtTypeReference<?>> ftp = ctType.getFormalTypeParameters();
            if(ftp != null && ftp.size() == actualType.size()) {
                for (int i = 0;i<ftp.size();i++) {
                    mapping.put(ftp.get(i),actualType.get(i));
                }
            }
        }
        return mapping;
    }

    private CtTypeReference<?> getActualType(Map<CtTypeReference<?>,CtTypeReference<?>> mapping,CtTypeReference<?> reference) {
        Class<?> actualClass = reference.getActualClass();
        if(actualClass == null) {
            return reference;
        }
        if(actualClass.getName().equals(reference.getQualifiedName())) {
            return reference;
        }
        if(mapping.containsKey(reference)) {
            return mapping.get(reference);
        }
        return reference;
    }

    private boolean isNotGenericType(CtTypeReference<?> reference) {
        Class<?> actualClass = reference.getActualClass();
        if(actualClass == null) {
            return false;
        }
        return actualClass.getName().equals(reference.getQualifiedName());
    }


    private boolean hasGlobalConsumes(ITypeModel type) {
        if(type.hasAnnotation(JAVAX_CONSUMES)){
            return true;
        }
        IAnnotationModel apiAnn = type.getAnnotation(SWAGGER_API);
        if(apiAnn==null){
            return false;
        }
        String consumes = apiAnn.getValue(JAVAX_CONSUMES.toLowerCase());
        if(consumes!=null){
            return true;
        }
        return false;
    }

    private void adjustBodyType(IMethodModel m, boolean hasGlobalConsumes) {

        MethodModel method = (MethodModel) m;

        boolean hasConsumes = hasGlobalConsumes;

        IAnnotationModel apiOperation = method.getAnnotation(API_OPERATION);
        if(apiOperation!=null){
            IAnnotationModel[] subAnn = apiOperation.getSubAnnotations(JAVAX_CONSUMES.toLowerCase());
            if(subAnn!=null){
                hasConsumes = true;
            }
        }

        IAnnotationModel consumes = method.getAnnotation(JAVAX_CONSUMES);
        if(consumes!=null){
            hasConsumes = true;
        }
        if(!hasConsumes){
            return;
        }

        IParameterModel[] parameters = method.getParameters();
        for(IParameterModel param : parameters){
            String paramType = param.getParameterActualType();

            if (isIgnoreType(paramType)) {
                continue;
            }
            if(param.hasAnnotation("QueryParam")){
                continue;
            }
            if(param.hasAnnotation("HeaderParam")){
                continue;
            }
            if(param.hasAnnotation("PathParam")){
                continue;
            }
            if(param.hasAnnotation("FormParam")){
                continue;
            }
            if(param.hasAnnotation("Context")){
                continue;
            }

            ITypeModel type = genericRegistry.getType(paramType);
            if(type==null){
                continue;
            }
            method.setBodyType(type);
            break;
        }
    }

    private IAnnotationModel transformAnnotation(CtAnnotation<? extends Annotation> annotation) {
        return new com.mulesoft.jaxrs.raml.annotation.model.reflection.AnnotationModel(annotation.getActualAnnotation());
    }
}
