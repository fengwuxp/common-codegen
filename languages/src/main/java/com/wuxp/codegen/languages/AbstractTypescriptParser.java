package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.customizeJavaTypeMapping;
import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.customizeTypeMapping;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, List<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping(this);

    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy, CodeGenMatchingStrategy genMatchingStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }

    @Override
    public TypescriptClassMeta parse(Class<?> source) {

        //符合匹配规则，或非集合类型和Map的的子类进行
        if (!this.isMatchGenCodeRule(source) ||
                JavaTypeUtil.isMap(source) ||
                JavaTypeUtil.isCollection(source)) {
            return null;
        }

        if (HANDLE_COUNT.containsKey(source)) {
            //标记某个类被处理的次数如果超过2次，从缓存中返回
            if (HANDLE_COUNT.get(source) > 2) {
                return this.getResultToLocalCache(source);
            } else {
                HANDLE_COUNT.put(source, HANDLE_COUNT.get(source) + 1);
            }
        } else {
            HANDLE_COUNT.put(source, 1);
        }
        Integer count = HANDLE_COUNT.get(source);

        TypescriptClassMeta mapping = customizeTypeMapping.mapping(source);
        if (mapping != null) {
            HANDLE_RESULT_CACHE.put(source, mapping);
            return mapping;
        }


        JavaClassMeta javaClassMeta = this.javaParser.parse(source);
        if (javaClassMeta.isApiServiceClass()) {
            //要生成的服务，判断是否需要生成
            if (!this.genMatchingStrategy.isMatchClazz(javaClassMeta)) {
                //跳过
                log.warn("跳过类{}", source.getName());
                return null;
            }
        }

        TypescriptClassMeta meta = this.getResultToLocalCache(source);

        if (meta != null) {
            return meta;
        }

        //检查代码
        this.detectJavaCode(javaClassMeta);

        meta = new TypescriptClassMeta();
        meta.setSource(source);
        meta.setName(this.packageMapStrategy.convertClassName(source.getSimpleName()));
        meta.setPackagePath(this.packageMapStrategy.convert(source));
        meta.setClassType(javaClassMeta.getClassType());
        meta.setAccessPermission(javaClassMeta.getAccessPermission());
        meta.setTypeVariables(Arrays.stream(javaClassMeta.getTypeVariables())
                .map(type -> {
                    if (type instanceof Class) {
                        return this.parse((Class) type);
                    } else if (type instanceof TypeVariable) {
                        return TypescriptClassMeta.TYPE_VARIABLE;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(CommonCodeGenClassMeta[]::new));
        Class<?> javaClassSuperClass = javaClassMeta.getSuperClass();
        TypescriptClassMeta typescriptSupperClassMeta = this.parse(javaClassSuperClass);
        if (javaClassSuperClass != null && typescriptSupperClassMeta == null) {
           if (log.isDebugEnabled()){
               log.debug("超类 {} 解析处理失败或被忽略", javaClassMeta.getClassName());
           }
        }
        meta.setSuperClass(typescriptSupperClassMeta);

        //类上的注释
        meta.setComments(this.generateComments(source.getAnnotations(), source).toArray(new String[]{}));
        //类上的注解
        meta.setAnnotations(this.converterAnnotations(source.getAnnotations(), javaClassMeta.getClass()));

        if (count == 1) {
            if (javaClassMeta.isApiServiceClass()) {
                //spring的控制器  生成方法列表
                meta.setMethodMetas(this.converterMethodMetas(javaClassMeta.getMethodMetas(), javaClassMeta, meta)
                        .toArray(new CommonCodeGenMethodMeta[]{}));
            } else {
                // 普通的java bean DTO  生成属性列表
                meta.setFiledMetas(this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta)
                        .toArray(new TypescriptFieldMate[]{}));
            }
        }

        //依赖处理
        final Map<String, TypescriptClassMeta> metaDependencies = meta.getDependencies() == null ? new LinkedHashMap<>() : (Map<String, TypescriptClassMeta>) meta.getDependencies();

        if (count == 1) {
            //依赖列表
            Set<Class<?>> dependencyList = javaClassMeta.getDependencyList();
            if (javaClassMeta.isApiServiceClass()) {
                dependencyList = dependencyList.stream().
                        filter(Objects::nonNull)
                        //忽略所有接口的依赖
                        .filter(clazz -> !clazz.isInterface())
                        //忽略超类的依赖
                        .filter(clazz -> !clazz.equals(javaClassSuperClass))
                        .collect(Collectors.toSet());
            }
            Map<String, TypescriptClassMeta> dependencies = this.fetchDependencies(dependencyList);
            dependencies.forEach(metaDependencies::put);
        }

        Map<String/*类型，父类，接口，本身*/, CommonCodeGenClassMeta[]> superTypeVariables = new LinkedHashMap<>();

        //处理超类上面的类型变量
        javaClassMeta.getSuperTypeVariables().forEach((superClazz, val) -> {

            if (val == null || val.length == 0) {
                return;
            }
            //处理超类
            TypescriptClassMeta typescriptClassMeta = this.parse(superClazz);
            if (typescriptClassMeta == null) {
                return;
            }

            //处理超类上的类型变量 例如 A<T,E> extends B<C<T>,E> 这种情况
            CommonCodeGenClassMeta[] typeVariables = Arrays.stream(val)
                    .map(clazz -> {
                        TypescriptClassMeta typeVariable = this.parse(clazz);
                        if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                            metaDependencies.put(typeVariable.getName(), typeVariable);
                        }
                        return typeVariable;
                    })
                    .filter(Objects::nonNull)
                    .toArray(CommonCodeGenClassMeta[]::new);

            superTypeVariables.put(typescriptClassMeta.getName(), typeVariables);
        });

        meta.setDependencies(metaDependencies);
        meta.setSuperTypeVariables(superTypeVariables);

        //当超类不为空，且超类的类型变量不为空的时候，重新设置一下超类的类型变量
        if (meta.getSuperClass() != null && superTypeVariables.size() > 0) {
            CommonCodeGenClassMeta[] supperClassTypeVariables = superTypeVariables.get(meta.getSuperClass().getName());
            CommonCodeGenClassMeta superClass = meta.getSuperClass();

            //做一次值复制，防止改变缓存中的值
            CommonCodeGenClassMeta newSupperClass = new CommonCodeGenClassMeta();
            BeanUtils.copyProperties(superClass, newSupperClass);
            newSupperClass.setTypeVariables(supperClassTypeVariables);
            meta.setSuperClass(newSupperClass);
        }

        HANDLE_RESULT_CACHE.put(source, meta);
        return meta;
    }

    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta == null) {
            return null;
        }


        boolean isEnum = classMeta.getClazz().isEnum();
        if (javaFieldMeta.getIsStatic() && !isEnum) {
            //不处理静态类型的字段
            return null;
        }
        TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

        typescriptFieldMate.setName(javaFieldMeta.getName());
        typescriptFieldMate.setAccessPermission(javaFieldMeta.getAccessPermission());

        //注释来源于注解和java的类类型
        List<String> comments = super.generateComments(javaFieldMeta.getAnnotations(), javaFieldMeta.getField());
        if (!isEnum) {
            comments.addAll(super.generateComments(javaFieldMeta.getTypes(), false));
        } else {
            if (comments.size() == 0) {
                comments.add(javaFieldMeta.getName());
                log.error("枚举没有加上描述相关的注解");
            }
        }

        //注解
        typescriptFieldMate.setComments(comments.toArray(new String[]{}));

        //注解
        typescriptFieldMate.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta.getField()));

        //是否必填
        typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));

        //field 类型类别
        Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaFieldMeta.getTypes());

        //从泛型中解析
        Type[] typeVariables = javaFieldMeta.getTypeVariables();
        if (typeVariables != null && typeVariables.length > 0) {

            typescriptClassMetas.addAll(Arrays.stream(typeVariables)
                    .filter(Objects::nonNull)
                    .map(Type::getTypeName).map(name -> {
                        TypescriptClassMeta typescriptClassMeta = new TypescriptClassMeta();
                        BeanUtils.copyProperties(TypescriptClassMeta.TYPE_VARIABLE, typescriptClassMeta);
                        typescriptClassMeta.setName(name);
                        return typescriptClassMeta;
                    }).collect(Collectors.toList()));
        }

        if (typescriptClassMetas.size() > 0) {
            //域对象类型描述
            typescriptFieldMate.setFiledTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
        } else {

            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败",
                    classMeta.getClassName(),
                    javaFieldMeta.getName(),
                    this.classToNamedString(javaFieldMeta.getTypes())));
        }


        //TODO 注解转化

        //增强处理
        this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);

        return typescriptFieldMate;
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, TypescriptClassMeta codeGenClassMeta) {
        if (javaMethodMeta == null) {
            return null;
        }

        CommonCodeGenMethodMeta genMethodMeta = new CommonCodeGenMethodMeta();
        //method转换
        genMethodMeta.setAccessPermission(javaMethodMeta.getAccessPermission());
        //注解转注释
        List<String> comments = super.generateComments(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod());
        comments.addAll(super.generateComments(javaMethodMeta.getReturnType(), true));
        genMethodMeta.setComments(comments.toArray(new String[]{}));
        genMethodMeta.setName(javaMethodMeta.getName());

        //处理方法上的相关注解
        genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod()));


        //TODO support spring webflux

        //处理返回值
        List<TypescriptClassMeta> typescriptReturnTypesClassMetas = this.typescriptTypeMapping.mapping(javaMethodMeta.getReturnType());
        if (!typescriptReturnTypesClassMetas.contains(TypescriptClassMeta.PROMISE)) {
            typescriptReturnTypesClassMetas.add(0, TypescriptClassMeta.PROMISE);
        }

        if (typescriptReturnTypesClassMetas.size() > 0) {
            //域对象类型描述
            genMethodMeta.setReturnTypes(typescriptReturnTypesClassMetas.toArray(new TypescriptClassMeta[]{}));
        } else {
            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                    classMeta.getClassName(),
                    javaMethodMeta.getName(),
                    this.classToNamedString(javaMethodMeta.getReturnType())));
        }


        //处理方法的参数
        //1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
        Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
        //有效的参数
        Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
        methodMetaParams.forEach((key, classes) -> {
            Class<?>[] array = Arrays.stream(classes)
                    .filter(this.packageNameCodeGenMatcher::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            effectiveParams.put(key, array);
        });

        //2: 合并参数列表，将参数列表中的简单类型参数和复杂的类型参数合并到一个列表中
        //2.1：遍历展开参数列表

        final Set<TypescriptFieldMate> typescriptFieldMates = new LinkedHashSet<>();
        //参数的元数据类型信息
        final TypescriptClassMeta argsClassMeta = new TypescriptClassMeta();


        effectiveParams.forEach((key, classes) -> {

            Class<?> clazz = classes[0];
            if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                TypescriptClassMeta typescriptClassMeta = this.parse(clazz);
                if (typescriptClassMeta != null) {
                    BeanUtils.copyProperties(typescriptClassMeta, argsClassMeta);
                }

            } else if (clazz.isEnum()) {
                //枚举
                TypescriptFieldMate fieldMate = new TypescriptFieldMate();

                typescriptFieldMates.add(fieldMate);

            } else {

                Set<Class<?>> otherDependencies = new HashSet<>();

                if (clazz.isArray()) {
                    //数组
                    otherDependencies.add(clazz.getComponentType());
                } else if (JavaTypeUtil.isCollection(clazz)) {
                    //集合
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isMap(clazz)) {
                    //map
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isJavaBaseType(clazz)) {
                    //简单数据类型
                } else {
                    log.warn("未处理的类型{}", clazz.getName());
                }

                otherDependencies.stream().filter(JavaTypeUtil::isNoneJdkComplex).forEach(c -> {
                    TypescriptClassMeta typescriptClassMeta = this.parse(c);
                    ((Map<String, TypescriptClassMeta>) argsClassMeta.getDependencies()).put(typescriptClassMeta.getName(), typescriptClassMeta);
                });

                //注释
                Annotation[] annotations = javaMethodMeta.getParamAnnotations().get(key);

                //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
//                    Arrays.stream(annotations).macth(annotation -> {
//                        annotation.annotationType().equals(Reque)
//                        return true;
//                    })
                JavaFieldMeta javaFieldMeta = new JavaFieldMeta();
                javaFieldMeta.setTypes(classes)
                        .setIsTransient(false)
                        .setIsVolatile(false);
                javaFieldMeta.setAccessPermission(AccessPermission.PUBLIC);
                javaFieldMeta.setAnnotations(annotations);
                javaFieldMeta.setName(key);
                TypescriptFieldMate typescriptFieldMate = this.converterField(javaFieldMeta, classMeta);

                if (typescriptFieldMate != null) {
                    this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);
                    typescriptFieldMates.add(typescriptFieldMate);
                }
            }

        });
        //3: 重组，使用第二步得到的列表构建一个信息的 TypescriptClassMeta对象，类型为typescript的interface
        argsClassMeta.setClassType(ClassType.INTERFACE);
        if (argsClassMeta.getFiledMetas() != null) {
            Arrays.asList(argsClassMeta.getFiledMetas()).forEach(genFiledMeta -> {
                TypescriptFieldMate target = new TypescriptFieldMate();
                BeanUtils.copyProperties(genFiledMeta, target);
                boolean isExist = typescriptFieldMates.stream()
                        .filter(typescriptFieldMate -> typescriptFieldMate.getName().equals(genFiledMeta.getName()))
                        .toArray().length > 0;
                if (isExist) {
                    log.error("{}方法中的参数{}在类{}中已经存在", javaMethodMeta.getName(), genFiledMeta.getName(), argsClassMeta.getPackagePath() + argsClassMeta.getName());
                } else {
                    typescriptFieldMates.add(target);
                }

            });
        }
        argsClassMeta.setFiledMetas(typescriptFieldMates.toArray(new TypescriptFieldMate[]{}));
        if (!StringUtils.hasText(argsClassMeta.getName())) {
            //没有复杂对象的参数
            String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
            argsClassMeta.setName(name);
            argsClassMeta.setPackagePath(MessageFormat.format("/req/{0}", name));
            //这个时候没有依赖
            argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
            argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});
        } else {
            boolean hasComplex = false, hasSimple = false;
            for (Class[] classes : effectiveParams.values()) {
                Class<?> clazz = classes[0];
                if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                    hasComplex = true;
                } else {
                    hasSimple = true;
                }
            }
            if (hasComplex && hasSimple) {
                //参数列表中有复杂对象，并且有额外的简单对象，将类的名称替换，使用方法的名称,重新生成过一个新的对象
                String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
                argsClassMeta.setPackagePath(argsClassMeta.getPackagePath().replace(argsClassMeta.getName(), name));
                argsClassMeta.setName(name);
            }

        }
        //加入依赖列表
        Map<String, ? extends CommonCodeGenClassMeta> dependencies = codeGenClassMeta.getDependencies();


        ((Map<String, TypescriptClassMeta>) dependencies).put(argsClassMeta.getName(), argsClassMeta);

        LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        codeGenClassMeta.setDependencies(dependencies);
        //请求参数名称，固定为req
        params.put("req", argsClassMeta);
        genMethodMeta.setParams(params);

        //增强处理
        this.enhancedProcessingMethod(genMethodMeta, javaMethodMeta, classMeta);

        return genMethodMeta;
    }


    /**
     * 抓取依赖列表
     *
     * @param dependencies
     * @return
     */
    protected Map<String, TypescriptClassMeta> fetchDependencies(Set<Class<?>> dependencies) {
        if (dependencies == null || dependencies.size() == 0) {
            return new HashMap<>();
        }


        List<Class<?>> classList = dependencies.stream()
                .map(customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(this::isMatchGenCodeRule)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Set<TypescriptClassMeta> classMetas = classList.stream()
                .map(this::parse)
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .collect(Collectors.toSet());
        classList.stream()
                .map(this.typescriptTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .forEach(classMetas::add);

        return classMetas.stream()
                .collect(Collectors.toMap(CommonBaseMeta::getName, v -> v));

    }

}
