package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * 根据包名进行匹配
 */
@Slf4j
public class PackageNameCodeGenMatcher implements CodeGenMatcher {

    /**
     * 需要忽略的包名列表
     */
    public static final List<String> IGNORE_PACKAGE_LIST = new ArrayList<>();

    /**
     * 需要导入的包
     */
    public static final List<String> INCLUDE_PACKAGE_LIST = new ArrayList<>();


    static {
        IGNORE_PACKAGE_LIST.add("org.springframework.");
        IGNORE_PACKAGE_LIST.add("org.slf4j.");
        IGNORE_PACKAGE_LIST.add("org.apache.");
        IGNORE_PACKAGE_LIST.add("org.freemarker.");
        IGNORE_PACKAGE_LIST.add("org.hibernate.");
        IGNORE_PACKAGE_LIST.add("org.jetbrains.");
        IGNORE_PACKAGE_LIST.add("org.jodd.");
        IGNORE_PACKAGE_LIST.add("lombok.");
        IGNORE_PACKAGE_LIST.add("javax.persistence.");
        IGNORE_PACKAGE_LIST.add("javax.servlet.");
        IGNORE_PACKAGE_LIST.add("java.security.");
        IGNORE_PACKAGE_LIST.add("java.text.");
        IGNORE_PACKAGE_LIST.add("java.io.");
//        IGNORE_PACKAGE_LIST.add("java.util.");
        IGNORE_PACKAGE_LIST.add("sun.");
        IGNORE_PACKAGE_LIST.add("com.google.");
        IGNORE_PACKAGE_LIST.add("com.alibaba.");
        IGNORE_PACKAGE_LIST.add("com.alipay.");
        IGNORE_PACKAGE_LIST.add("com.baidu.");
        IGNORE_PACKAGE_LIST.add("com.github.");
//        IGNORE_PACKAGE_LIST.add("com.wuxp.codegen.model");

        //文件上传
        INCLUDE_PACKAGE_LIST.add("org.springframework.web.multipart.commons.CommonsMultipartFile");
    }

    @Override
    public boolean match(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        //在包含列表里面
        boolean anyMatch = INCLUDE_PACKAGE_LIST.stream().anyMatch(name -> (clazz.getName().startsWith(name) || clazz.getName().equals(name)));
        if (anyMatch) {
            return true;
        }

        //不在忽略列表里面则返回true
        return IGNORE_PACKAGE_LIST.stream().noneMatch(name -> (clazz.getName().startsWith(name) || clazz.getName().equals(name)));
    }
}
