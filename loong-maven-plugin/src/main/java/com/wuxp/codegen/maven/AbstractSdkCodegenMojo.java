package com.wuxp.codegen.maven;


import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.loong.CodegenSdkUploader;
import com.wuxp.codegen.starter.enums.OpenApiType;
import lombok.Getter;
import lombok.Setter;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.shared.transfer.repository.RepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 抽象的sdk生成，用于调用{@link  com.wuxp.codegen.core.CodeGenerator#generate()}生成sdk代码
 *
 * @author wuxp
 */
@Setter
@Getter
public abstract class AbstractSdkCodegenMojo extends AbstractMojo {

    public final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * scan packages to be classpath
     * 如果没有设置扫描的包，则使用 {@link MavenProject#getGroupId()}
     *
     * @see #mavenProject
     */
    @Parameter(property = "scan.packages")
    protected String[] scanPackages;

    /**
     * 设置OpenApi类型
     */
    @Parameter
    protected OpenApiType openApiType;

    /**
     * 代码生成输出路径
     */
    @Parameter(property = "output.path", defaultValue = "${project.build.directory}")
    protected String outputPath;


    /**
     * Whether to skip the exporting execution
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    /**
     * 是否导入test classpath下的代码
     */
    @Parameter(property = "include.test.classpath", defaultValue = "true")
    private boolean includeTestClasspath = true;

    /**
     * 插件仅在构建命令启动的模块中执行
     */
    @Parameter(property = "only.execution.root", defaultValue = "true")
    private boolean onlyExecutionRoot = true;

    /**
     * 生成 sdk 的client lib type，如果为null或空，生成所有的
     */
    @Parameter(property = "client.provider.types")
    protected List<String> clientProviderTypes;

    /**
     * 执行的{@link com.wuxp.codegen.core.CodeGenerator} 实现类
     */
    @Parameter(property = "code.generator.class")
    protected String codeGeneratorClass;

    /**
     * 执行的{@link com.wuxp.codegen.core.MavenPluginInvokeCodeGenerator} 实现类
     * 该配置的优先级高于{@link #codeGeneratorClass}
     */
    @Parameter(property = "plugin.code.generator.class")
    protected String pluginCodeGeneratorClass;

    /**
     * codegen-server地址
     */
    @Parameter(property = CodegenSdkUploader.QUERY_SERVER_ADDRESS)
    private String loongCodegenServer;

    /**
     * 项目名称
     */
    @Parameter(property = CodegenSdkUploader.PROJECT_NAME)
    private String projectName;

    /**
     * 项目当前分支
     */
    @Parameter(property = CodegenSdkUploader.PROJECT_BRANCH_NAME, defaultValue = "master")
    private String projectBranch;

    /**
     * 运行插件的当前模块名称
     */
    @Parameter(property = CodegenSdkUploader.PROJECT_MODULE_NAME)
    private String currentModule;

    /**
     * Replace the absolute path to the local repo with this property. This field is ignored it prefix is declared. The
     * value will be forced to "${M2_REPO}" if no value is provided AND the attach flag is true.
     */
    @Parameter(defaultValue = "${M2_REPO}")
    private String localRepoProperty;

    /**
     * maven project
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    /**
     * The Maven session
     */
    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    protected MavenSession session;

    /**
     * 开启和项目的依赖
     */
    private boolean enableMargeProjectDependencies;

    /**
     * build context
     */
    @Component
    private BuildContext buildContext;

    /**
     * Maven ProjectHelper
     */
    @Component
    private MavenProjectHelper projectHelper;

    @Component
    private RepositoryManager repositoryManager;

    /**
     * 插件执行时的类加载器，用于加载项目空间的类
     * <p>
     * 由于插件执行时在独立的依赖空间中，无法获取项目的依赖，需要额外的处理
     *
     * @see #getProjectClassLoader()
     * @see #getProjectArtifactUrls()
     * </p>
     */
    private ClassLoader pluginProjectClassLoader;

    @Override
    public void execute() throws MojoFailureException {
        getLog().info("开始执行loong codegen maven 插件");
        if (skip || !hasSourceChanges()) {
            // Only run if something has changed in the source directories. This will
            // prevent m2e from entering an infinite build cycle.
            getLog().info("skip 插件 or source code not changes");
            return;
        }
        if (onlyExecutionRoot
                && !mavenProject.isExecutionRoot()) {
            getLog().warn(" 插件仅仅在命令启动的模块中[" + new File("").getAbsolutePath() + "]启用 ，可以配置插件参数[onlyExecutionRoot = false]禁用.");
            return;
        }
        try {
            this.pluginProjectClassLoader = getProjectClassLoader();
        } catch (MalformedURLException | DependencyResolutionRequiredException e) {
            getLog().error("初始化class loader failed：" + e.getMessage());
            throw new MojoFailureException(e.getMessage(), e);
        }

        setUploadCodegenResultProperties();
        // 在新的线程中执行
        Callable<Object> callable = Executors.callable(() -> {
            Thread.currentThread().setContextClassLoader(pluginProjectClassLoader);
            invokeCodegen();
        });
        try {
            callable.call();
        } catch (Exception e) {
            this.getLog().error("代码生成执行失败：" + e.getMessage());
        }
    }


    /**
     * invoke {@link com.wuxp.codegen.core.CodeGenerator#generate()}
     */
    protected abstract void invokeCodegen();


    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException,
            MalformedURLException {
        // classpath的类路径
        List<String> compileClasspathElements = mavenProject.getCompileClasspathElements();
        List<URL> urls = new ArrayList<>(compileClasspathElements.size());
        for (String element : compileClasspathElements) {
            File file = new File(element);
            if (file.exists()) {
                urls.add(file.toURI().toURL());
            }
        }
        if (includeTestClasspath) {
            // include test classpath
            for (String element : mavenProject.getTestClasspathElements()) {
                File file = new File(element);
                if (file.exists()) {
                    urls.add(file.toURI().toURL());
                }
            }
        }
        // 加入项目的依赖
        urls.addAll(this.getProjectArtifactUrls());

        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            getLog().info("add class loader url");
            urls.addAll(Arrays.asList(((URLClassLoader) classLoader).getURLs()));
            if (enableMargeProjectDependencies) {
                // never enable，is only example usage
                addProjectArtifactUrlToMainClassLoader(urls, classLoader);
            }
        }
        return new URLClassLoader(urls.toArray(new URL[0]), null);
    }

    /**
     * 将项目的依赖加入到插件的 classLoader中
     *
     * @param urls        依赖的jar路径
     * @param classLoader 插件的classLoader
     * @implNote 这个是一个危险的动作，肯能会引起jar版本冲突等问题 慎用！！！
     */
    private void addProjectArtifactUrlToMainClassLoader(List<URL> urls, ClassLoader classLoader) {
        Method addUrlMethod;
        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            getLog().warn("get URLClassLoader addURL method fail");
            return;
        }
        ReflectionUtils.makeAccessible(addUrlMethod);
        try {
            for (URL url : urls) {
                addUrlMethod.invoke(classLoader, url);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            getLog().warn("add project artifact url to main classLoader fail,message:" + e.getMessage());
        }
    }


    /**
     * 判断源码是否发生改变
     *
     * @return if return<code>true</code> 表示代码已经改变
     */
    @SuppressWarnings("rawtypes")
    private boolean hasSourceChanges() {
        if (buildContext != null) {
            List sourceRoots = includeTestClasspath ? mavenProject.getTestCompileSourceRoots() :
                    mavenProject.getCompileSourceRoots();
            for (Object path : sourceRoots) {
                if (buildContext.hasDelta(new File(path.toString()))) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取项目的依赖，交由插件的{@link ClassLoader}使用
     *
     * @see #pluginProjectClassLoader
     */
    private List<URL> getProjectArtifactUrls() {
        List<URL> urls = new ArrayList<>(64);
        Map<String, Artifact> artifacts = new LinkedHashMap<>();
        for (Artifact artifact : mavenProject.getArtifacts()) {
            String key = String.format("%s:%s%s", artifact.getGroupId(), artifact.getArtifactId(), artifact.hasClassifier() ? ":" + artifact.getClassifier() : "");
            if (artifacts.containsKey(key)) {
                logger.error(" ****  {} 的依赖中包含冲突的构件：{} {}", mavenProject.getArtifact(), artifact, artifacts.get(key));
            } else {
                artifacts.put(key, artifact);
            }
            if (artifact.isResolved() && artifact.getFile() != null) {
                try {
                    urls.add(artifact.getFile().toURI().toURL());
                } catch (MalformedURLException e) {
                    logger.error(" ****  {} 依赖包不可用,artifact={},path={}", mavenProject.getArtifact(), artifact, artifact.getFile().toPath());
                }
            } else {
                logger.error(" ****  {} 依赖包不可用,artifact={},path={}", mavenProject.getArtifact(), artifact, artifact.getFile().toPath());
            }
        }
        return urls;
    }

    /**
     * 设置上传sdk到codegen-server的系统属性
     */
    private void setUploadCodegenResultProperties() {
        String name = projectName == null ? mavenProject.getName() : projectName;
        if (name != null) {
            System.setProperty(CodegenSdkUploader.PROJECT_NAME, name);
        }
        if (projectBranch != null) {
            System.setProperty(CodegenSdkUploader.PROJECT_BRANCH_NAME, projectBranch);
        }
        if (currentModule != null) {
            System.setProperty(CodegenSdkUploader.PROJECT_MODULE_NAME, currentModule);
        }
        if (loongCodegenServer != null) {
            System.setProperty(CodegenSdkUploader.QUERY_SERVER_ADDRESS, loongCodegenServer);
        }
        if (mavenProject == null) {
            return;
        }
        MavenProject parent = mavenProject;
        while (parent != null) {
            boolean isBreak = parent.getParent() == null || parent.getParent().getBasedir() == null;
            if (isBreak) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent != null) {
            System.setProperty("project.basedir", parent.getBasedir().getAbsolutePath());
        }
    }

    protected String[] getScanPackages() {
        if (scanPackages == null || scanPackages.length == 0) {
            scanPackages = new String[]{mavenProject.getGroupId()};
        }
        return scanPackages;
    }

    protected String getFinallyOutputPath() {
        if (!StringUtils.hasText(outputPath) && mavenProject.getBuild() != null) {
            outputPath = mavenProject.getBuild().getOutputDirectory();
        }
        return outputPath;
    }

    protected List<ClientProviderType> getFinallyClientProviderTypes() {
        if (clientProviderTypes == null || clientProviderTypes.isEmpty()) {
            return Arrays.asList(ClientProviderType.values());
        }
        return clientProviderTypes.stream().map(String::toUpperCase).map(ClientProviderType::valueOf).collect(Collectors.toList());
    }

    public ClassLoader getPluginProjectClassLoader() {
        return pluginProjectClassLoader;
    }


}
