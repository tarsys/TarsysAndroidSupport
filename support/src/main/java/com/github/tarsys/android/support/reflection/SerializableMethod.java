package com.github.tarsys.android.support.reflection;

import com.annimon.stream.Stream;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by tarsys on 23/4/16.
 */
public class SerializableMethod implements Serializable {
    private String returnType;
    private String className;
    private String methodName;

    public SerializableMethod(Method method){
        this.returnType = method.getReturnType().getCanonicalName();
        this.className = method.getDeclaringClass().getCanonicalName();
        this.methodName = method.getName();
    }

    public Method getMethod(){
        Method returnValue = null;

        try {
            Class<?> classMethod = ClassLoader.getSystemClassLoader().loadClass(this.className);
            returnValue = classMethod.getMethod(this.methodName);

        } catch (ClassNotFoundException e) {
            returnValue = null;
        } catch (NoSuchMethodException e) {
            returnValue = null;
        }

        return returnValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString(){
        Method method = this.getMethod();


        return method != null ? method.toString() : "";
    }
}
