package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;

import java.util.List;

/**
 * 解析元数据时的增强处理 增强处理
 *
 * @author wxup
 */
public interface LanguageEnhancedProcessor<C, M, F> {

    LanguageEnhancedProcessor NONE = new NoneLanguageEnhancedProcessor();


    /**
     * 增强处理 class
     *
     * @param methodMeta
     * @param classMeta
     */
    default C enhancedProcessingClass(C methodMeta, JavaClassMeta classMeta) {
        return methodMeta;
    }

    /**
     * 增强处理方法
     *
     * @param methodMeta
     * @param javaMethodMeta
     * @param classMeta
     */
    default M enhancedProcessingMethod(M methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        return methodMeta;
    }


    /**
     * 增强处理 filed
     *
     * @param fieldMeta
     * @param javaFieldMeta
     * @param classMeta
     */
    default F enhancedProcessingField(F fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        return fieldMeta;
    }

    /**
     * 设置匹配器链
     * @param codeGenMatchers
     */
    void setCodeGenMatchers(List<CodeGenMatcher> codeGenMatchers);


    final class NoneLanguageEnhancedProcessor implements LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

        @Override
        public void setCodeGenMatchers(List<CodeGenMatcher> codeGenMatchers) {

        }
    }

}
