package com.wuxp.codegen;


import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.retrofit2.Retrofit2AnnotationProvider;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.macth.ExcludeClassCodeGenMatcher;
import com.wuxp.codegen.core.macth.IncludeClassCodeGenMatcher;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.parser.enhance.CombineLanguageEnhancedProcessor;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.loong.strategy.AgreedPackageMapStrategy;
import com.wuxp.codegen.enums.EnumCommentEnhancer;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.languages.typescript.UmiModel;
import com.wuxp.codegen.languages.typescript.UmiRequestEnhancedProcessor;
import com.wuxp.codegen.mapping.AbstractLanguageTypeMapping;
import com.wuxp.codegen.mapping.LanguageTypeMappingFactory;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 代码生成配置
 *
 * @author wxup
 */
public abstract class AbstractLoongCodegenBuilder implements CodegenBuilder {

    static {
        AbstractAnnotationProcessor.registerAnnotationProvider(ClientProviderType.RETROFIT, new Retrofit2AnnotationProvider());
    }


    protected LanguageDescription languageDescription;

    protected ClientProviderType clientProviderType;

    /**
     * 扫码生成的包名
     */
    protected String[] scanPackages;


    /**
     * 输出路径
     */
    protected String outPath;


    /**
     * 基础数据类型的映射关系
     */
    protected Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new LinkedHashMap<>();


    /**
     * 自定义的类型映射
     */
    protected Map<Class<?>, CommonCodeGenClassMeta> customTypeMapping = new LinkedHashMap<>();

    /**
     * 自定义的java类型映射
     */
    protected Map<Class<?>, Class<?>[]> customJavaTypeMapping = new LinkedHashMap<>();

    /**
     * 忽略的包
     */
    protected Set<String> ignorePackages = new HashSet<>();

    /**
     * 需要忽略的类
     */
    protected Set<Class> ignoreClasses = new HashSet<>();

    /**
     * 到导入的包
     */
    protected Set<String> includePackages = new HashSet<>();

    /**
     * 额外导入的类
     */
    protected Set<Class> includeClasses = new HashSet<>();


    /**
     * 忽略的方法
     *
     * @key 类
     * @value 方法名称
     */
    protected Map<Class<?>, String[]> ignoreMethods;

    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy = new AgreedPackageMapStrategy();

    /**
     * 代码检测器
     */
    protected Collection<CodeDetect> codeDetects = new ArrayList<>();

    /**
     * 代码匹配器
     */
    protected Collection<CodeGenMatcher> codeGenMatchers = new ArrayList<>();

    /**
     * 代码生成匹配策略
     */
    protected Collection<CodeGenMatchingStrategy> codeGenMatchingStrategies = new ArrayList<>();

    /**
     * 是否删除输出目录
     */
    protected Boolean isDeletedOutputDirectory = true;


    /**
     * 启用下划线风格，将字段的驼峰名转换为下线命名风格
     */
    protected boolean enableFieldUnderlineStyle = false;


    /**
     * 模板文件版本
     *
     * @see TemplateFileVersion
     */
    protected String templateFileVersion = TemplateFileVersion.DEFAULT.getVersion();


    /**
     * 需要而外生成的代码
     */
    protected Set<CommonCodeGenClassMeta> otherCodegenClassMetas = new HashSet<>();


    /**
     * 语言处理增强器
     */
    protected List<LanguageEnhancedProcessor> languageEnhancedProcessors = new ArrayList<>();

    /**
     * 被改注解标记的参数需要忽略
     */
    protected Set<Class<? extends Annotation>> ignoreParamByAnnotations = new HashSet<>();

    /**
     * 模板共享变量
     */
    protected Map<String, Object> sharedVariables = new HashMap<>();

    protected AbstractLoongCodegenBuilder() {
    }

