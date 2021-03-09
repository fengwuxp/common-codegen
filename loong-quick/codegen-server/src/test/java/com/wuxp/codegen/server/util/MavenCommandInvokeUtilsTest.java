package com.wuxp.codegen.server.util;

import com.wuxp.codegen.core.CodegenVersion;
import com.wuxp.codegen.core.util.PathResolveUtils;
import org.junit.jupiter.api.Test;

import java.io.File;


class MavenCommandInvokeUtilsTest {

    @Test
    void testMavenCommandExecute() {
        String mavenHome = System.getenv("MAVEN_HOME");
        String exampleMavenPluginDir = PathResolveUtils.relative(System.getProperty("user.dir"), "../../examples/swagger-3-maven-plugin");
        String pom = String.format("/%s%spom.xml", exampleMavenPluginDir, File.separator);
        String profiles = "jcenter,dist_repo_profile";
        MavenCommandInvokeUtils.execute("compile", mavenHome, pom, profiles);
        MavenCommandInvokeUtils.execute("compile test -Dmaven.test.skip=true", mavenHome, pom, "jcenter,dist_repo_profile");
        MavenCommandInvokeUtils.execute(String.format("com.wuxp.codegen:wuxp-codegen-loong-maven-plugin:%s:api-sdk-codegen", CodegenVersion.VERSION), mavenHome, pom, "jcenter,dist_repo_profile");
    }
}
