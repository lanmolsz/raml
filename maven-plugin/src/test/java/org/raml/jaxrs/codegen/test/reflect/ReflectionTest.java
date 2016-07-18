package org.raml.jaxrs.codegen.test.reflect;

import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/4/20.
 */
public class ReflectionTest {
    @Test
    public void testArray() throws ClassNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        String className = new String[10].getClass().getName();
        System.out.println(className);
        Class clazz = Class.forName(className);
        System.out.println(clazz == classLoader.loadClass("java.lang.String[]"));
        String[] array = (String[])Array.newInstance(String.class, 10);
        System.out.println(array.length);
    }

    @Test
    public void testField() throws Exception {

        Field field = MediaType.class.getDeclaredField("APPLICATION_JSON");
        System.out.println(field.get(null));
    }
}
