package com.wuxp.codegen.swagger3.builder;

import com.lmax.disruptor.EventHandler;
import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.event.DisruptorCodeGenPublisher;
import com.wuxp.codegen.core.macth.IgnoreClassCodeGenMatcher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.enums.CodeRuntimePlatform;
import com.wuxp.codegen.languages.AbstractDartParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartBuiltValueFactoryModel;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkDartParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;


/**
 * @author wxup
 */
@Slf4j
public class Swagger3FeignDartCodegenBuilder extends AbstractDragonCodegenBuilder {


    private static final List<DartClassMeta> SUPPORT_ALIAS_TYPES = Arrays.asList(
            DartClassMeta.BUILT_LIST,
            DartClassMeta.BUILT_MAP,
            DartClassMeta.BUILT_SET
    );

    private Map<Class<?>, List<String>> ignoreFields;

    // 类型别名
    private Map<DartClassMeta, List<String>> typeAlias = Collections.emptyMap();

    protected Swagger3FeignDartCodegenBuilder() {
        super();
    }


    public Swagger3FeignDartCodegenBuilder ignoreFields(Map<Class<?>, List<String>> ignoreFields) {
        this.ignoreFields = ignoreFields;
        return this;
    }

    public Swagger3FeignDartCodegenBuilder typeAlias(Map<DartClassMeta, List<String>> typeAlias) {
        this.typeAlias = typeAlias;
        return this;
    }


    public static Swagger3FeignDartCodegenBuilder builder() {
        return new Swagger3FeignDartCodegenBuilder();
    }


    @Override
    public CodeGenerator buildCodeGenerator() {

        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.DART;
        }

