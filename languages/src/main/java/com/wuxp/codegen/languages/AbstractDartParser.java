package com.wuxp.codegen.languages;


import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.spring.*;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.DartLanguageMetaInstanceFactory;
import com.wuxp.codegen.mapping.DartTypeMapping;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.dart.DartFieldMate;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor.FEIGN_CLIENT_ANNOTATION_NAME;
import static com.wuxp.codegen.model.languages.dart.DartClassMeta.BUILT_SERIALIZERS;

/**
 * 抽象的dart parser
 *
 * @author wxup
 */
@Slf4j
public class AbstractDartParser extends AbstractLanguageParser<DartClassMeta, CommonCodeGenMethodMeta, DartFieldMate> {


    private Map<Class<?>, List<String>> ignoreFields;

    private static final String RIGHT_SLASH = "/";

    static {
        ANNOTATION_PROCESSOR_MAP.put(CookieValue.class, new CookieValueProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestBody.class, new RequestBodyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestHeader.class, new RequestHeaderProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestParam.class, new RequestParamProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestPart.class, new RequestPartProcessor());
        ANNOTATION_PROCESSOR_MAP.put(PathVariable.class, new PathVariableProcessor());
    }

    public AbstractDartParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects,
                              Map<Class<?>, List<String>> ignoreFields) {
        this(null,
                new DartLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.typeMapping = new DartTypeMapping(this);
        this.ignoreFields = ignoreFields;
    }


    {

        codeGenMatchers.add(clazz -> {
            if (clazz == null) {
                return false;
            }
            Package aPackage = clazz.getPackage();
            if (aPackage == null) {
                return false;
            }
            return !aPackage.getName().startsWith("java.lang");

        });

    }

    public AbstractDartParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                              LanguageMetaInstanceFactory<DartClassMeta, CommonCodeGenMethodMeta, DartFieldMate> languageMetaInstanceFactory,
                              PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        super(javaParser, languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);
        this.typeMapping = new DartTypeMapping(this);

    }


    @Override
    public DartClassMeta parse(Class<?> source) {
        DartClassMeta dartClassMeta = super.parse(source);
        if (dartClassMeta == null) {
            return null;
        }

        CommonCodeGenMethodMeta[] methodMetas = dartClassMeta.getMethodMetas();
        boolean isFeignClient = false;
        if (methodMetas == null || methodMetas.length == 0) {
            Map<String, CommonCodeGenClassMeta> dependencies = (Map<String, CommonCodeGenClassMeta>) dartClassMeta.getDependencies();
            dependencies.put(BUILT_SERIALIZERS.getName(), BUILT_SERIALIZERS);
        } else {
            Optional<CommonCodeGenAnnotation> feignAnnotation = Arrays.stream(dartClassMeta.getAnnotations())
                    .filter((item) -> FEIGN_CLIENT_ANNOTATION_NAME.equals(item.getName()))
                    .findFirst();
            if (feignAnnotation.isPresent()) {
                feignAnnotation.get().setName("FeignClient");
                isFeignClient = true;
            }
        }

        String packagePath = dartClassMeta.getPackagePath();
        if (StringUtils.hasText(packagePath)) {
            dartClassMeta.setPackagePath(dartFileNameConverter(packagePath));
        }

        // 请求对象
        if (!isFeignClient && ClassType.CLASS.equals(dartClassMeta.getClassType())) {
            // DTO 合并超类的属性
            CommonCodeGenClassMeta superClass = dartClassMeta.getSuperClass();
            if (superClass == null || dartClassMeta.getFieldMetas() == null) {
                return dartClassMeta;
            }
            List<CommonCodeGenFiledMeta> filedMetas = Arrays.stream(dartClassMeta.getFieldMetas())
                    .collect(Collectors.toList());
            filedMetas.addAll(this.filterIgnoreFiledMetas(superClass));
            dartClassMeta.setFieldMetas(filedMetas.stream().distinct().toArray(CommonCodeGenFiledMeta[]::new));
            Map<String, CommonCodeGenClassMeta> dependencies = (Map<String, CommonCodeGenClassMeta>) dartClassMeta.getDependencies();
            dependencies.putAll(superClass.getDependencies());
        }


        return dartClassMeta;
    }

    public static String dartFileNameConverter(String filepath) {

        if (filepath.endsWith(MessageFormat.format(".{0}", LanguageDescription.DART.getSuffixName()))) {
            return filepath;
        }

        String[] split = filepath.split(RIGHT_SLASH);
        String s = split[split.length - 1];
        split[split.length - 1] = JavaMethodNameUtil.humpToLine(s);
        return String.join(RIGHT_SLASH, split);
    }

    @Override
    protected DartFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        DartFieldMate dartFieldMate = super.converterField(javaFieldMeta, classMeta);

        if (dartFieldMate == null) {
            return null;
        }
        //是否必填
        dartFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));
        return dartFieldMate;
    }


    @Override
    protected void enhancedProcessingField(DartFieldMate fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingClass(DartClassMeta methodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {

    }

    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, DartClassMeta codeGenClassMeta) {
        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);

        // 将请求参数的中的简单参数的集合类型 从Built的集合转换为Dart的标准集合对象
        Map<String, CommonCodeGenClassMeta> params = commonCodeGenMethodMeta.getParams();
        params.forEach((key, val) -> {
            DartClassMeta[] typeVariables = Arrays.stream(val.getTypeVariables()).map((typeVariableType) -> {
                if (DartClassMeta.BUILT_LIST.equals(typeVariableType)) {
                    return DartClassMeta.LIST;
                }
                if (DartClassMeta.BUILT_MAP.equals(typeVariableType)) {
                    return DartClassMeta.MAP;
                }
                if (DartClassMeta.BUILT_SET.equals(typeVariableType)) {
                    return DartClassMeta.SET;
                }
                if (DartClassMeta.BUILT_ITERABLE.equals(typeVariableType)) {
                    return DartClassMeta.ITERABLE;
                }
                return typeVariableType;
            }).toArray(DartClassMeta[]::new);
            val.setTypeVariables(typeVariables);
        });

        // 移除 返回值中的 Future 类型
        DartClassMeta[] returnTypes = Arrays.stream(commonCodeGenMethodMeta.getReturnTypes())
                .filter(item -> !DartClassMeta.FUTRUE.getName().equals(item.getName()))
                .toArray(DartClassMeta[]::new);

        if (returnTypes.length == 0) {
            // 补全返回值泛型
            returnTypes = new DartClassMeta[]{DartClassMeta.VOID};
            log.warn("方法解析警告，类 {}的方法 {}返回值类型不明确", classMeta.getName(), javaMethodMeta.getName());
        }
        commonCodeGenMethodMeta.setReturnTypes(returnTypes);


        return commonCodeGenMethodMeta;
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {


    }


    /**
     * 过滤需要忽略的filed
     *
     * @param dartClassMeta
     * @return
     */
    private List<CommonCodeGenFiledMeta> filterIgnoreFiledMetas(CommonCodeGenClassMeta dartClassMeta) {
        if (dartClassMeta.getFieldMetas() == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(dartClassMeta.getFieldMetas())
                .filter(commonCodeGenFiledMeta -> ignoreFields.getOrDefault(dartClassMeta.getSource(), Collections.emptyList())
                        .stream().noneMatch(filedName -> filedName.equals(commonCodeGenFiledMeta.getName())))
                .collect(Collectors.toList());
    }


}
