package com.wuxp.codegen.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class LoongSdkCodegenMojoTest {


    @Test
    void testExecute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");
        LoongSdkCodegenMojo mojo = new LoongSdkCodegenMojo();
        mojo.setTestClasspath(true);

        mojo.setScanPackages(new String[]{"com.wuxp.codegen.maven"});
        mojo.setMavenProject(mavenProject);
        mojo.setOnlyExecutionRoot(false);
        mojo.execute();
    }
}