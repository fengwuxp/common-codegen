package com.wuxp.codegen.server.plugins;

import com.wuxp.codegen.core.ClientProviderType;
import org.springframework.util.Assert;

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
     * @return pom.xml 文件或者 gradle 文件路径
     */
    List<String> findModuleFiles(@NotNull String projectBaseDir, String modelName);

    /**
     * @param projectBaseDir
     * @param modelName
     * @return 模块所在的文件路径
     */
    default String findModuleFilepath(@NotNull String projectBaseDir, String modelName) {
        List<String> moduleFiles = findModuleFiles(projectBaseDir, modelName);
        Assert.isTrue(!moduleFiles.isEmpty(), String.format("project = %s 未找到 modelName = %s", projectBaseDir, modelName));
        Assert.isTrue(moduleFiles.size() == 1,
                String.format("project = %s 找到了 %d 个 modelName = %s 的模块", projectBaseDir, moduleFiles.size(), modelName));
        String path = moduleFiles.get(0);
        return path.substring(0, path.lastIndexOf("."));
    }

}
