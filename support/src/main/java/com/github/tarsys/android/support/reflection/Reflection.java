package com.github.tarsys.android.support.reflection;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;

/**
 * Created by tarsys on 25/4/16.
 */
public class Reflection {

    public static PathClassLoader getClassLoader(Context context){
        PathClassLoader returnValue;

        try{
            String apkName = context.getPackageCodePath();
            returnValue = new PathClassLoader(apkName, Thread.currentThread().getContextClassLoader());
        }catch (Exception ex){
            returnValue = null;
        }

        return returnValue;
    }
    public static ArrayList<Method> getAnnotatedMethods(Class<?> baseClass, Class annotation){
        ArrayList<Method> returnValue = new ArrayList<>();

        ArrayList<Method> tmpReturnValue = new ArrayList<>();
        if (baseClass != null) {
            List<Method> methods = (List<Method>) Stream.of(baseClass.getDeclaredMethods())
                    .filter(m -> m.getAnnotation(annotation) != null)
                    .collect(Collectors.toList());

            if (methods != null) tmpReturnValue.addAll(methods);

            if (baseClass.getSuperclass() != null) {
                tmpReturnValue.addAll(Reflection.getAnnotatedMethods(baseClass.getSuperclass(), annotation));
            }

            returnValue.addAll(Stream.of(tmpReturnValue).distinct().collect(Collectors.toList()));
        }

        return returnValue;
    }
}
