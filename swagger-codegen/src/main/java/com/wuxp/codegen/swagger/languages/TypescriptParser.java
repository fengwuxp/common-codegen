package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractTypescriptParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.swagger.annotations.ApiModelPropertyProcessor;
import com.wuxp.codegen.swagger.annotations.ApiProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * typeScript的 parser
 */
@Slf4j
public class TypescriptParser extends AbstractTypescriptParser {


    static {
        //添加swagger相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModelProperty.class, new ApiModelPropertyProcessor());
    }

    public TypescriptParser() {
        super(null, null);
    }

    public TypescriptParser(PackageMapStrategy packageMapStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, codeDetects);
    }

    @Override
    public TypescriptClassMeta parse(JavaClassMeta source) {

        if (source == null) {
            return null;
        }

        TypescriptClassMeta meta = this.getResultToLocalCache(source.getClazz());
        if (meta != null) {
            return meta;
        }

        if (ClassType.ENUM.equals(source.getClassType())) {
            //TODO 枚举
        }

        meta = new TypescriptClassMeta();
        meta.setName(source.getName());
        meta.setPackagePath(this.packageMapStrategy.convert(source.getClazz()));
        meta.setClassType(source.getClassType());
        meta.setAccessPermission(source.getAccessPermission());

        if (source.existAnnotation(Controller.class, RestController.class, RequestMapping.class)) {
            //spring的控制器

        }
        //类上的注释
        meta.setComments(super.generateComments(source.getAnnotations()).toArray(new String[]{}));

        //属性列表
        meta.setFiledMetas(this.converterFieldMetas(source.getFieldMetas(), source));

        //方法列表
        meta.setMethodMetas(this.converterMethodMetas(source.getMethodMetas(), source));

        //依赖列表
        meta.setDependencies(this.fetchDependencies(source.getDependencyList()));

        //加入缓存列表
        HANDLE_RESULT_CACHE.put(source.getClazz(), meta);

        return meta;
    }


    @Override
    protected TypescriptFieldMate[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas, JavaClassMeta classMeta) {

        if (javaFieldMetas == null) {
            return new TypescriptFieldMate[0];
        }

        return Arrays.stream(javaFieldMetas).map(javaFieldMeta -> {
            TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

            typescriptFieldMate.setName(javaFieldMeta.getName());
            typescriptFieldMate.setAccessPermission(javaFieldMeta.getAccessPermission());

            //注释来源于注解和java的类类型
            List<String> comments = super.generateComments(javaFieldMeta.getAnnotations());
            comments.addAll(super.generateComments(javaFieldMeta.getTypes()));

            typescriptFieldMate.setComments(comments.toArray(new String[]{}));

            //是否必填
            typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class));

            ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);

            if (apiModelProperty == null) {
                log.warn("类{}上的属性{}没有ApiModelProperty注解", classMeta.getClassName(), javaFieldMeta.getName());
            } else {
                if (typescriptFieldMate.getRequired() == null) {
                    typescriptFieldMate.setRequired(apiModelProperty.required());
                }
            }
            //属性解释
            Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaFieldMeta.getTypes());
            if (typescriptClassMetas != null) {
                //域对象类型描述
                typescriptFieldMate.setFiledTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
            } else {
                //解析失败
                throw new RuntimeException(String.format("解析类{}上的属性{}的类型{}失败", classMeta.getClassName(), javaFieldMeta.getName(), this.classToNamedString(javaFieldMeta.getTypes())));
            }

            //TODO 注解转化
//            typescriptFieldMate.setAnnotations();

            return typescriptFieldMate;
        }).toArray(TypescriptFieldMate[]::new);

    }

    @Override
    protected CommonCodeGenMethodMeta[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas, JavaClassMeta classMeta) {
        if (javaMethodMetas == null) {
            return new CommonCodeGenMethodMeta[0];
        }
        return Arrays.stream(javaMethodMetas).map(javaMethodMeta -> {
            CommonCodeGenMethodMeta genMethodMeta = new CommonCodeGenMethodMeta();
            //TODO method转化

            return genMethodMeta;
        }).toArray(CommonCodeGenMethodMeta[]::new);
    }


}
