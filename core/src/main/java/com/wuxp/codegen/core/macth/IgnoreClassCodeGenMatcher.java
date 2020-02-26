package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class IgnoreClassCodeGenMatcher implements CodeGenMatcher {


    private final List<Class<?>> ignoreClass;



    public IgnoreClassCodeGenMatcher(Class<?>[] ignoreClass) {
        this.ignoreClass = ignoreClass == null ? Collections.EMPTY_LIST : Arrays.asList(ignoreClass);

    }

    @Override
    public boolean match(Class<?> clazz) {

        return !this.ignoreClass.contains(clazz);
    }
}
