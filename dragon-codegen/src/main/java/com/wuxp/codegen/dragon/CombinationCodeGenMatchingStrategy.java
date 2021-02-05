package com.wuxp.codegen.dragon;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 组合的匹配模式
 *
 * @author wuxp
 */
public class CombinationCodeGenMatchingStrategy implements CodeGenMatchingStrategy {

    private final List<CodeGenMatchingStrategy> matchingStrategies;

    public static CombinationCodeGenMatchingStrategy of(CodeGenMatchingStrategy... matchingStrategies) {
        return new CombinationCodeGenMatchingStrategy(Arrays.asList(matchingStrategies));
    }

    public static CombinationCodeGenMatchingStrategy of(Collection<CodeGenMatchingStrategy> matchingStrategies) {
        return new CombinationCodeGenMatchingStrategy(matchingStrategies);
    }


    private CombinationCodeGenMatchingStrategy(Collection<CodeGenMatchingStrategy> matchingStrategies) {
        this.matchingStrategies = new ArrayList<>(matchingStrategies);
    }

    @Override
    public boolean isMatchClazz(JavaClassMeta classMeta) {
        return matchingStrategies.stream()
                .allMatch(codeGenMatchingStrategy -> codeGenMatchingStrategy.isMatchClazz(classMeta));
    }

    @Override
    public boolean isMatchMethod(JavaMethodMeta methodMeta) {
        return matchingStrategies.stream()
                .allMatch(codeGenMatchingStrategy -> codeGenMatchingStrategy.isMatchMethod(methodMeta));
    }

    @Override
    public boolean isMatchField(JavaFieldMeta javaFieldMeta) {
        return matchingStrategies.stream()
                .allMatch(codeGenMatchingStrategy -> codeGenMatchingStrategy.isMatchField(javaFieldMeta));
    }

    @Override
    public boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter) {
        return matchingStrategies.stream()
                .allMatch(codeGenMatchingStrategy -> codeGenMatchingStrategy.isMatchParameter(javaMethodMeta, parameter));
    }
}
