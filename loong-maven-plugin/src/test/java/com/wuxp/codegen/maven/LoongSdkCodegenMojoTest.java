package com.wuxp.codegen.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import java.io.File;

class LoongSdkCodegenMojoTest {


    @Test
    void testExecute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.setFile(new File(System.getProperty("user.dir")));
        mavenProject.getBuild().setOutputDirectory("target/classes");
        LoongSdkCodegenMojo mojo = new LoongSdkCodegenMojo();
        mojo.setIncludeTestClasspath(true);

        mojo.setScanPackages(new String[]{"com.wuxp.codegen.maven"});
        mojo.setMavenProject(mavenProject);
        mojo.setOnlyExecutionRoot(false);
        mojo.execute();
    }
}