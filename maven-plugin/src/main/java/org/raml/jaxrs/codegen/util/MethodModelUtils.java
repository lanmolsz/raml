package org.raml.jaxrs.codegen.util;

import com.mulesoft.jaxrs.raml.annotation.model.IMethodModel;
import com.mulesoft.jaxrs.raml.annotation.model.IParameterModel;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by E355 on 2016/4/20.
 */
public class MethodModelUtils {

    public static IMethodModel[] getRestMethods(ITypeModel type) {
        Map<String,IMethodModel> methodModelMap = new HashMap<String,IMethodModel>();
        for(ITypeModel t = type; t!=null ; t = t.getSuperClass()){
            IMethodModel[] methods = type.getMethods();

            for (IMethodModel method : methods) {
                if(method.hasAnnotation("GET")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("POST")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("PUT")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("DELETE")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("HEAD")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("OPTIONS")) {
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("TRACE")) {//目前并没有这个注解
                    methodModelMap.put(getKey(method),method);
                } else if(method.hasAnnotation("PATCH")) {//目前并没有这个注解
                    methodModelMap.put(getKey(method),method);
                } else if(StringUtils.isNotBlank(method.getAnnotationValue("HttpMethod"))) {
                    methodModelMap.put(getKey(method),method);
                }
            }
        }
        return methodModelMap.values().toArray(new IMethodModel[0]);
    }

    private static String getKey(IMethodModel method) {
        StringBuilder bld = new StringBuilder(method.getName());
        for(IParameterModel param : method.getParameters()){
            bld.append(";").append(param.getParameterType());
        }
        return bld.toString();
    }
}
