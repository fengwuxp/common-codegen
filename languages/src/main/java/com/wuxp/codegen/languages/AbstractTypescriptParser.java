package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.TypescriptLanguageMetaInstanceFactory;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 抽象的typescript parser
 *
 * @author wxup
 */
@Slf4j
public abstract class AbstractTypescriptParser extends
        AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy,
                                    CodeGenMatchingStrategy genMatchingStrategy,
                                    Collection<CodeDetect> codeDetects) {
        super(new TypescriptLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        //根据java 类进行匹配
        codeGenMatchers.add(clazz -> clazz.isEnum() || JavaTypeUtils.isNoneJdkComplex(clazz) || clazz.isAnnotation());
    }


    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        TypescriptFieldMate typescriptFieldMate = super.converterField(javaFieldMeta, classMeta);
        if (typescriptFieldMate == null) {
            return null;
        }
        //是否必填
        typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));
        return typescriptFieldMate;
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta,
                                                      TypescriptClassMeta codeGenClassMeta) {

        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
        if (commonCodeGenMethodMeta == null) {
            return null;
        }

        //处理返回值
        Class<?>[] returnTypes = javaMethodMeta.getReturnType();
        Class<?> mapClazz = Arrays.stream(returnTypes)
                .filter(JavaTypeUtils::isMap)
                .findAny()
                .orElse(null);

        List<Class<?>> newTypes = Arrays.stream(returnTypes).collect(Collectors.toList());

        //处理map 类型的对象
        if (mapClazz != null) {
            int length = newTypes.size();
            for (int i = 0; i < length; i++) {
                if (newTypes.get(i).equals(mapClazz)) {
                    int i1 = i + 1;
                    if (i1 >= length) {
                        //没有设置 Map 的key value的泛型
                        log.warn(MessageFormat.format("处理类：{0}上的方法：{1},发现非预期的情况", classMeta.getClassName(), javaMethodMeta.getName()));
                        newTypes.add(Object.class);
                        newTypes.add(Object.class);
                    }
                    Class<?> keyClazz = newTypes.get(i1);
                    if (!JavaTypeUtils.isJavaBaseType(keyClazz)) {
                        // TODO 如果map的key不是基础数据类
//                        newTypes.set(i1, Object.class);
                        log.error("类 {} 的 {} 方法的返回值Map类型的key不是基础数据类型或字符串", classMeta.getName(), javaMethodMeta.getName());
                    }
                    break;
                }
            }
        }
        Class[] newReturnTypes = newTypes.toArray(new Class[0]);
        List<TypescriptClassMeta> mapping = this.languageTypeMapping.mapping(newReturnTypes);
        if (newTypes.size() > returnTypes.length) {
            //返回值类型列表发生变化，重新计算返回值类型
            returnTypes = newReturnTypes;
            javaMethodMeta.setReturnType(newReturnTypes);
            mapping = this.languageTypeMapping.mapping(newReturnTypes);
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[0]));
        }
        // 移除所有的 Promise Type
        mapping.remove(TypescriptClassMeta.PROMISE);
//        ClientProviderType providerType = CodegenConfigHolder.getConfig().getProviderType();
//        if (ClientProviderType.TYPESCRIPT_FEIGN.equals(providerType)) {
//            if (!mapping.contains(TypescriptClassMeta.PROMISE)) {
//                mapping.add(0, TypescriptClassMeta.PROMISE);
//            }
//        } else {
//            mapping.remove(TypescriptClassMeta.PROMISE);
//        }
        if (mapping.size() > 0) {
            //域对象类型描述
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[]{}));
        } else {
            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                    classMeta.getClassName(),
                    javaMethodMeta.getName(),
                    this.classToNamedString(returnTypes)));
        }

        //将需要导入的加入依赖列表
        Arrays.stream(commonCodeGenMethodMeta.getReturnTypes())
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .forEach(returnType -> {
                    ((Map<String, CommonCodeGenClassMeta>) codeGenClassMeta.getDependencies()).put(returnType.getName(), returnType);
                });

        //增强处理
        this.enhancedProcessingMethod(commonCodeGenMethodMeta, javaMethodMeta, classMeta);

        return commonCodeGenMethodMeta;
    }

    @Override
    protected void enhancedProcessingClass(TypescriptClassMeta methodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected boolean needMargeMethodParams() {
        return true;
    }
}
