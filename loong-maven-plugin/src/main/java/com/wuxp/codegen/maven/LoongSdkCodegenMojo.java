package com.wuxp.codegen.maven;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.starter.enums.OpenApiType;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * sdk 代码生成插件
 * 支持执行{@link com.wuxp.codegen.core.MavenPluginInvokeCodeGenerator#generate}、{@link com.wuxp.codegen.core.CodeGenerator#generate}
 * 的实现，默认执行{@link #LOONG_GENERATOR_CLASS_NAME}
 *
 * @author wuxp
 * @see #codeGeneratorClass
 * @see #pluginCodeGeneratorClass
 */
@Mojo(name = "api-sdk-codegen", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class LoongSdkCodegenMojo extends AbstractSdkCodegenMojo {

    /**
     * 默认执行的sdk生成实现类类名
     */
    private static final String LOONG_GENERATOR_CLASS_NAME = "com.wuxp.codegen.starter.LoongCodeGenerator";

    @Override
    protected void invokeCodegen() {
        if (StringUtils.hasText(pluginCodeGeneratorClass)) {
            invokePluginCodeGenerator();
        } else {
            invokeCodeGenerator();
        }
    }

    /**
     * 调用{@link com.wuxp.codegen.core.MavenPluginInvokeCodeGenerator#generate}的实现
     */
    private void invokePluginCodeGenerator() {
        Class<?> clazz;
        this.getLog().info("开始执行sdk代码插件生成器");
        try {
            clazz = this.getPluginProjectClassLoader().loadClass(pluginCodeGeneratorClass);
        } catch (ClassNotFoundException e) {
            this.getLog().error("not found plugin generator class " + pluginCodeGeneratorClass);
            return;
        }
        Constructor<?> constructor = clazz.getConstructors()[0];
        try {
            Object instance = constructor.newInstance();
            Method generateMethod = clazz.getMethod("generate", String.class, List.class);
            // 调用自定义的生成策略
            generateMethod.invoke(instance, this.getFinallyOutputPath(), transformClassLoaderClientProviderTypes(instance.getClass().getClassLoader()));
        } catch (Exception exception) {
            this.getLog().error(String.format("调用MavenPluginInvokeCodeGenerator的实例失败，%s exception %s", exception.getMessage(), exception.getClass().getName()), exception);
            logger.error("调用MavenPluginInvokeCodeGenerator的实例失败，{}，exception：{}", exception.getMessage(), exception.getClass().getName(), exception);
        }
    }

    /**
     * 调用{@link com.wuxp.codegen.core.CodeGenerator#generate}的实现
     */
    private void invokeCodeGenerator() {
        String codeGeneratorClass = getCodeGeneratorClass();
        if (!StringUtils.hasText(codeGeneratorClass)) {
            codeGeneratorClass = LOONG_GENERATOR_CLASS_NAME;
        }
        Class<?> aClass;
        this.getLog().info("开始执行sdk代码生成器");
        try {
            aClass = this.getPluginProjectClassLoader().loadClass(codeGeneratorClass);
        } catch (ClassNotFoundException e) {
            this.getLog().error("not found generator class " + codeGeneratorClass);
            return;
        }
        Method generateMethod;
        Object newInstance = getCodeGeneratorInstance(aClass);
        if (newInstance == null) {
            return;
        }

        if (openApiType != null) {
            try {
                newInstance.getClass()
                        .getMethod("setOpenApiType", OpenApiType.class)
                        .invoke(newInstance, openApiType);
            } catch (Exception e) {
                this.getLog().error("设置 OpenApiType 类型失败 " + openApiType);
            }
        }

        setCodegenOutPath(newInstance);
        setCodegenClientProviderTypes(newInstance);
        try {
            generateMethod = aClass.getMethod("generate");
        } catch (Exception exception) {
            this.getLog().error(String.format("获取生成方法失败，%s exception %s", exception.getMessage(), exception.getClass().getName()), exception);
            return;
        }
        try {
            generateMethod.invoke(newInstance);
        } catch (Exception exception) {
            this.getLog().error(String.format("代码生成生成执行失败 %s", exception.getMessage()), exception);
            logger.error("代码生成生成执行失败，{}，exception：{}", exception.getMessage(), exception.getClass().getName(), exception);
        }
    }

    /**
     * 实例化一个 {@link com.wuxp.codegen.core.CodeGenerator#generate}的实例
     *
     * @param clazz CodeGenerator子类的全类名
     * @return CodeGenerator子类的实例，return null表示实例化失败
     */
    private Object getCodeGeneratorInstance(Class<?> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 1) {
                return constructors[0].newInstance();
            } else {
                String[] scanPackages = this.getScanPackages();
                Object args = Array.newInstance(String.class, scanPackages.length);
                Constructor<?> classConstructor = clazz.getConstructor(args.getClass());
                for (int i = 0; i < this.scanPackages.length; i++) {
                    Array.set(args, i, this.scanPackages[i]);
                }
                return classConstructor.newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            this.getLog().error(String.format("创建CodeGenerator的实例失败，%s exception %s", exception.getMessage(), exception.getClass().getName()), exception);
            logger.error("创建CodeGenerator的实例失败，{}，exception：{}", exception.getMessage(), exception.getClass().getName(), exception);
        }

        return null;
    }

    /**
     * 设置输出路径
     *
     * @param codegenInstance {@link com.wuxp.codegen.core.CodeGenerator#generate}的实例
     */
    private void setCodegenOutPath(Object codegenInstance) {
        try {
            codegenInstance.getClass().getMethod("setOutputPath", String.class).invoke(codegenInstance, this.getFinallyOutputPath());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // ignore Exception
        }
    }

    /**
     * 设置生成的{@link com.wuxp.codegen.core.ClientProviderType}
     *
     * @param codegenInstance {@link com.wuxp.codegen.core.CodeGenerator#generate}的实例
     */
    private void setCodegenClientProviderTypes(Object codegenInstance) {
        List<? extends Enum<?>> types = transformClassLoaderClientProviderTypes(codegenInstance.getClass().getClassLoader());
        if (types.isEmpty()) {
            return;
        }
        try {
            codegenInstance.getClass().getMethod("setClientProviderTypes", List.class).invoke(codegenInstance, types);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // ignore Exception
        }
    }

    private List<? extends Enum<?>> transformClassLoaderClientProviderTypes(ClassLoader currentClassLoader) {
        List<ClientProviderType> finallyClientProviderTypes = this.getFinallyClientProviderTypes();
        try {
            // 处理由于ClassLoader对象不同导致的同一个类的实例equals不相等的问题
            Class<Enum<?>> typeClass = (Class<Enum<?>>) currentClassLoader.loadClass(ClientProviderType.class.getName());
            return finallyClientProviderTypes.stream()
                    .map(Enum::name)
                    .map(name -> {
                        Enum<?>[] enumConstants = typeClass.getEnumConstants();
                        return Arrays.stream(enumConstants).filter(anEnum -> name.equals(anEnum.name())).findFirst().orElse(null);
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (ClassNotFoundException exception) {
            //  ignore Exception
        }
        return Collections.emptyList();
    }
}
