package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.event.CodeGenEvent;
import com.wuxp.codegen.core.event.CodeGenEventListener;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.dart.DartBuiltValueFactoryModel;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.CommonCodeGenClassMeta.TYPE_VARIABLE_NAME;

/**
 * 基于 dart 代码生成事件监听
 */
@Slf4j
public class DartCodeGenEventListener implements CodeGenEventListener {

    /**
     * Page 分页对象的标记
     */
    public static final DartClassMeta PAGE_REF = new DartClassMeta("Page", "Page<T>", ClassType.CLASS, true, null, null);

    private static final List<DartClassMeta> SUPPORT_ALIAS_TYPES = Arrays.asList(
            DartClassMeta.BUILT_LIST,
            DartClassMeta.BUILT_MAP,
            DartClassMeta.BUILT_SET
    );

    private static final List<DartClassMeta> BUILT_COLLECTION_TYPES = Arrays.asList(
            DartClassMeta.BUILT_LIST,
            DartClassMeta.BUILT_MAP,
            DartClassMeta.BUILT_SET,
            DartClassMeta.BUILT_ITERABLE);

    private static final String DEPENDENCIES_TAG_NAME = "dependencies";

    private static final String SDK_LIB_TAG_NAME = "sdkLibName";

    private static final String SDK_NAME = "feign_sdk";

    /**
     * sdk索引文件名称
     */
    private final String feignSdkLibName;

    /**
     * 使用 {@link SortedSet} 保证同样的文件输出的顺序是一致的
     */
    private final Set<CommonCodeGenClassMeta> eventCodeGenMetas = new TreeSet<>();

    // dto 对象
    private final Set<CommonCodeGenClassMeta> dtoMetas = new TreeSet<>();

    // feign client 对象
    private final Set<CommonCodeGenClassMeta> feignClientMetas = new TreeSet<>();

    private final CombineTypeDescStrategy simpleCombineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

    private final CombineTypeDescStrategy dartFullTypeCombineTypeDescStrategy = new DartFullTypeCombineTypeDescStrategy();

    public DartCodeGenEventListener(String feignSdkLibName) {
        this.feignSdkLibName = StringUtils.hasText(feignSdkLibName) ? feignSdkLibName : SDK_NAME;
    }

    @Override
    public void onApplicationEvent(CodeGenEvent event) {
        if (CodeGenEvent.CodeGenEventStatus.SCAN_CODEGEN_DONE.equals(event.getStatus())) {
            // 生成结束
            buildEventMetas();
        }
        if (CodeGenEvent.CodeGenEventStatus.SCAN_CODEGEN.equals(event.getStatus())) {
            putEventMeta(event);
        }
    }

    private void buildEventMetas() {
        if (dtoMetas.isEmpty() && feignClientMetas.isEmpty()) {
            return;
        }
        this.eventCodeGenMetas.add(buildSerializersMeta());
        this.eventCodeGenMetas.add(buildSkdReflectMeta());
        this.eventCodeGenMetas.add(buildSdkIndexMeta());
        this.eventCodeGenMetas.forEach(meta -> {
            meta.setNeedGenerate(true);
            meta.getTags().put(EVENT_CODEGEN_META_TAG_NAME, true);
        });
    }

    private void putEventMeta(CodeGenEvent event) {
        CommonCodeGenClassMeta genData = event.getSource();
        boolean isClientObject = genData.getMethodMetas() != null && genData.getMethodMetas().length > 0;
        if (isClientObject) {
            feignClientMetas.add(genData);
        } else {
            dtoMetas.add(genData);
        }
    }

    private DartClassMeta buildSerializersMeta() {
        DartClassMeta result = new DartClassMeta();
        Map<String, Object> tags = new HashMap<>();
        tags.put(DEPENDENCIES_TAG_NAME, this.dtoMetas);
        tags.put(SDK_LIB_TAG_NAME, feignSdkLibName);
        tags.put("builderFactories", this.getBuilderFactories());
        tags.put(TEMPLATE_PATH_TAG_NAME, "serializers");
        result.setTags(tags);
        result.setPackagePath("/serializers");
        return result;
    }

    private DartClassMeta buildSkdReflectMeta() {
        DartClassMeta result = new DartClassMeta();
        Map<String, Object> tags = new HashMap<>();
        Set<CommonCodeGenClassMeta> dependencies = new TreeSet<>();
        dependencies.addAll(feignClientMetas);
        dependencies.addAll(dtoMetas);
        tags.put(DEPENDENCIES_TAG_NAME, dependencies);
        tags.put(SDK_LIB_TAG_NAME, feignSdkLibName);
        result.setTags(tags);
        tags.put(TEMPLATE_PATH_TAG_NAME, "feign_sdk");
        // 生成到 lib 目录下
        result.setPackagePath(String.format("../%s", feignSdkLibName));
        return result;
    }

    private DartClassMeta buildSdkIndexMeta() {
        DartClassMeta result = new DartClassMeta();
        Map<String, Object> tags = new HashMap<>();
        Set<CommonCodeGenClassMeta> dependencies = new TreeSet<>();
        dependencies.addAll(feignClientMetas);
        dependencies.addAll(dtoMetas);
        tags.put(DEPENDENCIES_TAG_NAME, dependencies);
        tags.put(SDK_LIB_TAG_NAME, feignSdkLibName);
        tags.put(TEMPLATE_PATH_TAG_NAME, "index");
        result.setTags(tags);
        // 生成到 lib 目录下
        result.setPackagePath("../index");
        return result;
    }

    @Override
    public Set<CommonCodeGenClassMeta> getEventCodeGenMetas() {
        return eventCodeGenMetas;
    }

