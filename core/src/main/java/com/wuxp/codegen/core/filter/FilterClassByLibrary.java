package com.wuxp.codegen.core.filter;

import com.wuxp.codegen.core.CodeGenFilter;
import com.wuxp.codegen.model.utils.JavaTypeUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 过滤library的依赖
 */
public class FilterClassByLibrary implements CodeGenFilter<Class<?>> {

    public static final List<String> IGNORE_PACKAGE_LIST = new ArrayList<>();

    //忽略处理的类
//    public static final Set<Class<?>> IGNORE_CLASSES = new HashSet<>();

    static {
        IGNORE_PACKAGE_LIST.add("org.springframework");
        IGNORE_PACKAGE_LIST.add("org.slf4j.");
        IGNORE_PACKAGE_LIST.add("lombok.");
        IGNORE_PACKAGE_LIST.add("javax.persistence.");
        IGNORE_PACKAGE_LIST.add("javax.servlet.");
//        IGNORE_PACKAGE_LIST.add("java.util.");
//        IGNORE_PACKAGE_LIST.add("java.net.");
        IGNORE_PACKAGE_LIST.add("sun.");
    }

    @Override
    public boolean filter(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        //不在忽略列表里面则返回true
        return IGNORE_PACKAGE_LIST.stream().noneMatch(name -> (clazz.getName().startsWith(name)|| clazz.getName().equals(name)));
    }
}
