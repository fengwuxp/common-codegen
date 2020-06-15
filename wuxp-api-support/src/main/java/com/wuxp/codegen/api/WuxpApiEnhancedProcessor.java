package com.wuxp.codegen.api;

import com.wuxp.api.context.InjectField;
import com.wuxp.api.signature.ApiSignature;
import com.wuxp.codegen.api.annotations.ApiSignatureProcessor;
import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wxup
 * 基于 fengwuxp-api-support 模块的增强处理
 */
@Slf4j
@Setter
public class WuxpApiEnhancedProcessor implements LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(false);

    /**
     * 匹配器链
     */
    protected List<CodeGenMatcher> codeGenMatchers = new ArrayList<>();

    @Override
    public CommonCodeGenFiledMeta enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta.existAnnotation(InjectField.class) && !javaFieldMeta.existAnnotation(ApiSignature.class)) {
            return null;
        }

        return fieldMeta;
    }

    @Override
    public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

        // 过滤掉被注入的参数
        Set<String> ignoreNames = new HashSet<>();
        javaMethodMeta.getParameters().forEach((name, parameter) -> {
            if (parameter.isAnnotationPresent(InjectField.class)) {
                ignoreNames.add(name);
            }
        });
        if (!ignoreNames.isEmpty()) {
            Map<String/*参数名称*/, CommonCodeGenClassMeta/*参数类型描述*/> params = new LinkedHashMap<>();
            methodMeta.getParams().forEach((name, parameter) -> {
                if (!ignoreNames.contains(name)) {
                    params.put(name, parameter);
                }
            });
            methodMeta.setParams(params);
        }

        // 找到需要签名的列
        Set<String> needSignFields = javaMethodMeta.getParams()
                .values()
                .stream()
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(clazz -> !clazz.getName().startsWith("org.springframework"))
                .filter((clazz -> {
                    if (!this.isMatchGenCodeRule(clazz) ||
                            JavaTypeUtil.isMap(clazz) ||
                            JavaTypeUtil.isCollection(clazz)) {
                        return false;
                    }
                    return JavaTypeUtil.isNoneJdkComplex(clazz);
                }))
                .map(clazz -> Arrays.asList(this.javaParser.parse(clazz).getFieldMetas()))
                .flatMap(Collection::stream)
                .filter(fieldMeta -> fieldMeta.existAnnotation(ApiSignature.class))
                .map(CommonBaseMeta::getName)
                .sorted(Comparator.comparing(Function.identity()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> collect = javaMethodMeta.getParameters()
                .values()
                .stream()
                .filter(parameter -> parameter.isAnnotationPresent(ApiSignature.class))
                .map(Parameter::getName)
                .collect(Collectors.toSet());
        needSignFields.addAll(collect);

        if (needSignFields.size() == 0) {
            return methodMeta;
        }

        //是否存在签名注解
        Optional<CommonCodeGenAnnotation> codeGenAnnotation = Arrays.stream(methodMeta.getAnnotations())
                .filter(commonCodeGenAnnotation -> commonCodeGenAnnotation.getName().equals(ApiSignatureProcessor.ANNOTATION_NAME))
                .findFirst();
        if (!codeGenAnnotation.isPresent()) {
            List<CommonCodeGenAnnotation> commonCodeGenAnnotations = Arrays.stream(methodMeta.getAnnotations()).collect(Collectors.toList());
            commonCodeGenAnnotations.add(ApiSignatureProcessor.genAnnotation(needSignFields));
            methodMeta.setAnnotations(commonCodeGenAnnotations.toArray(new CommonCodeGenAnnotation[]{}));
        }
        return methodMeta;
    }

    /**
     * 是否匹配生成的规则
     *
     * @param clazz
     * @return
     */
    protected boolean isMatchGenCodeRule(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        //必须满足所有的匹配器才能进行生成
        return codeGenMatchers.stream()
                .allMatch(codeGenMatcher -> codeGenMatcher.match(clazz));
    }
}
