package test.com.wuxp.codegen.server.plugins;

import com.wuxp.codegen.core.util.PathResolveUtils;
import com.wuxp.codegen.server.plugins.MavenCodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MavenCodegenPluginExecuteStrategyTest {


    @Test
    void testExecuteCodegenPlugin() {
        String projectDir = "/" + PathResolveUtils.relative(System.getProperty("user.dir"), "../../");
        CodegenPluginExecuteStrategy findPluginExecuteDirStrategy = new MavenCodegenPluginExecuteStrategy("jcenter,dist_repo_profile");
        List<String> optional = findPluginExecuteDirStrategy.findModuleFiles(projectDir, null);
        Assertions.assertFalse(optional.isEmpty());
        optional = findPluginExecuteDirStrategy.findModuleFiles(projectDir, "wuxp-codegen-examples-swagger-3-maven-plugin");
        Assertions.assertFalse(optional.isEmpty());
        findPluginExecuteDirStrategy.executeCodegenPlugin(projectDir, null, null);
    }
}
