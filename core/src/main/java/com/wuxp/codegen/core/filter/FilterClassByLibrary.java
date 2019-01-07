package com.wuxp.codegen.core.filter;

import com.wuxp.codegen.core.CodeGenFilter;
import com.wuxp.codegen.model.utils.JavaTypeUtil;

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
        packageNames.add("java.");
    }

    @Override
    public boolean filter(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        if (!JavaTypeUtil.isComplex(clazz) || clazz.isAnnotation()) {
            return false;
        }
        return packageNames.stream().noneMatch(name -> clazz.getName().startsWith(name));
    }
}
