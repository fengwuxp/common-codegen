package com.wuxp.codegen.server.plugins;

import com.wuxp.codegen.core.ClientProviderType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 代码生成插件执行策略
 *
 * @author wuxp
 */
public interface CodegenPluginExecuteStrategy {


    /**
     * 执行代码生成插件
     *
     * @param projectBaseDir 项目根目录 不能为null
     * @param modelName      执行插件的模块名称 可以为null
     * @param type           期望生成的 lib类型 如果指定了生成类型，则替换掉原有的插件配置
     */
    void executeCodegenPlugin(@NotNull String projectBaseDir, String modelName, ClientProviderType type);

    /**
     * 查找需要生成代码的模块文件路径
     *
     * @param projectBaseDir 项目根目录 不能为null
     * @param modelName      执行插件的模块名称 可以为null
     * @return pom.xml文件或者gradle文件路径
     */
    List<String> findModuleFiles(@NotNull String projectBaseDir, String modelName);

}
