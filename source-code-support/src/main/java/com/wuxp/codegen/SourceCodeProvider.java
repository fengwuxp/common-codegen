package com.wuxp.codegen;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.utils.CollectionStrategy;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceZip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;

import static com.github.javaparser.utils.CodeGenerationUtils.mavenModuleRoot;

/**
 * 用于提供源代码的AST分析结果
 * 通过类对象{@link Class<?>}交换对应的编译结果
 * @author wuxp
 */
@Slf4j
public class SourceCodeProvider {


    /**
     * 内部类的标识
     */
    private static final String INNER_CLASS_FLAG = "$";


    /**
     * 源代码文件扩展名称
     */
    private static final String SOURCE_FILE_EXT_NAME = "java";

    /**
     * 源代码jar包的后缀
     */
    private static final String SOURCES_JAR_SUFFIX = "-sources.jar";

    /**
     * jar的后缀名称
     */
    private static final String JAR_SUFFIX = ".jar";

    /**
     * test class path
     */
    private static final String TEST_CLASSES_PATH = "test-classes";


    /**
     * test source code dir
     */
    private static final String TEST_SOURCE_DIR = String.join(File.separator, "src", "test", "java");

    /**
     * 源代码编译结果缓存
     */
    private final Map<Class<?>, Optional<CompilationUnit>> compilationUnitCaches;

    /**
     * 项目roots缓存
     *
     * @key 模块源码路径
     * @value ProjectRoot
     */
    private final Map<Path, ProjectRoot> projectRoots;

    /**
     * 源码jar解析缓存
     *
     * @key sources.jar路径
     * @value sources.jar解析结果
     */
    private final Map<String, Map<Path, ParseResult<CompilationUnit>>> sourcesJarCaches;


    private final CollectionStrategy parserCollectionStrategy;

    public SourceCodeProvider() {
        compilationUnitCaches = new ConcurrentReferenceHashMap<>(256, ConcurrentReferenceHashMap.ReferenceType.WEAK);
        projectRoots = new ConcurrentReferenceHashMap<>(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);
        sourcesJarCaches = new ConcurrentReferenceHashMap<>(8, ConcurrentReferenceHashMap.ReferenceType.WEAK);
        parserCollectionStrategy = new ParserCollectionStrategy();
    }


