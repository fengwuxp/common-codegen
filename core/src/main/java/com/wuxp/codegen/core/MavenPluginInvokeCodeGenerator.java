package com.wuxp.codegen.core;

import java.util.List;

/**
 * 用于maven插件在执行调用的生成器，该类的实现类必须存在一个空构造用于插件调用是进行反射创建
 * <p>
 * 建议将该类的实现放在test source目录
 * 该类在需要自定义sdk的生成实现时进行使用
 * </p>
 *
 * @author wuxp
 */
public interface MavenPluginInvokeCodeGenerator {

    /**
     * 生成代码
     *
     * @param output 输出目录
     * @param types  client type，如果为null表示生成所有
     */
    void generate(String output, List<ClientProviderType> types);
}
