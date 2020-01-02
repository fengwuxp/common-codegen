package com.wuxp.codegen.spring.model;

import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JavaSpringCodeGenClassMeta {

    public static String CONTROLLER_NAME_SUFFIX = "Controller";

    public static String SERVICE_NAME_SUFFIX = "Controller";

    public static String[] DTO_PREFIX_NAMES = new String[]{"Create", "Edit", "Query", "Find"};


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