    /**
     * 获取 built_value builderFactory
     */
    private Set<DartBuiltValueFactoryModel> getBuilderFactories() {
        // 将所有的 built collections 集合和自定义的 泛型对象的组合返回
        return this.feignClientMetas.stream()
                .map(CommonCodeGenClassMeta::getMethodMetas)
                .map(this::flatMetaReturnTypes)
                .flatMap(Collection::stream)
                // 返回值类型大于 1，表示有泛型存在
                .filter(returnTypes -> returnTypes.length > 1)
                .map(returnTypes -> Arrays.stream(returnTypes).collect(Collectors.toList()))
                .map(this::recursiveReturnTypes)
                .flatMap(Collection::stream)
                .map(this::getBuiltCollectionTypes)
                .flatMap(Collection::stream)
                .map(this::buildBuiltValueFactoryModel)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private List<CommonCodeGenClassMeta[]> flatMetaReturnTypes(CommonCodeGenMethodMeta[] methodMetas) {
        return Arrays.stream(methodMetas)
                .map(CommonCodeGenMethodMeta::getReturnTypes)
                .collect(Collectors.toList());
    }

    /**
     * 递归解析返回值类型中的泛型组合
     * 例如
     * <code>
     * public class Page<E> {
     * private List<E> records
     * }
     * ==>
     * {Page<XXX>,List<XXX>}
     * </code>
     *
     * @param classMetas 返回值泛型描述
     * @return 用于生成序列化工厂代码的类型描述集合
     */
    private List<CommonCodeGenClassMeta[]> recursiveReturnTypes(List<CommonCodeGenClassMeta> classMetas) {
        List<CommonCodeGenClassMeta[]> result = new ArrayList<>();
        result.add(classMetas.toArray(new CommonCodeGenClassMeta[0]));
        CommonCodeGenClassMeta wrapperType = classMetas.get(0);
        if (wrapperType.getSource() == null || !JavaTypeUtils.isNoneJdkComplex(wrapperType.getSource())) {
            // 说明 returnTypes 为 List<E> Map<K,V>、Set<E> 等 jdk 的泛型类型
            return result;
        }
        List<CommonCodeGenClassMeta[]> metas = Arrays.stream(wrapperType.getFieldMetas())
                .map(CommonCodeGenFiledMeta::getFiledTypes)
                // 字段中存在泛型
                .filter(filedTypes -> filedTypes.length > 1)
                .map(filedTypes -> Arrays.stream(filedTypes).collect(Collectors.toList()))
                .map(filedTypes -> {
                    boolean hasTypeVariable = filedTypes.stream().anyMatch(filedType -> filedType.getName().equals(TYPE_VARIABLE_NAME));
                    if (hasTypeVariable) {
                        // 类型中存在的泛型变量，需要替换为具体的泛型变量
                        List<CommonCodeGenClassMeta> types = new ArrayList<>();
                        types.add(filedTypes.get(0));
                        // 替换泛型为具体的类型
                        types.addAll(classMetas.subList(1, classMetas.size()));
                        return types;
                    }
                    return filedTypes;
                })
                // 递归解析字段中的类型
                .map(this::recursiveReturnTypes)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        result.addAll(metas);
        return result;
    }

    private DartBuiltValueFactoryModel buildBuiltValueFactoryModel(CommonCodeGenClassMeta[] returnTypes) {
        // 获取泛型描述
        String fullTypeCode = this.dartFullTypeCombineTypeDescStrategy.combine(returnTypes);
        if (fullTypeCode == null) {
            return null;
        }
        String genericDesc = this.simpleCombineTypeDescStrategy.combine(returnTypes);
        String functionCode = MessageFormat.format(" () => {0}())", getFunctionCode(genericDesc));
        fullTypeCode = MessageFormat.format("const {0}", fullTypeCode);
        return new DartBuiltValueFactoryModel(fullTypeCode, functionCode);
    }

    private List<CommonCodeGenClassMeta[]> getBuiltCollectionTypes(CommonCodeGenClassMeta[] returnTypes) {
        // 判断是否为 built 的集合对象，集合对象内部是是否存在复杂对象
        CommonCodeGenClassMeta returnType = returnTypes[0];
        // 判断集合中是否有复杂的集合对象
        if (isBuiltCollectionType(returnType) && returnTypes.length > 2) {
            return Arrays.asList(returnTypes, Arrays.copyOfRange(returnTypes, 1, returnTypes.length));
        }
        if (DartClassMeta.BUILT_MAP.getName().equals(returnType.getName()) && returnTypes.length > 3) {
            return Arrays.asList(returnTypes, Arrays.copyOfRange(returnTypes, 2, returnTypes.length));
        }
        List<CommonCodeGenClassMeta[]> result = new ArrayList<>();
        result.add(returnTypes);
        return result;
    }

    private boolean isBuiltCollectionType(CommonCodeGenClassMeta returnType) {
        return DartClassMeta.BUILT_LIST.getName().equals(returnType.getName()) ||
                DartClassMeta.BUILT_SET.getName().equals(returnType.getName()) ||
                DartClassMeta.BUILT_ITERABLE.getName().equals(returnType.getName());
    }


    private String getFunctionCode(String originalGenericDesc) {
        Optional<Boolean> isBuiltCollection = BUILT_COLLECTION_TYPES.stream()
                .map(type -> originalGenericDesc.startsWith(type.getName()))
                .filter(result -> result)
                .findFirst();
        int firstIndex = originalGenericDesc.indexOf("<");
        String type = originalGenericDesc.substring(0, firstIndex);
        if (isBuiltCollection.isPresent() && Boolean.TRUE.equals(isBuiltCollection.get())) {
            // Built_Value 集合
            type = type.substring(5);
        }
        return MessageFormat.format("{0}Builder{1}", type, originalGenericDesc.substring(firstIndex));
    }
}
