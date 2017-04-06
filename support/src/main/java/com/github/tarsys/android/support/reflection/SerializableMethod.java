package com.github.tarsys.android.support.reflection;

import android.content.Context;

import com.annimon.stream.Stream;
import com.github.tarsys.android.support.utilities.AndroidSupport;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.PathClassLoader;

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

    public Method getMethod(Context context){
        Method returnValue = null;

        try {
            PathClassLoader classLoader = Reflection.getClassLoader(context);
            Class<?> classMethod = classLoader.loadClass(this.className);
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
        String returnValue;

        try {
            returnValue = new GsonBuilder().create().toJson(this);
        }catch (Exception ex){
            returnValue = AndroidSupport.EmptyString;
        }

        return returnValue;
    }
}