    /**
     * 获取一个类的AST解析后的定义对应
     *
     * @param clazz 类对象
     * @return class的编译AST的分析结果对象
     */
    public <T extends TypeDeclaration> Optional<T> getTypeDeclaration(Class<?> clazz) {
        Assert.notNull(clazz, "clazz must not null");
        Optional<CompilationUnit> unitOptional = this.getCompilationUnit(clazz);
        if (unitOptional.isPresent()) {
            CompilationUnit compilationUnit = unitOptional.get();
            String simpleName = clazz.getSimpleName();
            Optional<T> optionalTypeDeclaration = compilationUnit.getTypes().stream()
                    .filter(typeDeclaration -> typeDeclaration.getNameAsString().equals(simpleName))
                    .map(typeDeclaration -> (T) typeDeclaration)
                    .findFirst();
            if (optionalTypeDeclaration.isPresent()) {
                return optionalTypeDeclaration;
            }
            return compilationUnit.getTypes()
                    .stream()
                    // 从节点对象中寻找
                    .map(typeDeclaration -> typeDeclaration.getMembers()
                            .stream()
                            .filter(node -> node instanceof TypeDeclaration)
                            .map(bodyDeclaration -> (TypeDeclaration) bodyDeclaration)
                            .filter(bodyDeclaration -> bodyDeclaration.getNameAsString().equals(simpleName))
                            .map(bodyDeclaration -> (T) bodyDeclaration)
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .findFirst();

        }
        return Optional.empty();
    }

    /**
     * 获取一个类的AST解析后的定义对应
     *
     * @param clazz 类对象
     * @return class的编译AST的分析结果对象
     */
    public Optional<ClassOrInterfaceDeclaration> getInterfaceDeclaration(Class<?> clazz) {
        if (!clazz.isInterface()) {
            log.warn("class={} not interface", clazz.getName());
            return Optional.empty();
        }
        return this.getTypeDeclaration(clazz);
    }

    /**
     * 获取枚举对象的AST对象
     *
     * @param clazz 类对象
     * @return class的编译AST的分析结果对象
     */
    public Optional<EnumDeclaration> getEnumDeclaration(Class<? extends Enum> clazz) {
        if (!clazz.isEnum()) {
            log.warn("class={} not enum", clazz.getName());
            return Optional.empty();
        }
        return this.getTypeDeclaration(clazz);
    }

    /**
     * 获取注解对象的AST对象
     *
     * @param clazz 类对象
     * @return class的编译AST的分析结果对象
     */
    public Optional<AnnotationDeclaration> getAnnotationDeclaration(Class<? extends Annotation> clazz) {
        if (!clazz.isAnnotation()) {
            log.warn("class={} not annotation", clazz.getName());
            return Optional.empty();
        }
        return this.getTypeDeclaration(clazz);
    }

    /**
     * 获取一个类的编译单元对象（一个类对应的源代码文件中可能包含了多个类定义）
     *
     * @param clazz 类对象
     * @return 编译单元对象
     */
    public Optional<CompilationUnit> getCompilationUnit(Class<?> clazz) {
        return compilationUnitCaches.computeIfAbsent(clazz, this::getCompilationUnitBySourcePath);
    }

    /**
     * 获取Field编译描述对象
     *
     * @param field 字段定义
     * @return Field编译描述对象
     */
    public Optional<FieldDeclaration> getFieldDeclaration(Field field) {
        if (field == null) {
            return Optional.empty();
        }
        String fieldName = field.getName();
        Class<?> declaringClass = field.getDeclaringClass();
        return getFieldDeclaration(declaringClass, fieldName);
    }

    /**
     * 获取Field编译描述对象
     *
     * @param clazz     类定义
     * @param fieldName 字段名称
     * @return Field编译描述对象
     */
    public Optional<FieldDeclaration> getFieldDeclaration(Class<?> clazz, String fieldName) {
        return this.getTypeDeclaration(clazz).flatMap(declaration -> declaration
                .getMembers()
                .stream()
                .filter(item -> item instanceof FieldDeclaration)
                .filter(item -> {
                    FieldDeclaration fieldDeclaration = (FieldDeclaration) item;
                    String name = fieldDeclaration.getVariables().get(0).getName().asString();
                    return fieldName.equals(name);
                })
                .findFirst());
    }

    /**
     * 获取枚举常量编译描述对象
     *
     * @param field 枚举常量field
     * @return 枚举常量编译描述对象
     */
    public Optional<EnumConstantDeclaration> getEnumConstantDeclaration(Field field) {
        if (field == null || !field.isEnumConstant()) {
            return Optional.empty();
        }
        String fieldName = field.getName();
        Class<? extends Enum> declaringClass = (Class<? extends Enum>) field.getDeclaringClass();
        return getEnumConstantDeclaration(declaringClass, fieldName);
    }

    /**
     * 获取枚举常量编译描述对象
     *
     * @param enumClass    枚举类定义
     * @param enumConstant 枚举常量名称
     * @return 枚举常量编译描述对象
     */
    public Optional<EnumConstantDeclaration> getEnumConstantDeclaration(Class<? extends Enum> enumClass, String enumConstant) {
        Optional<EnumDeclaration> typeDeclaration = this.getTypeDeclaration(enumClass);
        return typeDeclaration.flatMap(declaration -> declaration
                .getEntries()
                .stream()
                .filter(item -> item.asEnumConstantDeclaration().getName().asString().equals(enumConstant))
                .map(BodyDeclaration::asEnumConstantDeclaration)
                .findFirst());
    }

    /**
     * 获取Method编译描述对象
     *
     * @param method 方法定义
     * @return Method编译描述对象
     */
    public Optional<MethodDeclaration> getMethodDeclaration(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        String methodName = method.getName();
        return this.getMethodDeclaration(declaringClass, methodName,
                Arrays.stream(method.getParameters()).map(java.lang.reflect.Parameter::getType).toArray(Class[]::new));
    }

    /**
     * 获取Method编译描述对象
     *
     * @param clazz          方法定义
     * @param methodName     方法定义
     * @param parameterTypes 参数类型列表
     * @return Method编译描述对象
     */
    public Optional<MethodDeclaration> getMethodDeclaration(Class<?> clazz, String methodName, Class[] parameterTypes) {
        String[] parameterTypeNames = Arrays.stream(parameterTypes).map(Class::getName).toArray(String[]::new);
        return this.getTypeDeclaration(clazz)
                .flatMap(declaration -> declaration
                .getMembers()
                .stream()
                .filter(item -> item instanceof MethodDeclaration)
                .filter(item -> ((MethodDeclaration) item).getName().asString().equals(methodName))
                .filter(item -> {
                    // 参数匹配
                    MethodDeclaration methodDeclaration = (MethodDeclaration) item;
                    NodeList<Parameter> parameters = methodDeclaration.getParameters();
                    String[] parameterTypeSimpleNames = parameters.stream()
                            .map(parameter -> parameter.getType().asString())
                            .toArray(String[]::new);

                    boolean matchParameterType;
                    for (int i = 0; i < parameterTypeSimpleNames.length; i++) {
                        String expectName = parameterTypeNames[i];
                        String actual = parameterTypeSimpleNames[i];
                        if (actual.contains(".")) {
                            // 参数写了全类名
                            matchParameterType = expectName.equals(actual);
                        } else {
                            // 只匹配类型后缀，为了准确加上"."
                            matchParameterType = expectName.endsWith(String.format(".%s", actual));
                        }
                        if (!matchParameterType) {
                            // 不匹配则返回失败
                            return false;
                        }
                    }
                    return true;
                })
                .findFirst());
    }


    /**
     * 获取方法的参数编译定义对象
     *
     * @param parameter java 参数类型定义
     * @return 参数编译定义对象
     */
    public Optional<Parameter> getMethodParameter(java.lang.reflect.Parameter parameter) {
        Executable declaringExecutable = parameter.getDeclaringExecutable();
        Method method = (Method) declaringExecutable;
        Optional<MethodDeclaration> methodDeclaration = this.getMethodDeclaration(method);
        if (!methodDeclaration.isPresent()) {
            return Optional.empty();
        }
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        NodeList<Parameter> parameters = methodDeclaration.get().getParameters();
        if (index >= parameters.size()) {
            return Optional.empty();
        }
        return Optional.of(parameters.get(index));
    }


    /**
     * 从源代码路径（classes\sources.jar）获取编译结果
     *
     * @param clazz 类对象
     * @return 编译单元对象
     */
    private Optional<CompilationUnit> getCompilationUnitBySourcePath(Class<?> clazz) {
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            return Optional.empty();
        }
        String sourceCodePath = codeSource.getLocation().getPath();
        if (isSystemWindows()){
            // 如果是windows 移除路径上的第一个斜杆
            sourceCodePath=sourceCodePath.substring(1);
        }
        if (this.isInSourcesJar(sourceCodePath)) {
            // sources.jar中
            return getSourceCodeBySourcesJar(clazz, sourceCodePath);
        } else {
            return getSourceCodeByClasses(clazz, sourceCodePath);
        }
    }