        if (this.codeRuntimePlatform == null) {
            this.codeRuntimePlatform = CodeRuntimePlatform.JAVA_SERVER;
        }
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkDartParser(
                packageMapStrategy,
                new Swagger3FeignSdkGenMatchingStrategy(this.ignoreMethods),
                this.codeDetects,
                this.ignoreFields);
        languageParser.addCodeGenMatchers(new IgnoreClassCodeGenMatcher(ignoreClasses));
        languageParser.setLanguageEnhancedProcessor(this.languageEnhancedProcessor);

        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.languageDescription, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory,
                filepath -> {
                    if (StringUtils.hasText(filepath)) {
                        return AbstractDartParser.dartFileNameConverter(filepath);
                    }
                    return filepath;
                });

        Thread mainThread = Thread.currentThread();

        return new Swagger3CodeGenerator(
                this.scanPackages,
                this.ignorePackages,
                this.includeClasses,
                this.ignoreClasses,
                languageParser,
                templateStrategy,
                this.looseMode,
                this.enableFieldUnderlineStyle,
                new DisruptorCodeGenPublisher(new DartFeignCodeGenEventHandler(templateLoader, this.outPath, mainThread)));
    }


    /**
     * 生成事件处理者
     */
    public class DartFeignCodeGenEventHandler implements EventHandler<DisruptorCodeGenPublisher.CodegenEvent<DartClassMeta>> {


        private final List<DartClassMeta> builtList = Arrays.asList(DartClassMeta.BUILT_LIST,
                DartClassMeta.BUILT_MAP,
                DartClassMeta.BUILT_SET,
                DartClassMeta.BUILT_ITERABLE);

        private Set<DartClassMeta> dtos = new HashSet<>();

        private Set<DartClassMeta> feignClients = new HashSet<>();

        private TemplateLoader<Template> templateLoader;

        private String outputPath;

        private Thread mainThread;

        private CombineTypeDescStrategy simpleCombineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

        private CombineTypeDescStrategy dartFullTypeCombineTypeDescStrategy = new DartFullTypeCombineTypeDescStrategy();


        public DartFeignCodeGenEventHandler(TemplateLoader<Template> templateLoader, String outputPath, Thread mainThread) {
            this.templateLoader = templateLoader;
            this.outputPath = outputPath;
            this.mainThread = mainThread;
        }

        @Override
        public void onEvent(DisruptorCodeGenPublisher.CodegenEvent<DartClassMeta> event, long sequence, boolean endOfBatch) throws Exception {

            if (event.isEndEvent()) {
                // 生成成功
                this.buildSerializersFile();
            } else if (event.isCodegenEvent()) {
                DartClassMeta genData = event.getGenData();
                if (genData.getMethodMetas() != null && genData.getMethodMetas().length > 0) {
                    feignClients.add(genData);
                } else {
                    dtos.add(genData);
                }
            } else {
                // 异常
                log.error("gen error ", event.getException());
            }
        }

        /**
         * 生成 serializers
         */
        private void buildSerializersFile() {
            log.info("dto size ===> {}", dtos.size());
            String filename = "serializers";
            Template template = templateLoader.load(filename + ".ftl");
            // 生成路径
            String filepath = this.normalizationFilePath(MessageFormat.format("{0}/{1}", this.outputPath, filename));
            String output = Paths.get(MessageFormat.format("{0}.{1}", filepath, LanguageDescription.DART.getSuffixName())).toString();

            Map<String, Object> data = new HashMap<>();
            data.put("packagePath", MessageFormat.format("{0}{1}", PathResolve.RIGHT_SLASH, filename));
            Set<DartClassMeta> dtos = this.dtos;
            data.put("dependencies", dtos);
            data.put("builderFactories", this.getBuilderFactories());
            // 遍历控制器所有的方法得到泛型的组合
            Writer writer = null;
            try {
                //输出
                writer = new OutputStreamWriter(new FileOutputStream(output),
                        StandardCharsets.UTF_8);
                template.process(data, writer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                log.info("===生成完成，释放主线程===>");
                LockSupport.unpark(this.mainThread);
            }
        }


        /**
         * 获取 built_value builderFactory Set
         */
        private Set<DartBuiltValueFactoryModel> getBuilderFactories() {


            // 将所有的built collections 集合和自定义的 泛型对象的组合返回

            Set<DartBuiltValueFactoryModel> factoryModels = this.feignClients.stream().map(item -> item.getMethodMetas())
                    .map(methodMetas -> Arrays.stream(methodMetas)
                            .map(methodMeta -> methodMeta.getReturnTypes()))
                    .map(stream -> stream.collect(Collectors.toList()))
                    .flatMap(Collection::stream)
                    .filter(returnTypes -> returnTypes.length > 1)
                    .map((returnTypes) -> {

                        // 别名转换
                        List<CommonCodeGenClassMeta> types = new ArrayList();
                        DartClassMeta aliasType = SUPPORT_ALIAS_TYPES.stream().map(dartClassMeta -> {
                            List<String> alias = typeAlias.get(dartClassMeta);
                            if (alias == null) {
                                return null;
                            }
                            boolean match = alias.stream().anyMatch((name) -> {
                                boolean isMath = false;
                                for (int i = 0; i < returnTypes.length; i++) {
                                    if (isMath) {
                                        types.add(returnTypes[i]);
                                        continue;
                                    }
                                    if (name.equals(returnTypes[i].getName())) {
                                        isMath = true;
                                    }
                                }

                                return isMath;
                            });
                            if (match && !types.isEmpty()) {
                                return dartClassMeta;
                            }
                            return null;
                        }).filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null);
                        if (aliasType == null) {
                            List<CommonCodeGenClassMeta[]> list = new ArrayList();
                            list.add(returnTypes);
                            return list;
                        }

                        types.add(0, aliasType);
                        return Arrays.asList(returnTypes, types.toArray(new CommonCodeGenClassMeta[0]));
                    })
                    .flatMap(Collection::stream)
                    .map((returnTypes) -> {
                        // 判断是否为 built的集合对象，集合对象内部是是否存在复杂对象
                        CommonCodeGenClassMeta returnType = returnTypes[0];
//                        boolean isBuiltCollection = builtList.stream()
//                                .map(type -> returnType.getName().startsWith(type.getName()))
//                                .filter(result -> result)
//                                .findFirst()
//                                .orElse(false);
//                        if (!isBuiltCollection) {
//
//                        }
                        // 判断集合中是否有复杂的集合对象
                        boolean isCollection = DartClassMeta.BUILT_LIST.getName().equals(returnType.getName()) ||
                                DartClassMeta.BUILT_SET.getName().equals(returnType.getName()) ||
                                DartClassMeta.BUILT_ITERABLE.getName().equals(returnType.getName());
                        if (isCollection && returnTypes.length > 2) {

                            return Arrays.asList(returnTypes, Arrays.asList(returnTypes).subList(1, returnTypes.length).toArray(new CommonCodeGenClassMeta[0]));
                        }
                        if (DartClassMeta.BUILT_MAP.getName().equals(returnType.getName()) && returnTypes.length > 3) {

                            return Arrays.asList(returnTypes, Arrays.asList(returnTypes).subList(2, returnTypes.length).toArray(new CommonCodeGenClassMeta[0]));
                        }
                        List<CommonCodeGenClassMeta[]> list = new ArrayList();
                        list.add(returnTypes);
                        return list;
                    })
                    .flatMap(Collection::stream)
                    .map(returnTypes -> {
                        // 获取泛型描述
                        String fullTypeCode = this.dartFullTypeCombineTypeDescStrategy.combine(returnTypes);
                        if (fullTypeCode == null) {
                            return null;
                        }
                        String genericDesc = this.simpleCombineTypeDescStrategy.combine(returnTypes);
                        String functionCode = MessageFormat.format(" () => {0}())", getFunctionCode(genericDesc));
                        fullTypeCode = MessageFormat.format("const {0}", fullTypeCode);
                        return new DartBuiltValueFactoryModel(fullTypeCode, functionCode);

                    }).filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            return factoryModels;
        }


        private String getFunctionCode(String originalGenericDesc) {
            Optional<Boolean> isBuiltCollection = builtList.stream()
                    .map(type -> originalGenericDesc.startsWith(type.getName()))
                    .filter(result -> result)
                    .findFirst();
            int first = originalGenericDesc.indexOf("<");
            String type = originalGenericDesc.substring(0, first);
            if (isBuiltCollection.isPresent() && isBuiltCollection.get()) {
                // Built_Value 集合
                type = type.substring(5);
            }
            return MessageFormat.format("{0}Builder{1}", type, originalGenericDesc.substring(first));

        }


        private String normalizationFilePath(String packagePath) {

            return packagePath.replaceAll("\\.", PathResolve.RIGHT_SLASH);
        }
    }


}
