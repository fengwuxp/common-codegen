package com.wuxp.codegen.maven;


import com.wuxp.codegen.core.CodeGenerator;
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

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * 抽象的sdk生成，用于调用{@link  CodeGenerator#generate()}生成sdk代码
 *
 * @author wuxp
 */
@Setter
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
     * Whether to skip the exporting execution
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    /**
     * test classpath usage switch
     */
    @Parameter(property = "test.classpath",defaultValue = "false")
    private boolean testClasspath;

    /**
     * 插件仅在构建命令启动的模块中执行
     */
    @Parameter(property = "only.execution.root",defaultValue = "true")
    private boolean onlyExecutionRoot = true;

    /**
     * Replace the absolute path to the local repo with this property. This field is ignored it prefix is declared. The
     * value will be forced to "${M2_REPO}" if no value is provided AND the attach flag is true.
     */
    @Parameter(defaultValue = "")
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
     * @see #getProjectDependencies()
     * </p>
     */
    private ClassLoader pluginProjectClassLoader;

    @Override
    public void execute() throws MojoFailureException {
        getLog().info("开始执行dragon codegen maven 插件");
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
     * invoke {@link CodeGenerator#generate()}
     */
    protected abstract void invokeCodegen();


    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException,
            MalformedURLException {
        List<String> classpathElements;
        if (testClasspath) {
            classpathElements = mavenProject.getTestClasspathElements();
        } else {
            classpathElements = mavenProject.getCompileClasspathElements();
        }
        // classpath的类路径
        List<URL> urls = new ArrayList<>(classpathElements.size());
        for (String element : classpathElements) {
            File file = new File(element);
            if (file.exists()) {
                urls.add(file.toURI().toURL());
            }
        }
        // 加入项目的依赖
        urls.addAll(this.getProjectDependencies());

        ClassLoader classLoader = getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            getLog().info("add class loader url");
            urls.addAll(Arrays.asList(((URLClassLoader) classLoader).getURLs()));
//            installDependeciesToClassLoader(urls, classLoader);
        }
        return new URLClassLoader(urls.toArray(new URL[0]), null);
    }

    private void installDependeciesToClassLoader(List<URL> urls, ClassLoader classLoader) {
        Method addURL = null;
        try {
            addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (addURL != null) {
            AccessibleObject.setAccessible(new AccessibleObject[]{addURL}, true);
            try {
                for (URL url : urls) {
                    addURL.invoke(classLoader, url);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
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
            List sourceRoots = testClasspath ? mavenProject.getTestCompileSourceRoots() :
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
    private List<URL> getProjectDependencies() {
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


    protected String[] getScanPackages() {
        String[] packages = this.scanPackages;
        if (packages == null || packages.length == 0) {
            packages = new String[]{mavenProject.getGroupId()};
            this.scanPackages = packages;
        }
        return packages;
    }

    public ClassLoader getPluginProjectClassLoader() {
        return pluginProjectClassLoader;
    }

}
