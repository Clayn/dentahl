package de.clayntech.dentahl4j.tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reflections {

    private static final Logger LOG= LoggerFactory.getLogger(Reflections.class);
    public static Method findMethod(Class<?> cls,String name, Class<?> returnType, Class<?> ...parameters) {
        Method back=null;
        List<Method> methods=new ArrayList<>();
        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
        methods.addAll(Arrays.asList(cls.getMethods()));
        for(Method m:methods) {
            if(m.getName().equals(name)&&m.getReturnType().equals(returnType)) {
                if(m.getParameterTypes().length>0&&parameters.length>0) {
                    if(Arrays.equals(m.getParameterTypes(),parameters)) {
                        back=m;
                        break;
                    }
                }
                else {
                    back=m;
                    break;
                }
            }
        }
        if(back==null&&!cls.getSuperclass().equals(Object.class)) {
            back=findMethod(cls.getSuperclass(),name,returnType,parameters);
        }
        return back;
    }
}
