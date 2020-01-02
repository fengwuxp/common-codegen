package com.wuxp.codegen.spring.model;

import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用于生成 spring 控制器，服务层 dto的代码
 */
@Data
public class JavaSpringCodeGenClassMeta {

    public static String CONTROLLER_NAME_SUFFIX = "Controller";

    public static String SERVICE_NAME_SUFFIX = "Controller";

    public static String[] DTO_PREFIX_NAMES = new String[]{"Create", "Edit", "Query", "Find"};

    private static Pattern IS_PACKAGE_NAME = Pattern.compile("[a-z][A-Z]");


    private JavaCodeGenClassMeta[] javaCodeGenClassMetas;

    // com.xxx.example.controller.area
    private String[] controllerNames;

    private String[] serviceNames;

    private List<String[]> dtoNames;

    private String[] controllerPackages;

    private String[] servicePackages;


    public JavaSpringCodeGenClassMeta(String[] controllerNames,
                                      String[] serviceNames,
                                      List<String[]> dtoNames,
                                      String[] controllerPackages,
                                      String[] servicePackages) {
        this.controllerNames = controllerNames;
        this.serviceNames = serviceNames;
        this.dtoNames = dtoNames;
        this.controllerPackages = controllerPackages;
        this.servicePackages = servicePackages;
    }

    public static JavaSpringCodeGenClassMeta newInstance(Class<?> entityClass,
                                                         String basePackage,
                                                         String moduleName) {
        if (!StringUtils.hasText(moduleName)) {
            Tag annotation = entityClass.getAnnotation(Tag.class);
            String name = annotation.name();
            if (IS_PACKAGE_NAME.matcher(name).find()) {
                moduleName = name.toLowerCase();
            }
        }
        return newInstance(new Class[]{entityClass}, basePackage, moduleName);
    }

    public static JavaSpringCodeGenClassMeta newInstance(Class<?>[] entityClasses,
                                                         String basePackage,
                                                         String moduleName) {

        boolean hasModuleName = StringUtils.hasText(moduleName);
        List<Class<?>> classes = Arrays.asList(entityClasses);
        return newInstance(
                classes.stream().map(clazz -> MessageFormat.format("{0}{1}", clazz.getSimpleName(), CONTROLLER_NAME_SUFFIX)).toArray(String[]::new),
                classes.stream().map(clazz -> MessageFormat.format("{0}{1}", clazz.getSimpleName(), SERVICE_NAME_SUFFIX)).toArray(String[]::new),
                classes.stream().map(clazz -> Arrays.stream(DTO_PREFIX_NAMES).map(prefix -> MessageFormat.format("{0}{1}", prefix, clazz.getSimpleName()))
                        .toArray(String[]::new))
                        .collect(Collectors.toList()),
                classes.stream().map(clazz -> MessageFormat.format("{0}.{1}.{2}",
                        basePackage, CONTROLLER_NAME_SUFFIX.toLowerCase(), hasModuleName ? moduleName : clazz.getSimpleName().toLowerCase()))
                        .toArray(String[]::new),
                classes.stream().map(clazz -> MessageFormat.format("{0}.{1}.{2}",
                        basePackage, SERVICE_NAME_SUFFIX.toLowerCase(), hasModuleName ? moduleName : clazz.getSimpleName().toLowerCase()))
                        .toArray(String[]::new)
        );


    }

    public static JavaSpringCodeGenClassMeta newInstance(String[] controllerNames,
                                                         String[] serviceNames,
                                                         List<String[]> dtoNames,
                                                         String[] controllerPackages,
                                                         String[] servicePackages) {

        return new JavaSpringCodeGenClassMeta(controllerNames, serviceNames, dtoNames, controllerPackages, servicePackages);
    }
}
