package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, Collection<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping(this);

    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy, Collection<CodeDetect> codeDetects) {
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

        //检查代码
        this.detectJavaCode(source);

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

            //field 类型类别
            Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaFieldMeta.getTypes());
            if (typescriptClassMetas != null) {
                //域对象类型描述
                typescriptFieldMate.setFiledTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
            } else {
                //解析失败
                throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败", classMeta.getClassName(), javaFieldMeta.getName(), this.classToNamedString(javaFieldMeta.getTypes())));
            }

            //TODO 注解转化
//            typescriptFieldMate.setAnnotations();

            //增强处理
            this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);

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
            //method转换
            genMethodMeta.setAccessPermission(javaMethodMeta.getAccessPermission());
            List<String> comments = super.generateComments(javaMethodMeta.getAnnotations());
            comments.addAll(super.generateComments(javaMethodMeta.getReturnType()));
            genMethodMeta.setComments(comments.toArray(new String[]{}));
            genMethodMeta.setName(javaMethodMeta.getName());


            //TODO support spring webflux

            //处理返回值
            Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaMethodMeta.getReturnType());
            if (typescriptClassMetas != null) {
                //域对象类型描述
                genMethodMeta.setReturnTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
            } else {
                //解析失败
                throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                        classMeta.getClassName(),
                        javaMethodMeta.getName(),
                        this.classToNamedString(javaMethodMeta.getReturnType())));
            }

            //TODO 处理方法的参数
            //1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
            Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
            //有效的参数
            Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
            methodMetaParams.forEach((key, classes) -> {
                Class<?>[] array = Arrays.stream(classes)
                        .filter(this.filterClassByLibrary::filter)
                        .toArray(Class<?>[]::new);
                if (array.length == 0) {
                    return;
                }
                effectiveParams.put(key, array);
            });

            //2: 合并参数列表，将参数列表中的简单类型参数和复杂的类型参数合并到一个列表中
            //2.1：遍历展开参数列表


            List<TypescriptFieldMate> typescriptFieldMates = new LinkedList<>();

            effectiveParams.forEach((key, classes) -> {

                Class<?> clazz = classes[0];
                if (JavaTypeUtil.isComplex(clazz)) {
                    //复杂的数据类型
                    JavaClassMeta javaClassMeta = this.javaParser.parse(clazz);

                    //转换

                } else {
                    Collection<TypescriptClassMeta> classMetas = this.typescriptTypeMapping.mapping(classes);
                    TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();
                    typescriptFieldMate.setName(key);

                    Annotation[] annotations = javaMethodMeta.getParamAnnotations().get(key);

                    //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
//                    Arrays.stream(annotations).filter(annotation -> {
//                        annotation.annotationType().equals(Reque)
//                        return true;
//                    })

//                    typescriptFieldMate.setRequired();
                    typescriptFieldMates.add(typescriptFieldMate);
                }


            });

            //3: 重组，使用第二部得到的列表构建一个信息的 TypescriptClassMeta对象，类型为typescript的interface
            //参数的元数据类型信息
            TypescriptClassMeta argsClassMeta = new TypescriptClassMeta();
            argsClassMeta.setClassType(ClassType.INTERFACE);
            argsClassMeta.setFiledMetas(typescriptFieldMates.toArray(new TypescriptFieldMate[]{}));
            LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
            //请求参数名称，固定为req
            params.put("req", argsClassMeta);
            genMethodMeta.setParams(params);
            //增强处理
            this.enhancedProcessingMethod(genMethodMeta, javaMethodMeta, classMeta);

            return genMethodMeta;
        }).toArray(CommonCodeGenMethodMeta[]::new);
    }

}
