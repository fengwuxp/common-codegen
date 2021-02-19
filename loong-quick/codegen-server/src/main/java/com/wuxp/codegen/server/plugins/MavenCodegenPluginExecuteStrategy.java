package com.wuxp.codegen.server.plugins;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodegenVersion;
import com.wuxp.codegen.server.util.MavenCommandInvokeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * maven sdk代码生成插件执行策略
 *
 * @author wuxp
 */
@Slf4j
public class MavenCodegenPluginExecuteStrategy implements CodegenPluginExecuteStrategy {

    /**
     * 代码生成的maven插件 名称
     */
    private static final String CODEGEN_MAVEN_PLUGIN_ARTIFACT_ID = "wuxp-codegen-loong-maven-plugin";

    private static final String MODULE_FILE_NAME = "pom.xml";

    private final String profiles;

    private final IOFileFilter pomFileFilter;

    public MavenCodegenPluginExecuteStrategy(String profiles) {
        this.profiles = profiles;
        this.pomFileFilter = new NameFileFilter(MODULE_FILE_NAME);
    }

    @Override
    public void executeCodegenPlugin(String projectBaseDir, String modelName, ClientProviderType type) {
        this.findModuleFiles(projectBaseDir, modelName).forEach(this::invokeCodegenPlugin);
    }

    @Override
    public List<String> findModuleFiles(String projectBaseDir, String modelName) {

        Collection<File> pomFiles = FileUtils.listFiles(new File(projectBaseDir), pomFileFilter, TrueFileFilter.INSTANCE);
        if (pomFiles.isEmpty()) {
            return Collections.emptyList();
        }
        boolean filterByModelName = StringUtils.hasText(modelName);
        return pomFiles.stream()
                .filter(file -> {
                    Optional<Model> optional = this.parsePom(file);
                    if (!optional.isPresent()) {
                        return false;
                    }
                    Model model = optional.get();
                    if (filterByModelName) {
                        return modelName.equals(model.getArtifactId()) || modelName.equals(model.getName());
                    } else {
                        return findCodegenPlugin(model).isPresent();
                    }
                })
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
    }

    private Optional<Plugin> findCodegenPlugin(Model model) {
        Build build = model.getBuild();
        if (build == null) {
            return Optional.empty();
        }
        List<Plugin> plugins = build.getPlugins();
        return plugins.stream().filter(plugin -> CODEGEN_MAVEN_PLUGIN_ARTIFACT_ID.equals(plugin.getArtifactId())).findFirst();
    }

    private Optional<Model> parsePom(File pom) {
        //pom 为 pom.xml 路径
        try {
            try (InputStream inputStream = new FileInputStream(pom)) {
                MavenXpp3Reader reader = new MavenXpp3Reader();
                //  最后得到的model就是pom.xml解析后对应的Java模型。
                return Optional.of(reader.read(inputStream));
            }
        } catch (IOException | XmlPullParserException exception) {
            log.error("解析pom文件：{}失败：{}", pom.getAbsolutePath(), exception.getMessage(), exception);
        }
        return Optional.empty();
    }

    /**
     * 执行maven代码生成插件
     *
     * @param pom pom 文件路径
     */
    private void invokeCodegenPlugin(String pom) {
        String mavenHome = System.getenv("MAVEN_HOME");
        MavenCommandInvokeUtils.execute("compile", mavenHome, pom, profiles);
        MavenCommandInvokeUtils.execute("compile test -Dmaven.test.skip=true", mavenHome, pom, profiles);
        MavenCommandInvokeUtils.execute(String.format("com.wuxp.codegen:wuxp-codegen-loong-maven-plugin:%s:api-sdk-codegen", CodegenVersion.VERSION), mavenHome, pom, profiles);
    }
}
