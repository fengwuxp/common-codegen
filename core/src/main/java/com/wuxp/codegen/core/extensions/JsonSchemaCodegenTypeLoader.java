package com.wuxp.codegen.core.extensions;

import com.alibaba.fastjson.JSON;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.extensions.*;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.dart.DartFieldMate;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通过解析json文件转换为{@link CommonCodeGenClassMeta}对象
 *
 * @author wuxp
 */
@Slf4j
public class JsonSchemaCodegenTypeLoader implements CodegenTypeLoader<CommonCodeGenClassMeta> {

    private static final Map<LanguageDescription, Class<? extends CommonCodeGenClassMeta>> CODEGEN_CLASSES = new EnumMap<>(LanguageDescription.class);

    private static final Map<CodegenSimpleType, TypescriptClassMeta> TYPESCRIPT_TYPE_MAP = new EnumMap<>(CodegenSimpleType.class);

    private static final Map<CodegenSimpleType, DartClassMeta> DART_TYPE_MAP = new EnumMap<>(CodegenSimpleType.class);

    private static final Map<CodegenSimpleType, JavaCodeGenClassMeta> JAVA_TYPE_MAP = new EnumMap<>(CodegenSimpleType.class);

    /**
     * 缓存已经解析好的类型
     *
     * @key 类名全路径
     * @value CommonCodeGenClassMeta
     */
    private static final Map<String, CommonCodeGenClassMeta> CACHE_CODEGEN_CLASSES = new HashMap<>();

