package com.wuxp.codegen.maven;


import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import lombok.Setter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 抽象的sdk生成，用于调用{@link  CodeGenerator#generate()}生成sdk代码
 *
 * @author wuxp
 */
@Setter
public abstract class AbstractSdkCodegenMojo extends AbstractMojo {

    /**
     * target folder for sources
     *
     * @parameter
     */
//    @Parameter(name = "target.folder")
//    protected File targetFolder;

    /**
     * scan packages to be classpath
     * 如果没有设置扫描的包，则使用 {@link MavenProject#getGroupId()}
     *
     * @parameter
     * @see #mavenProject
     */
    @Parameter()
    protected String[] scanPackages;

    /**
     * maven project
     *
     * @parameter default-value="${project}"
     * @readonly
     */
    @Parameter(defaultValue = "${project}")
    protected MavenProject mavenProject;


    /**
     * Whether to skip the exporting execution
     *
     * @parameter default-value=false property="maven.codegen.skip"
     */
    @Parameter(defaultValue = "false")
    private boolean skip;

    /**
     * test classpath usage switch
     *
     * @parameter default-value=false
     */
    @Parameter(defaultValue = "false")
    private boolean testClasspath;

    /**
     * build context
     *
     * @component
     */
    @Component
    private BuildContext buildContext;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip || !hasSourceChanges()) {
            // Only run if something has changed in the source directories. This will
            // prevent m2e from entering an infinite build cycle.
            return;
        }
        ClassLoader classLoader = null;
        try {
            classLoader = getProjectClassLoader();
        } catch (MalformedURLException | DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }

        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver(classLoader);
        String[] scanPackages = this.getScanPackages();
        getLog().info("开始执行dragon maven 插件3，scanPackages：" + Arrays.toString(scanPackages));
        Collection<CodegenBuilder> codeGeneratorBuilders = getCodeGeneratorBuilders();
        codeGeneratorBuilders.stream()
                .map(codegenBuilder -> {
                    if (codegenBuilder instanceof AbstractDragonCodegenBuilder) {
                        AbstractDragonCodegenBuilder dragonCodegenBuilder = (AbstractDragonCodegenBuilder) codegenBuilder;
                        dragonCodegenBuilder.scanPackages(scanPackages).buildCodeGenerator();
                    }
                    return codegenBuilder.buildCodeGenerator();
                })
                .peek(codeGenerator -> {
                    if (codeGenerator instanceof AbstractCodeGenerator) {
                        AbstractCodeGenerator abstractCodeGenerator = (AbstractCodeGenerator) codeGenerator;
                        abstractCodeGenerator.getClassPathScanningCandidateComponentProvider().setResourceLoader(resourceLoader);
                    }
                }).forEach(codeGenerator -> {
            getLog().info("开始执行dragon maven 插件4，scanPackages：" + Arrays.toString(scanPackages));
            codeGenerator.generate();
        });

    }

    /**
     * 获取用于builder {@link CodeGenerator}的 {@link CodegenBuilder}
     *
     * @return
     */
    protected abstract Collection<CodegenBuilder> getCodeGeneratorBuilders();


    @SuppressWarnings("unchecked")
    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException,
            MalformedURLException {
        List<String> classpathElements;
        if (testClasspath) {
            classpathElements = mavenProject.getTestClasspathElements();
        } else {
            classpathElements = mavenProject.getCompileClasspathElements();
        }
        List<URL> urls = new ArrayList<>(classpathElements.size());
        for (String element : classpathElements) {
            File file = new File(element);
            if (file.exists()) {
                urls.add(file.toURI().toURL());
            }
        }
        return new URLClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }

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

    private String[] getScanPackages() {
        String[] scanPackages = this.scanPackages;
        if (scanPackages == null || scanPackages.length == 0) {
            return new String[]{mavenProject.getGroupId()};
        }
        return scanPackages;
    }

}
