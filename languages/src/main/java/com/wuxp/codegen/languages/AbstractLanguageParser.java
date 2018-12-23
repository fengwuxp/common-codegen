package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;


/**
 * 抽象的语言解释器
 * @param <C>   类
 * @param <M>   方法
 * @param <F>   属性
 */
@Slf4j
public abstract class AbstractLanguageParser<C, M, F> implements GenericParser<C, JavaClassMeta> {

    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(true);


    /**
     * 转换属性列表
     *
     * @param javaFieldMetas
     * @return
     */
    protected abstract F[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas);

    /**
     * 转换方法列表
     *
     * @param javaMethodMetas
     * @return
     */
    protected abstract M[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas);


    /**
     * 抓取依赖列表
     *
     * @param classes
     * @return
     */
    protected abstract Set<C> fetchDependencies(Set<Class<?>> classes);

    /**
     * 解析超类
     *
     * @param clazz
     * @return
     */
    protected C parseSupper(Class<?> clazz) {

        return this.parse(this.javaParser.parse(clazz));
    }
}
