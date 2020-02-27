package com.wuxp.codegen.core.parser;


import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

/**
 * 语言解析器
 *
 * @param <C>
 */
public interface LanguageParser<C extends CommonCodeGenClassMeta> extends GenericParser<C, Class<?>> {


    <M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta> LanguageMetaInstanceFactory<C, M, F> getLanguageMetaInstanceFactory();


    /**
     * 添加匹配器
     * @param codeGenMatchers
     */
    void addCodeGenMatchers(CodeGenMatcher... codeGenMatchers);

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


