package com.wuxp.codegen.swagger3.builder;

import com.lmax.disruptor.EventHandler;
import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.event.DisruptorCodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.enums.CodeRuntimePlatform;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
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
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import freemarker.template.Template;
import lombok.Builder;
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
import java.util.stream.Stream;


@Builder
@Slf4j
public class Swagger3FeignDartCodegenBuilder extends AbstractDragonCodegenBuilder {


    protected Swagger3FeignDartCodegenBuilder() {
        super();
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
                this.codeDetects);


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

        return new Swagger3CodeGenerator(this.scanPackages, languageParser, templateStrategy, this.enableFieldUnderlineStyle,
                new DisruptorCodeGenPublisher(new DartFeignCodeGenEventHandler(templateLoader, this.outPath, mainThread)));
    }


    public class DartFeignCodeGenEventHandler implements EventHandler<DisruptorCodeGenPublisher.CodegenEvent<DartClassMeta>> {


        private Set<DartClassMeta> dtos = new HashSet<>();

        private Set<DartClassMeta> feignClients = new HashSet<>();

        private TemplateLoader<Template> templateLoader;

        private String outputPath;

        private Thread mainThread;

        private CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();


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
                    .map(returnTypes -> {

                        // 获取泛型描述
                        String genericDesc = this.combineTypeDescStrategy.combine(returnTypes);
                        String fullTypeCode = this.getFullTypeCode(genericDesc);
                        if (fullTypeCode == null) {
                            return null;
                        }
                        String functionCode = MessageFormat.format(" () => {0}())", getFunctionCode(genericDesc));
                        return new DartBuiltValueFactoryModel(fullTypeCode, functionCode);

                    }).filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            return factoryModels;
        }

        /**
         * 获取 FullType Code
         * <p>
         * 替换示例
         * List<Map<String, List<User>>>
         * ==>
         * FullType(List<Map<String, List<User>>>)
         * FullType(List [FullType(Map<String, List<User>>]))
         * FullType(List [FullType(Map[FullType(String), FullType(List<User>>)]))
         * FullType(List,[FullType(Map,[ FullType(String),FullType( List,[FullType(User)])])])
         *
         * @param originalGenericDesc 泛型描述 例如 Map<String,String>
         * @return
         */
        private String getFullTypeCode(String originalGenericDesc) {

//            originalGenericDesc = "BuiltMap<PageInfo<User>,BuiltList<PageInfo<User>>>";
            String value = new String(originalGenericDesc);
            Stack<String> typeStacks = new Stack<>();

            //分割出所有的 类型
            int start = -1;
            while ((start = value.indexOf("<")) > -1) {
                value = value.substring(start + 1, value.length() - 1);
                if (value.indexOf(">,") > 0) {
                    // 存在复合类型的key 不支持
                    return null;
                }
                typeStacks.push(value);
            }

            String fullTypeFormat = "FullType({0})";
            Stack<String[]> tempStack = new Stack<>();
            while (!typeStacks.empty()) {
                String key = typeStacks.pop();
                String typeStr = new String(key);
                if (!tempStack.empty()) {
                    String[] items = tempStack.pop();
                    typeStr = typeStr.replaceAll(items[0], items[1]);
                }
                String type;
                int leftArrowIndex = typeStr.indexOf("<");
                if ((typeStr.indexOf(",") < leftArrowIndex) || leftArrowIndex < 0) {
                    type = Arrays.stream(typeStr.split(","))
                            .map(text -> {
                                if (text.startsWith("FullType")) {
                                    return text;
                                }
                                return MessageFormat.format(fullTypeFormat, text);
                            })
                            .collect(Collectors.joining(","));
                } else {
                    type = MessageFormat.format(fullTypeFormat, typeStr);
                }
                tempStack.push(new String[]{key, type});
            }
            String[] items = tempStack.pop();
            String returnValue = MessageFormat.format(fullTypeFormat, originalGenericDesc.replaceAll(items[0], items[1]))
                    .replaceAll("\\<", ",[")
                    .replaceAll("\\>", "]");
            return returnValue;
        }

        private final List<DartClassMeta> builtList = Arrays.asList(DartClassMeta.BUILT_LIST,
                DartClassMeta.BUILT_MAP,
                DartClassMeta.BUILT_SET,
                DartClassMeta.BUILT_ITERABLE);

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

//        private Stack<String> spiltTypes(String originalGenericDesc) {
//            String[] chars = originalGenericDesc.split("");
//            List<String> results = new ArrayList<>(chars.length);
//            boolean startPush = false, startPop = false;
//            // Map<Page<User<Login>>,List<User<Map<String,Long>>>>
//            for (String c : chars) {
//                if (!startPush) {
//                    startPush = "<".equals(c);
//                }
//                if (!startPop) {
//                    startPop = ">".equals(c);
//                }
//                if (startPush) {
//                    results.add(c);
//                } else {
//
//                }
//            }
//
//
//            return null;
//        }

        private String normalizationFilePath(String packagePath) {

            return packagePath.replaceAll("\\.", PathResolve.RIGHT_SLASH);
        }
    }


}
