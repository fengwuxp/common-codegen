package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.swagger.annotations.ApiProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * typeScript的
 */
@Slf4j
public class TypescriptParser extends AbstractLanguageParser<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    protected PackageMapStrategy packageMapStrategy;


    static {

        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
    }


    public TypescriptParser(PackageMapStrategy packageMapStrategy) {
        this.packageMapStrategy = packageMapStrategy;
    }

    @Override
    public CommonCodeGenClassMeta parse(JavaClassMeta source) {

        CommonCodeGenClassMeta meta = new CommonCodeGenClassMeta();
        meta.setName(source.getName());
        meta.setPackagePath(this.packageMapStrategy.convert(source.getClazz()));
        meta.setClassType(source.getClassType());
        meta.setAccessPermission(source.getAccessPermission());

        if (source.hasAnnotation(Controller.class, RestController.class, RequestMapping.class)) {
            //控制器
            //类上的注释
            meta.setComments(super.generateComments(source.getAnnotations()).toArray(new String[]{}));

        }

        meta.setFiledMetas(this.converterFieldMetas(source.getFieldMetas()));


        return null;
    }


    @Override
    protected CommonCodeGenFiledMeta[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas) {

        Arrays.stream(javaFieldMetas).map(javaFieldMeta -> {
            CommonCodeGenFiledMeta commonCodeGenFiledMeta = new CommonCodeGenFiledMeta();

            commonCodeGenFiledMeta.setName(javaFieldMeta.getName());
            commonCodeGenFiledMeta.setAccessPermission(javaFieldMeta.getAccessPermission());

            List<String> comments = super.generateComments(javaFieldMeta.getAnnotations());
            ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
            if (StringUtils.hasText(apiModelProperty.value())) {
                comments.add(apiModelProperty.value());
            } else {
                comments.add(apiModelProperty.notes());
            }
            commonCodeGenFiledMeta.setComments(comments.toArray(new String[]{}));

            //是否必填
            commonCodeGenFiledMeta.getTags().put("required", javaFieldMeta.hasAnnotation(NotNull.class) || apiModelProperty.required());

            return commonCodeGenFiledMeta;
        });

        return new CommonCodeGenFiledMeta[0];
    }

    @Override
    protected CommonCodeGenMethodMeta[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas) {
        return new CommonCodeGenMethodMeta[0];
    }

    @Override
    protected Set<CommonCodeGenClassMeta> fetchDependencies(Set<Class<?>> classes) {
        return null;
    }


}
