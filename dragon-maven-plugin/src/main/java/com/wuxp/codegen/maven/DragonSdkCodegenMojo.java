package com.wuxp.codegen.maven;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * sdk 代码生成插件
 * 默认在编译阶段输出
 *
 * @author wuxp
 */
@Mojo(name = "api-sdk-codegen", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DragonSdkCodegenMojo extends AbstractSdkCodegenMojo {

    private static final String GENERATOR_CLASS_NAME = "com.wuxp.codegen.starter.DragonSdkCodeGenerator";

    @Override
    protected void invokeCodegen() {
        Class<?> aClass = null;

        this.getLog().info("开始执行生成器");
        try {
            aClass = this.getPluginProjectClassLoader().loadClass(GENERATOR_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            this.getLog().error("not found generator class " + GENERATOR_CLASS_NAME);
            return;
        }
        Method generateMethod = null;
        Object newInstance = null;
        try {
            generateMethod = aClass.getMethod("generate");
            String[] scanPackages = this.getScanPackages();
            Object args = Array.newInstance(String.class, scanPackages.length);
            Constructor<?> classConstructor = aClass.getConstructor(args.getClass());
            for (int i = 0; i < this.scanPackages.length; i++) {
                Array.set(args, i, this.scanPackages[i]);
            }
            newInstance = classConstructor.newInstance(args);
        } catch (Exception exception) {
            this.getLog().error("获取生成方法失败 " + exception.getMessage() + " exception " + exception.getClass().getName());
            return;
        }
        try {
            generateMethod.invoke(newInstance);
        } catch (Exception exception) {
            this.getLog().error("代码生成生成执行失败 " + exception.getMessage());
        }
    }
}
