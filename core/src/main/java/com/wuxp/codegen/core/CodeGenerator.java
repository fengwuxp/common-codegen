package com.wuxp.codegen.core;


/**
 * 负责生成最终的代码
 * <p>
 * 通过包扫描得到的{@link java.lang.Class} 对象通过{@link com.wuxp.codegen.core.parser.JavaClassParser}和
 * {@link com.wuxp.codegen.core.parser.LanguageParser}转换为最终用于生成代码的元数据对象，最终通过{@link com.wuxp.codegen.core.strategy.TemplateStrategy#build(Object)}
 * 输出生成的代码
 * </p>
 *
 * @author wxup
 */
public interface CodeGenerator {

    /**
     * 生成
     */
    void generate();
}
