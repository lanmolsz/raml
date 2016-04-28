package org.raml.jaxrs.codegen.maven;

import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import org.raml.jaxrs.codegen.spoon.SpoonProcessor;
import spoon.reflect.declaration.CtType;

import java.util.LinkedHashMap;

/**
 * Created by E355 on 2016/4/21.
 */
public class MetadataModelRegistry {
    private static final LinkedHashMap<Class<?>,CtType<?>> metadataMap = new LinkedHashMap<Class<?>,CtType<?>>();
    private static SpoonProcessor processor;

    public static void registerSpoon(SpoonProcessor processor){
        if(MetadataModelRegistry.processor == null) {
            MetadataModelRegistry.processor = processor;
        }
    }
    public static void register(CtType<?> classElement) {
        metadataMap.put(classElement.getActualClass(),classElement);
    }

    public static ITypeModel getModel(String className){
        try {
            ITypeModel model = processor.getRegistry().getType(className);
            if(model!=null) {
                return model;
            }
            return buildModel(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ITypeModel getModel(Class<?> clazz){
        ITypeModel model = processor.getRegistry().getType(clazz.getName());
        if(model!=null) {
            return model;
        }
        return buildModel(clazz);
    }

    public static ITypeModel buildModel(Class<?> clazz){
        CtType<?> type = metadataMap.get(clazz);
        if(type != null) {
            return processor.processType(type);
        }
        return null;
    }


}
