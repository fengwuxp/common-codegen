package com.wuxp.codegen.maven;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class DragonSdkCodegenMojoTest {


    @Test
    void testExecute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");
        DragonSdkCodegenMojo mojo = new DragonSdkCodegenMojo();
        mojo.setTestClasspath(true);

        mojo.setScanPackages(new String[]{"com.wuxp.codegen.maven"});
        mojo.setMavenProject(mavenProject);
        mojo.setOnlyExecutionRoot(false);
        mojo.execute();
    }
}