package com.wuxp.codegen.disruptor;

import com.lmax.disruptor.EventHandler;
import com.wuxp.codegen.core.event.DisruptorCodeGenPublisher;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartBuiltValueFactoryModel;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.templates.TemplateLoader;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import com.wuxp.codegen.util.FileUtils;
import freemarker.template.Template;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * 生成事件处理者
 *
 * @author wuxp
 */
@Slf4j
@Setter
public class DartFeignCodeGenEventHandler implements EventHandler<DisruptorCodeGenPublisher.CodegenEvent<DartClassMeta>> {

    private static final List<DartClassMeta> SUPPORT_ALIAS_TYPES = Arrays.asList(
            DartClassMeta.BUILT_LIST,
            DartClassMeta.BUILT_MAP,
            DartClassMeta.BUILT_SET
    );

    /**
     * sdk索引文件名称
     */
    private String sdkIndexFileName = "feign_sdk";

    /**
     * 类型别名
     */
    private Map<DartClassMeta, List<String>> typeAlias = Collections.emptyMap();


    private final List<DartClassMeta> builtList = Arrays.asList(DartClassMeta.BUILT_LIST,
            DartClassMeta.BUILT_MAP,
            DartClassMeta.BUILT_SET,
            DartClassMeta.BUILT_ITERABLE);

    // dto 对象
    private Set<DartClassMeta> dtos = new HashSet<>();

    // feign client 对象
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
            // 生成结束
            if (!dtos.isEmpty() || !feignClients.isEmpty()) {
                this.buildSerializersFile();
                this.buildSkdReflectFile();
                this.buildSdkIndexFile();
            }
            log.info("===生成完成，释放主线程===>");
            LockSupport.unpark(this.mainThread);
        } else if (event.isCodegenEvent()) {
            DartClassMeta genData = event.getGenData();
            boolean isClientObject = genData.getMethodMetas() != null && genData.getMethodMetas().length > 0;
            if (isClientObject) {
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
        buildFile(template, output, data);
    }

    /**
     * build sdk reflectable 文件
     */
    public void buildSkdReflectFile() {
        log.info("feignClients size ===> {}", feignClients.size());

        Template template = templateLoader.load("feign_sdk.ftl");
        // 生成路径
        String outputPath = this.outputPath.substring(0, this.outputPath.lastIndexOf(File.separator));
        String filename = sdkIndexFileName;
        String filepath = this.normalizationFilePath(MessageFormat.format("{0}/{1}", outputPath, filename));
        String output = Paths.get(MessageFormat.format("{0}.{1}", filepath, LanguageDescription.DART.getSuffixName())).toString();

        Map<String, Object> data = new HashMap<>();
        data.put("packagePath", MessageFormat.format("{0}{1}", PathResolve.RIGHT_SLASH, filename));
        Set<DartClassMeta> feignClients = this.feignClients;
        data.put("dependencies", feignClients);
        data.put("sdkLibName", sdkIndexFileName);
        // 遍历控制器所有的方法得到泛型的组合
        buildFile(template, output, data);
    }

    /**
     * build sdk index 文件
     */
    public void buildSdkIndexFile() {
        String filename = "index";
        Template template = templateLoader.load(filename + ".ftl");
        // 生成路径
        String outputPath = this.outputPath.substring(0, this.outputPath.lastIndexOf(File.separator));
        String filepath = this.normalizationFilePath(MessageFormat.format("{0}/{1}", outputPath, filename));
        String output = Paths.get(MessageFormat.format("{0}.{1}", filepath, LanguageDescription.DART.getSuffixName())).toString();

        Map<String, Object> data = new HashMap<>();
        data.put("packagePath", MessageFormat.format("{0}{1}", PathResolve.RIGHT_SLASH, filename));
        Set<DartClassMeta> feignClients = this.feignClients;
        Set<DartClassMeta> dtos = this.dtos;
        Set<DartClassMeta> dependencies = new HashSet<>(feignClients.size() + dtos.size());
        dependencies.addAll(feignClients);
        dependencies.addAll(dtos);
        data.put("dependencies", dependencies);
        data.put("sdkLibName", sdkIndexFileName);
        // 遍历控制器所有的方法得到泛型的组合
        FileUtils.createDirectory(output.substring(0, output.lastIndexOf(File.separator)));
        buildFile(template, output, data);
    }

    private void buildFile(Template template, String output, Map<String, Object> data) {
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
        }
    }


    /**
     * 获取 built_value builderFactory Set
     */
    @SuppressWarnings("rawtypes")
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
                    // 判断集合中是否有复杂的集合对象
                    boolean isCollection = DartClassMeta.BUILT_LIST.getName().equals(returnType.getName()) ||
                            DartClassMeta.BUILT_SET.getName().equals(returnType.getName()) ||
                            DartClassMeta.BUILT_ITERABLE.getName().equals(returnType.getName());
                    if (isCollection && returnTypes.length > 2) {

                        return Arrays
                                .asList(returnTypes, Arrays.asList(returnTypes).subList(1, returnTypes.length).toArray(new CommonCodeGenClassMeta[0]));
                    }
                    if (DartClassMeta.BUILT_MAP.getName().equals(returnType.getName()) && returnTypes.length > 3) {

                        return Arrays
                                .asList(returnTypes, Arrays.asList(returnTypes).subList(2, returnTypes.length).toArray(new CommonCodeGenClassMeta[0]));
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