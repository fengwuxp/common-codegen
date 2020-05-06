package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 匹配时跳过指定的类
 * {@link CodeGenMatcher}
 * {@link IgnoreClassCodeGenMatcher#ignoreClasses}
 * @author wxup
 */
@Slf4j
public class IgnoreClassCodeGenMatcher implements CodeGenMatcher {


    private final List<Class<?>> ignoreClasses;


    public IgnoreClassCodeGenMatcher(Class<?>[] ignoreClasses) {
        this.ignoreClasses = ignoreClasses == null ? Collections.EMPTY_LIST : Arrays.asList(ignoreClasses);

    }

    @Override
    public boolean match(Class<?> clazz) {

        return !this.ignoreClasses.contains(clazz);
    }
}
