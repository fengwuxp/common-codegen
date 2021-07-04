package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxp
 */
public class DispatchCodeGenElementMatcher implements CodeGenElementMatcher<Object> {

    private static final DispatchCodeGenElementMatcher INSTANCE = new DispatchCodeGenElementMatcher();

    private final Map<Class<?>, CodeGenElementMatcher<Object>> elementMatchers;

    public DispatchCodeGenElementMatcher() {
        this.elementMatchers = new HashMap<>();
    }

    public static DispatchCodeGenElementMatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean matches(Object source) {
        return elementMatchers.get(source.getClass()).matches(source);
    }

    @SuppressWarnings("rawtypes")
    public void addCodeGenElementMatcher(Class<?> clazz, CodeGenElementMatcher codeGenElementMatcher) {
        elementMatchers.put(clazz, codeGenElementMatcher);
    }
}