    /**
     * 从项目的classes目录下获取源代码文件
     *
     * @param clazz 类对象
     * @return 编译单元对象
     */
    private Optional<CompilationUnit> getSourceCodeByClasses(Class<?> clazz, String classSourcePath) {
        Path mavenModuleRoot = mavenModuleRoot(clazz);
        if (mavenModuleRoot == null) {
            return Optional.empty();
        }
        ProjectRoot projectRoot = projectRoots.computeIfAbsent(mavenModuleRoot, key -> parserCollectionStrategy.collect(mavenModuleRoot));
        if (projectRoot == null) {
            return Optional.empty();
        }
        boolean isTestClasses = classSourcePath.endsWith(String.format("%s%s", TEST_CLASSES_PATH, File.separator));
        String sourceFilePath = getSourceFilePath(clazz);
        // 模块下可能有多个源代码目录，main\test等
        return projectRoot.getSourceRoots()
                .stream()
                .filter(sourceRoot -> {
                    if (isTestClasses) {
                        return sourceRoot.getRoot().endsWith(TEST_SOURCE_DIR);
                    } else {
                        return true;
                    }
                })
                .map(sourceRoot -> {
                    try {
                        return sourceRoot.parse(clazz.getPackage().getName(), sourceFilePath);
                    } catch (Exception e) {
                        // 由于target目录下可能会有一些其他目录会被误识别为源码目录，此处的异常忽略
                        log.warn("get source file error sourceRoot={},sourceFilePath={},clazz={}", sourceRoot.getRoot(), sourceFilePath, clazz.getName());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }


    private Optional<CompilationUnit> getSourceCodeBySourcesJar(Class<?> clazz, String classSourcePath) {
        classSourcePath = classSourcePath.replace(JAR_SUFFIX, SOURCES_JAR_SUFFIX);
        if (!new File(classSourcePath).exists()) {
            log.warn("sources.jar not exists,classSourcePath={}", classSourcePath);
            return Optional.empty();
        }
        Map<Path, ParseResult<CompilationUnit>> sources = sourcesJarCaches.computeIfAbsent(classSourcePath, this::getSourcesJarParseResults);
        Path sourcePath = Paths.get(this.transformClassToPath(clazz));
        return sources.get(sourcePath).getResult();
    }

    private Map<Path, ParseResult<CompilationUnit>> getSourcesJarParseResults(String sourcesJarPath) {
        SourceZip sourceZip = new SourceZip(Paths.get(sourcesJarPath));
        Map<Path, ParseResult<CompilationUnit>> sources = new HashMap<>(256);
        try {
            sourceZip.parse(sources::put);
        } catch (IOException e) {
            log.info("解析sources.jar失败，sourcesJarPath = {},message={}", sourcesJarPath, e.getMessage(), e);
            return Collections.emptyMap();
        }
        return sources;
    }


    private String getSourceFilePath(Class<?> clazz) {
        String name = clazz.getName();
        String simpleName = clazz.getSimpleName();
        if (name.contains(INNER_CLASS_FLAG)) {
            // TODO 内部类判断加强 Modifier.isStatic(clazz.getModifiers())
            name = name.split("\\$")[0];
            String[] values = name.split("\\.");
            simpleName = values[values.length - 1];
        }
        return String.format("%s.%s", simpleName, SOURCE_FILE_EXT_NAME);
    }

    private String transformClassToPath(Class<?> clazz) {
        return String.format("%s.%s", clazz.getName().replace(".", "/"), SOURCE_FILE_EXT_NAME);
    }

    private boolean isInSourcesJar(String classSourcePath) {
        return classSourcePath.endsWith(JAR_SUFFIX);
    }

    private static  boolean isSystemWindows (){
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
