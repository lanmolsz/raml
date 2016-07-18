package org.raml.jaxrs.codegen.util;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ClassUtils {

    public static Class<?> loadClass(ClassLoader loader,String className) throws ClassNotFoundException {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return loadClass(className);
        }
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        if(className.equals("boolean")) return boolean.class;
        if(className.equals("byte")) return byte.class;
        if(className.equals("short")) return short.class;
        if(className.equals("int")) return int.class;
        if(className.equals("long")) return long.class;
        if(className.equals("char")) return char.class;
        if(className.equals("float")) return float.class;
        if(className.equals("double")) return double.class;
        if(className.equals("void")) return void.class;
        return Class.forName(className);
    }
}
