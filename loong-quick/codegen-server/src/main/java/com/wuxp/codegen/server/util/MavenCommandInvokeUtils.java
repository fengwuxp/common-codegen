package com.wuxp.codegen.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

/**
 * 调用maven命令
 *
 * @author wuxp
 */
@Slf4j
public final class MavenCommandInvokeUtils {

    private MavenCommandInvokeUtils() {
    }

    /**
     * @param command     maven命令如：clean
     * @param mavenHome   maven系统路径
     * @param pomFilepath 要操控的pom文件的系统路径
     * @param profiles    profiles
     */
    public static void execute(String command, String mavenHome, String pomFilepath, String profiles) {
        InvocationRequest request = new DefaultInvocationRequest();
        //想要操控的pom文件的位置
        if (pomFilepath != null) {
            request.setPomFile(new File(pomFilepath));
        }
        //操控的maven命令
        request.setGoals(Collections.singletonList(command));
        if (profiles != null) {
            request.setProfiles(Arrays.asList(profiles.split(",")));
        }
        Invoker invoker = new DefaultInvoker();
        if (mavenHome != null) {
            invoker.setMavenHome(new File(mavenHome));
        }
        invoker.setLogger(new SystemOutLogger());
        try {
            invoker.execute(request);
        } catch (MavenInvocationException exception) {
            log.error("调用maven命令失败：{}", exception.getMessage(), exception);
        }
    }

}
