package com.wuxp.codegen.server.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * 调用maven命令
 * <p>
 *   增加从系统的环境变量中获取{@link #MAVEN_HOME}和{@link #JAVA_HOME}
 * </p>
 * @author wuxp
 */
@Slf4j
public final class MavenCommandInvokeUtils {


    /**
     * maven home 地址
     */
    private static final String MAVEN_HOME;

    /**
     * java home地址
     */
    private static final String JAVA_HOME;

    private MavenCommandInvokeUtils() {
    }

    static {
        MAVEN_HOME = getSystemEnv("MAVEN_HOME", "MVN_HOME");
        JAVA_HOME = getSystemEnv("JAVA_HOME", "JAVA8_HOME");
    }


    /**
     * @param command     maven命令如：clean
     * @param pomFilepath 要操控的pom文件的系统路径
     * @param profiles    profiles
     */
    public static void execute(String command, String pomFilepath, String profiles) {
        execute(command, MAVEN_HOME, pomFilepath, profiles);
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
        if (JAVA_HOME != null) {
            request.setJavaHome(new File(JAVA_HOME));
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

    private static String getSystemEnv(String... names) {
        return Arrays.stream(names).map(name -> {
            String env = System.getenv(name);
            if (env == null) {
                env = System.getProperty(name);
            }
            return env;
        }).filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
