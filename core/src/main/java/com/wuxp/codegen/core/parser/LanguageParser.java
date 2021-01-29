package com.wuxp.codegen.core.parser;


import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

import java.util.List;

/**
 * 用于将{@link java.lang.Class}对象转换为不同语言用于生成代码的元数据描述对象的解析器
 *
 * @param <C>
 * @author wxup
 */
public interface LanguageParser<C extends CommonCodeGenClassMeta> extends GenericParser<C, Class<?>> {


    <M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta> LanguageMetaInstanceFactory<C, M, F> getLanguageMetaInstanceFactory();


    /**
     * 添加匹配器
     *
     * @param codeGenMatchers 代码生成匹配器列表
     */
    void addCodeGenMatchers(CodeGenMatcher... codeGenMatchers);

    /**
     * 获取代码匹配器
     *
     * @return 代码生成匹配器列表
     */
    List<CodeGenMatcher> getCodeGenMatchers();

    /**
     * 设置一个语言增强器
     *
     * @param languageEnhancedProcessor 语言增强处理者
     */
    void setLanguageEnhancedProcessor(LanguageEnhancedProcessor languageEnhancedProcessor);

    /**
     * 语言类型的实例工厂
     *
     * @param <C>
     * @param <M>
     * @param <F>
     */
    interface LanguageMetaInstanceFactory<C extends CommonCodeGenClassMeta, M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta> {


        default C newClassInstance() {
            return (C) new CommonCodeGenClassMeta();
        }

        default M newMethodInstance() {
            return (M) new CommonCodeGenMethodMeta();
        }

        default F newFieldInstance() {
            return (F) new CommonCodeGenFiledMeta();
        }

        default C getTypeVariableInstance() {
            return null;
        }

    }


}


