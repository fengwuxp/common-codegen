package com.wuxp.codegen.server.plugins;

import com.wuxp.codegen.core.util.PathResolveUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MavenCodegenPluginExecuteStrategyTest {


    @Test
    void testExecuteCodegenPlugin() {
        String projectDir = PathResolveUtils.relative(System.getProperty("user.dir"), "../../");
        CodegenPluginExecuteStrategy findPluginExecuteDirStrategy = new MavenCodegenPluginExecuteStrategy("jcenter,dist_repo_profile");
        List<String> moduleFiles = findPluginExecuteDirStrategy.findModuleFiles(projectDir, null);
        Assertions.assertFalse(moduleFiles.isEmpty());
        moduleFiles = findPluginExecuteDirStrategy.findModuleFiles(projectDir, "wuxp-codegen-examples-swagger-3-maven-plugin");
        Assertions.assertFalse(moduleFiles.isEmpty());
        findPluginExecuteDirStrategy.executeCodegenPlugin(projectDir, null, null);
    }
}
