package com.wuxp.codegen.core.filter;

import com.wuxp.codegen.core.CodeGenFilter;

import java.util.ArrayList;
import java.util.List;


/**
 * 过滤library的依赖
 */
public class FilterClassByLibrary implements CodeGenFilter<Class<?>> {

    public static final List<String> packageNames = new ArrayList<>();

    static {
        packageNames.add("org.springframework");
        packageNames.add("org.slf4j.");
        packageNames.add("lombok.");
//        packageNames.add("javax.persistence.");
        packageNames.add("javax.servlet.");
    }

    @Override
    public boolean filter(Class<?> data) {
        if (data == null) {
            return false;
        }
        return packageNames.stream().filter(name -> data.getName().startsWith(name)).isParallel();
    }
}
