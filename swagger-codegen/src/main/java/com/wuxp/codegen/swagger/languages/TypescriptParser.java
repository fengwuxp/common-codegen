package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.languages.AbstractTypescriptParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.swagger.annotations.ApiModelPropertyProcessor;
import com.wuxp.codegen.swagger.annotations.ApiProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * typeScript的 parser
 */
@Slf4j
public class TypescriptParser extends AbstractTypescriptParser {


    protected PackageMapStrategy packageMapStrategy;

    protected TypeMapping<Class<?>, List<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping();

    static {
        //添加swagger相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModelProperty.class, new ApiModelPropertyProcessor());
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
    protected TypescriptFieldMate[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas) {

        return Arrays.stream(javaFieldMetas).map(javaFieldMeta -> {
            TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

            typescriptFieldMate.setName(javaFieldMeta.getName());
            typescriptFieldMate.setAccessPermission(javaFieldMeta.getAccessPermission());

            //注释来源于注解和java的类类型
            List<String> comments = super.generateComments(javaFieldMeta.getAnnotations());
            comments.addAll(super.generateComments(javaFieldMeta.getTypes()));

            typescriptFieldMate.setComments(comments.toArray(new String[]{}));
            ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
            //是否必填
            typescriptFieldMate.setRequired(javaFieldMeta.hasAnnotation(NotNull.class) || apiModelProperty.required());

            //类型解释
//            typescriptFieldMate.setFiledType();


            return typescriptFieldMate;
        }).toArray(TypescriptFieldMate[]::new);

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
