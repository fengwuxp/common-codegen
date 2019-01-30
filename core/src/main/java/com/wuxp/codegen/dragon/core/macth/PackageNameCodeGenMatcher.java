package com.wuxp.codegen.dragon.core.macth;

import com.wuxp.codegen.dragon.core.CodeGenMatcher;
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


    static {
        IGNORE_PACKAGE_LIST.add("org.springframework");
        IGNORE_PACKAGE_LIST.add("org.slf4j.");
        IGNORE_PACKAGE_LIST.add("lombok.");
        IGNORE_PACKAGE_LIST.add("javax.persistence.");
        IGNORE_PACKAGE_LIST.add("javax.servlet.");
        IGNORE_PACKAGE_LIST.add("sun.");
    }

    @Override
    public boolean match(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        //不在忽略列表里面则返回true
        return IGNORE_PACKAGE_LIST.stream().noneMatch(name -> (clazz.getName().startsWith(name) || clazz.getName().equals(name)));
    }
}
