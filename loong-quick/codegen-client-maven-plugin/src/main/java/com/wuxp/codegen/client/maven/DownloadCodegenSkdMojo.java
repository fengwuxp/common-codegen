package com.wuxp.codegen.client.maven;

import com.wuxp.codegen.core.ClientProviderType;
import lombok.Setter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * 用于下载sdk的插件
 *
 * @author wuxp
 */
@Setter
@Mojo(name = "download-sdk-codegen", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DownloadCodegenSkdMojo extends AbstractMojo {


    /**
     * codegen-server的服务端地址
     */
    @Parameter()
    private String loongCodegenServer;

    /**
     * 项目名称
     */
    @Parameter()
    private String projectName;

    /**
     * 项目当前分支
     */
    @Parameter(defaultValue = "master")
    private String projectBranch;

    /**
     * 需要下载的sdk模块名称
     */
    @Parameter()
    private String moduleName;

    /**
     * ClientProviderType
     */
    @Parameter(defaultValue = "SPRING_CLOUD_OPENFEIGN")
    private String clientType;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String projectBasedir = mavenProject.getBasedir().getAbsolutePath();
        new CodegenDownloader(loongCodegenServer, projectName, projectBranch).download(moduleName, ClientProviderType.valueOf(clientType.toUpperCase()), projectBasedir);
    }
}
