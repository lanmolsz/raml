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
        return Class.forName(className);
    }
}