    public AbstractLoongCodegenBuilder scanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
        return this;
    }

    public AbstractLoongCodegenBuilder outPath(String outPath) {
        this.outPath = outPath;
        return this;
    }

    public AbstractLoongCodegenBuilder baseTypeMapping(Class<?> javaType, CommonCodeGenClassMeta classMeta) {
        this.baseTypeMapping.put(javaType, classMeta);
        return this;
    }

    public AbstractLoongCodegenBuilder customTypeMapping(Class<?> javaType, CommonCodeGenClassMeta classMeta) {
        this.customTypeMapping.put(javaType, classMeta);
        return this;
    }

    public AbstractLoongCodegenBuilder customJavaTypeMapping(Class<?> javaType, Class<?>[] classes) {
        this.customJavaTypeMapping.put(javaType, classes);
        return this;
    }

    public AbstractLoongCodegenBuilder ignorePackages(String... ignorePackages) {
        this.ignorePackages.addAll(Arrays.asList(ignorePackages));
        return this;
    }

    public AbstractLoongCodegenBuilder ignoreParamByAnnotations(Class<? extends Annotation>... ignoreParamByAnnotations) {
        this.ignoreParamByAnnotations.addAll(Arrays.asList(ignoreParamByAnnotations));
        return this;
    }

    public AbstractLoongCodegenBuilder includePackages(String... includePackages) {
        this.includePackages.addAll(Arrays.asList(includePackages));
        return this;
    }

    public AbstractLoongCodegenBuilder sharedVariables(String name, Object val) {
        sharedVariables.put(name, val);
        return this;
    }


    public AbstractLoongCodegenBuilder packageMapStrategy(PackageMapStrategy packageMapStrategy) {
        Assert.notNull(packageMapStrategy, "package map strategy not null");
        this.packageMapStrategy = packageMapStrategy;
        return this;
    }

    public AbstractLoongCodegenBuilder codeDetects(CodeDetect... codeDetects) {
        this.codeDetects.addAll(Arrays.asList(codeDetects));
        return this;
    }

    public AbstractLoongCodegenBuilder codeGenMatchers(CodeGenMatcher... codeGenMatchers) {
        this.codeGenMatchers.addAll(Arrays.asList(codeGenMatchers));
        return this;
    }


    public AbstractLoongCodegenBuilder codeGenMatchers(CodeGenMatchingStrategy... codeGenMatchingStrategies) {
        this.codeGenMatchingStrategies.addAll(Arrays.asList(codeGenMatchingStrategies));
        return this;
    }

    public AbstractLoongCodegenBuilder isDeletedOutputDirectory(Boolean isDeletedOutputDirectory) {
        this.isDeletedOutputDirectory = isDeletedOutputDirectory;
        return this;
    }

    public AbstractLoongCodegenBuilder languageDescription(LanguageDescription languageDescription) {
        this.languageDescription = languageDescription;
        return this;
    }

    public AbstractLoongCodegenBuilder clientProviderType(ClientProviderType clientProviderType) {
        this.clientProviderType = clientProviderType;
        return this;
    }

    public AbstractLoongCodegenBuilder enableFieldUnderlineStyle(boolean enableFieldUnderlineStyle) {
        this.enableFieldUnderlineStyle = enableFieldUnderlineStyle;
        return this;
    }


    public AbstractLoongCodegenBuilder templateFileVersion(String templateFileVersion) {
        this.templateFileVersion = templateFileVersion;
        return this;
    }

    public AbstractLoongCodegenBuilder templateFileVersion(TemplateFileVersion templateFileVersion) {
        this.templateFileVersion = templateFileVersion.getVersion();
        return this;
    }

    public AbstractLoongCodegenBuilder includeClasses(Class<?>... includeClasses) {
        this.includeClasses.addAll(Arrays.asList(includeClasses));
        return this;
    }


    public AbstractLoongCodegenBuilder ignoreClasses(Class<?>... ignoreClasses) {
        this.ignoreClasses.addAll(Arrays.asList(ignoreClasses));
        return this;
    }

    public AbstractLoongCodegenBuilder ignoreMethods(Map<Class<?>/*类名*/, String[]/*方法名称*/> ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
        return this;
    }

    public AbstractLoongCodegenBuilder languageEnhancedProcessors(LanguageEnhancedProcessor... languageEnhancedProcessors) {
        this.languageEnhancedProcessors.addAll(Arrays.asList(languageEnhancedProcessors));
        return this;
    }

    public AbstractLoongCodegenBuilder otherCodegenClassMetas(CommonCodeGenClassMeta... otherCodegenClassMetas) {
        this.otherCodegenClassMetas.addAll(Arrays.asList(otherCodegenClassMetas));
        return this;
    }

    protected void initTypeMapping() {
        CodegenConfig codegenConfig = CodegenConfig.builder()
                .providerType(this.clientProviderType)
                .languageDescription(this.languageDescription)
                .build();
        CodegenConfigHolder.setConfig(codegenConfig);

        Collection<CodeGenMatcher> codeGenMatchers = this.codeGenMatchers;
        Optional<PackageNameCodeGenMatcher> optionalCodeGenMatcher = codeGenMatchers.stream()
                .filter(codeGenMatcher -> codeGenMatcher instanceof PackageNameCodeGenMatcher)
                .map(codeGenMatcher -> (PackageNameCodeGenMatcher) codeGenMatcher)
                .findFirst();
        if (optionalCodeGenMatcher.isPresent()) {
            PackageNameCodeGenMatcher codeGenMatcher = optionalCodeGenMatcher.get();
            codeGenMatcher.addIgnorePackages(ignorePackages);
        } else {
            PackageNameCodeGenMatcher codeGenMatcher = new PackageNameCodeGenMatcher();
            codeGenMatcher.addIgnorePackages(ignorePackages);
            codeGenMatchers.add(codeGenMatcher);
        }
        List<LanguageEnhancedProcessor> languageEnhancedProcessors = this.languageEnhancedProcessors;
        boolean needAddUmiRequestEnhancedProcessor = !this.containsLanguageEnhancedProcessorType(UmiRequestEnhancedProcessor.class)
                && ClientProviderType.UMI_REQUEST.equals(this.clientProviderType);
        if (needAddUmiRequestEnhancedProcessor) {
            languageEnhancedProcessors.add(new UmiRequestEnhancedProcessor());
        }
        if (!this.containsLanguageEnhancedProcessorType(EnumCommentEnhancer.class)) {
            languageEnhancedProcessors.add(new EnumCommentEnhancer());
        }
        boolean needSetUmiModel = !this.sharedVariables.containsKey("umiModel") && ClientProviderType.UMI_REQUEST.equals(this.clientProviderType);
        if (needSetUmiModel) {
            this.sharedVariables.put("umiModel", UmiModel.OPEN_SOURCE);
        }
    }

    /**
     * 获取模板的共享变量
     *
     * @return
     */
    protected Map<String, Object> getSharedVariables() {
        //全局共享变量
        return sharedVariables;
    }

    protected void initLanguageParser(LanguageParser languageParser) {
        languageParser.addCodeGenMatchers(ExcludeClassCodeGenMatcher.of(ignorePackages, ignoreClasses));
        languageParser.addCodeGenMatchers(this.codeGenMatchers.toArray(new CodeGenMatcher[0]));
        languageParser.setLanguageEnhancedProcessor(
                CombineLanguageEnhancedProcessor.of(this.languageEnhancedProcessors.toArray(new LanguageEnhancedProcessor[0])));
        if (!this.includePackages.isEmpty() || !includeClasses.isEmpty()) {
            // include 模式
            languageParser.addCodeGenMatchers(IncludeClassCodeGenMatcher.of(this.includePackages, includeClasses));
        }
        if (languageParser instanceof AbstractLanguageParser) {
            AbstractLanguageParser<?, ?, ?> abstractLanguageParser = (AbstractLanguageParser<?, ?, ?>) languageParser;
            AbstractLanguageTypeMapping languageTypeMapping = LanguageTypeMappingFactory.builder()
                    .baseTypeMapping(this.baseTypeMapping)
                    .customizeTypeMapping(this.customTypeMapping)
                    .customizeJavaMapping(this.customJavaTypeMapping)
                    .languageParser(languageParser)
                    .languageDescription(languageDescription)
                    .build()
                    .factory();
            abstractLanguageParser.setLanguageTypeMapping(languageTypeMapping);
        }

    }

    protected boolean containsCollectionByType(Collection<?> objects, Class<?> clazz) {
        return objects.stream().anyMatch(ob -> clazz.isAssignableFrom(ob.getClass()));
    }

    private boolean containsLanguageEnhancedProcessorType(Class<? extends LanguageEnhancedProcessor> clazz) {
        return this.containsCollectionByType(languageEnhancedProcessors, clazz);
    }
}
