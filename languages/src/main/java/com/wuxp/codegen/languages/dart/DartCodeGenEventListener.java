package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.event.CodeGenEvent;
import com.wuxp.codegen.core.event.CodeGenEventListener;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.dart.DartBuiltValueFactoryModel;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.TableRowSorter;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于 dart 代码生成事件监听
 */
@Slf4j
public class DartCodeGenEventListener implements CodeGenEventListener {

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

    /**
     * sdk索引文件名称
     */
    private final String feignSdkLibName;

    /**
     * 类型别名
     *
     * @key 类型
     * @value 别名列表
     */
    private final Map<DartClassMeta, List<String>> typeAlias;


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

    public DartCodeGenEventListener(String feignSdkLibName, Map<DartClassMeta, List<String>> typeAlias) {
        this.feignSdkLibName = feignSdkLibName == null ? "feign_sdk" : feignSdkLibName;
        this.typeAlias = new TreeMap<>(typeAlias);
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
                .filter(returnTypes -> returnTypes.length > 1)
                .map(this::converterTypeAliasName)
                .flatMap(Collection::stream)
                .map(this::getBuiltCollectionTypes)
                .flatMap(Collection::stream)
                .map(this::buildBuiltValueFactoryModel)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private List<CommonCodeGenClassMeta[]> flatMetaReturnTypes(CommonCodeGenMethodMeta[] methodMetas) {
        return Arrays.stream(methodMetas)
                .map(CommonCodeGenMethodMeta::getReturnTypes).collect(Collectors.toList());
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

    private List<CommonCodeGenClassMeta[]> converterTypeAliasName(CommonCodeGenClassMeta[] returnTypes) {
        // 别名转换
        List<CommonCodeGenClassMeta> types = new ArrayList<>();
        DartClassMeta aliasType = SUPPORT_ALIAS_TYPES.stream()
                .map(classMeta -> {
                    List<CommonCodeGenClassMeta> typeAliasNames = getTypeAliasNames(returnTypes, classMeta);
                    if (typeAliasNames.isEmpty()) {
                        return null;
                    }
                    types.addAll(typeAliasNames);
                    return classMeta;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (aliasType == null) {
            List<CommonCodeGenClassMeta[]> list = new ArrayList<>();
            list.add(returnTypes);
            return list;
        }

        types.add(0, aliasType);
        return Arrays.asList(returnTypes, types.toArray(new CommonCodeGenClassMeta[0]));
    }

    private List<CommonCodeGenClassMeta> getTypeAliasNames(CommonCodeGenClassMeta[] returnTypes, DartClassMeta classMeta) {
        List<String> aliasNames = typeAlias.get(classMeta);
        if (aliasNames == null) {
            return Collections.emptyList();
        }
        List<CommonCodeGenClassMeta> result = new ArrayList<>();
        aliasNames.forEach(aliasName -> {
            boolean isMath = false;
            for (CommonCodeGenClassMeta returnType : returnTypes) {
                if (isMath) {
                    result.add(returnType);
                    continue;
                }
                if (aliasName.equals(returnType.getName())) {
                    isMath = true;
                }
            }
        });
        return result;
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