    static {
        CODEGEN_CLASSES.put(LanguageDescription.TYPESCRIPT, TypescriptClassMeta.class);
        CODEGEN_CLASSES.put(LanguageDescription.DART, DartClassMeta.class);
        CODEGEN_CLASSES.put(LanguageDescription.JAVA, JavaCodeGenClassMeta.class);
        CODEGEN_CLASSES.put(LanguageDescription.JAVA_ANDROID, JavaCodeGenClassMeta.class);


        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.INT, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.INT64, TypescriptClassMeta.STRING);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.STRING, TypescriptClassMeta.STRING);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.BOOLEAN, TypescriptClassMeta.BOOLEAN);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.ARRAY, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.LIST, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.SET, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_TYPE_MAP.put(CodegenSimpleType.MAP, TypescriptClassMeta.RECORD);

        DART_TYPE_MAP.put(CodegenSimpleType.INT, DartClassMeta.INT);
        DART_TYPE_MAP.put(CodegenSimpleType.INT64, DartClassMeta.STRING);
        DART_TYPE_MAP.put(CodegenSimpleType.STRING, DartClassMeta.STRING);
        DART_TYPE_MAP.put(CodegenSimpleType.BOOLEAN, DartClassMeta.BOOL);
        DART_TYPE_MAP.put(CodegenSimpleType.ARRAY, DartClassMeta.BUILT_LIST);
        DART_TYPE_MAP.put(CodegenSimpleType.LIST, DartClassMeta.BUILT_LIST);
        DART_TYPE_MAP.put(CodegenSimpleType.SET, DartClassMeta.BUILT_LIST);
        DART_TYPE_MAP.put(CodegenSimpleType.MAP, DartClassMeta.BUILT_MAP);

        JAVA_TYPE_MAP.put(CodegenSimpleType.INT, JavaCodeGenClassMeta.INTEGER);
        JAVA_TYPE_MAP.put(CodegenSimpleType.INT64, JavaCodeGenClassMeta.LONG);
        JAVA_TYPE_MAP.put(CodegenSimpleType.STRING, JavaCodeGenClassMeta.STRING);
        JAVA_TYPE_MAP.put(CodegenSimpleType.BOOLEAN, JavaCodeGenClassMeta.STRING);
        JAVA_TYPE_MAP.put(CodegenSimpleType.ARRAY, JavaCodeGenClassMeta.LIST);
        JAVA_TYPE_MAP.put(CodegenSimpleType.LIST, JavaCodeGenClassMeta.LIST);
        JAVA_TYPE_MAP.put(CodegenSimpleType.SET, JavaCodeGenClassMeta.SET);
        JAVA_TYPE_MAP.put(CodegenSimpleType.MAP, JavaCodeGenClassMeta.MAP);
    }


    private final List<File> jsonFiles;

    private final LanguageDescription language;

    private final PackageMapStrategy packageMapStrategy;


    public JsonSchemaCodegenTypeLoader(String filepath, LanguageDescription language, PackageMapStrategy packageMapStrategy) {
        this(getJsonFiles(filepath), language, packageMapStrategy);
    }

    public JsonSchemaCodegenTypeLoader(List<File> jsonFiles, LanguageDescription language, PackageMapStrategy packageMapStrategy) {
        this.jsonFiles = jsonFiles;
        this.language = language;
        this.packageMapStrategy = packageMapStrategy;
    }

    @Override
    public List<CommonCodeGenClassMeta> load() {

        return jsonFiles.stream().map(this::transformToCodegenModel)
                .filter(Objects::nonNull)
                .sorted((o1, o2) -> o2.getOrder() - o1.getOrder())
                .map(this::converterCodegenClassMeta)
                .collect(Collectors.toList());
    }


    private CommonCodeGenClassMeta converterCodegenClassMeta(SchemaCodegenModel model) {
        CommonCodeGenClassMeta classMeta = newCodegenClassInstance();
        classMeta.setSource(model.getSource());
        classMeta.setNeedImport(model.getNeedImport());
        classMeta.setNeedGenerate(model.getNeedGenerate());
        classMeta.setAnnotations(new CommonCodeGenAnnotation[0]);
        classMeta.setIsFinal(false);
        classMeta.setIsStatic(false);
        classMeta.setIsAbstract(false);
        classMeta.setName(model.getName());
        classMeta.setGenericDescription(model.getGenericDescription());
        ArrayList<String> uris = new ArrayList<>(Arrays.asList(model.getPackagePath().split("/")));
        uris.add(classMeta.getName());
        classMeta.setPackagePath(packageMapStrategy.genPackagePath(uris.toArray(new String[0])));
        classMeta.setClassType(model.getClassType());
        classMeta.setAccessPermission(AccessPermission.PUBLIC);
        List<SchemaCodegenModelTypeVariable> typeVariables = model.getTypeVariables();
        if (!CollectionUtils.isEmpty(typeVariables)) {
            String text = JSON.toJSONString(typeVariables);
            Class<? extends CommonCodeGenClassMeta> codegenClass = CODEGEN_CLASSES.get(language);
            classMeta.setTypeVariables(JSON.parseArray(text, codegenClass).toArray(new CommonCodeGenClassMeta[0]));
        } else {
            classMeta.setTypeVariables(new CommonCodeGenClassMeta[0]);
        }
        List<SchemaCodegenModelFieldMeta> fieldMetas = model.getFieldMetas();
        if (!CollectionUtils.isEmpty(fieldMetas)) {
            classMeta.setFieldMetas(fieldMetas.stream().map(fieldMeta -> this.converterFiledMeta(fieldMeta, classMeta)).toArray(CommonCodeGenFiledMeta[]::new));
        } else {
            classMeta.setFieldMetas(new CommonCodeGenFiledMeta[0]);
        }
        classMeta.setMethodMetas(new CommonCodeGenMethodMeta[0]);
        CACHE_CODEGEN_CLASSES.put(model.getSource().getName(), classMeta);
        return classMeta;
    }

    private CommonCodeGenFiledMeta converterFiledMeta(SchemaCodegenModelFieldMeta fieldMeta, CommonCodeGenClassMeta classMeta) {
        CommonCodeGenFiledMeta filed = newCodegenFileInstance(fieldMeta);
        filed.setName(fieldMeta.getName());
        filed.setEnumFiledValues(fieldMeta.getEnumFiledValues());
        filed.setAnnotations(new CommonCodeGenAnnotation[0]);
        filed.setTypeVariables(new CommonCodeGenClassMeta[0]);
        filed.setName(filed.getName());
        filed.setAccessPermission(AccessPermission.PRIVATE);
        filed.setIsFinal(false);
        filed.setIsStatic(false);
        filed.setFiledTypes(getFiledTypes(fieldMeta, classMeta));
        return filed;
    }

    private CommonCodeGenClassMeta[] getFiledTypes(SchemaCodegenModelFieldMeta meta, CommonCodeGenClassMeta classMeta) {
        String type = meta.getType();
        boolean isArray = type.endsWith("[]");
        if (isArray) {
            type = type.replace("[]", "");
        }
        List<CommonCodeGenClassMeta> results = new ArrayList<>();
        if (type.startsWith("$")) {
            // 引用
            type = type.substring(1);
            CommonCodeGenClassMeta cacheMeta = CACHE_CODEGEN_CLASSES.get(type);
            CommonCodeGenClassMeta commonCodeGenClassMeta = newCodegenClassInstance();
            BeanUtils.copyProperties(cacheMeta, commonCodeGenClassMeta);
            results.add(commonCodeGenClassMeta);
            Map<String, CommonCodeGenClassMeta> dependencies = (Map<String, CommonCodeGenClassMeta>) classMeta.getDependencies();
            dependencies.put(commonCodeGenClassMeta.getName(), commonCodeGenClassMeta);
        } else {
            CodegenSimpleType codegenSimpleType = CodegenSimpleType.valueOf(type.toUpperCase());
            results.add(this.getCodegenClassByType(codegenSimpleType));
        }

        if (isArray) {
            results.add(TypescriptClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);
        }
        return results.toArray(new CommonCodeGenClassMeta[0]);

    }


    private CommonCodeGenClassMeta getCodegenClassByType(CodegenSimpleType type) {
        switch (language) {
            case TYPESCRIPT:
                return TYPESCRIPT_TYPE_MAP.get(type);
            case DART:
                return DART_TYPE_MAP.get(type);
            case JAVA:
            case JAVA_ANDROID:
                return JAVA_TYPE_MAP.get(type);
            default:
                throw new RuntimeException("not support language：" + language);
        }

    }


    private CommonCodeGenClassMeta newCodegenClassInstance() {
        switch (language) {
            case TYPESCRIPT:
                return new TypescriptClassMeta();
            case DART:
                return new DartClassMeta();
            case JAVA:
            case JAVA_ANDROID:
                return new JavaCodeGenClassMeta();
            default:
                throw new RuntimeException("not support language：" + language);
        }
    }

    private CommonCodeGenFiledMeta newCodegenFileInstance(SchemaCodegenModelFieldMeta fieldMet) {
        switch (language) {
            case TYPESCRIPT:
                TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();
                typescriptFieldMate.setRequired(fieldMet.isRequired());
                return typescriptFieldMate;
            case DART:
                DartFieldMate dartFieldMate = new DartFieldMate();
                dartFieldMate.setRequired(fieldMet.isRequired());
                return dartFieldMate;
            case JAVA:
            case JAVA_ANDROID:
            default:
                return new CommonCodeGenFiledMeta();
        }
    }

    private SchemaCodegenModel transformToCodegenModel(File file) {
        String name = file.getName();
        SchemaCodegenModel model = null;
        try {
            model = JSON.parseObject(FileUtils.readFileToString(file, StandardCharsets.UTF_8), SchemaCodegenModel.class);
            model.setSource(Thread.currentThread().getContextClassLoader().loadClass(name.substring(0, name.lastIndexOf("."))));
        } catch (IOException exception) {
            if (log.isDebugEnabled()) {
                log.debug("加载codegen class meta 失败，{}", exception.getMessage(), exception);
            }
        }catch (ClassNotFoundException exception){
            if (log.isTraceEnabled()) {
                log.trace("加载codegen class meta 失败，{}", exception.getMessage(), exception);
            }
        }
        return model;
    }


    private static List<File> getJsonFiles(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        List<File> jsonFiles = new ArrayList<>();
        if (file.isDirectory()) {
            jsonFiles.addAll(FileUtils.listFiles(file, new String[]{"json"}, true));
        } else {
            jsonFiles.add(file);
        }
        return jsonFiles;
    }
}
