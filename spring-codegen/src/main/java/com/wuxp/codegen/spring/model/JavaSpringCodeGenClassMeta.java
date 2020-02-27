package com.wuxp.codegen.spring.model;

import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于生成 spring 控制器，服务层 dto的代码
 */
@Data
public class JavaSpringCodeGenClassMeta {


    public static String CONTROLLER_NAME_SUFFIX = "Controller";

    public static String SERVICE_NAME_SUFFIX = "Service";

    public static String DTO_RESP_SUFFIX = "Info";

    public static Map<DtoObjectType, String> DOT_TYPE_REQ_PREFIX = new HashMap<>(4);


    private static Pattern IS_PACKAGE_NAME = Pattern.compile("[a-z][A-Z]");

    static {
        DOT_TYPE_REQ_PREFIX.put(DtoObjectType.CREATE, "Create");
        DOT_TYPE_REQ_PREFIX.put(DtoObjectType.UPDATE, "Edit");
        DOT_TYPE_REQ_PREFIX.put(DtoObjectType.DELETED, "Delete");
        DOT_TYPE_REQ_PREFIX.put(DtoObjectType.QUERY, "Query");
    }


    private JavaCodeGenClassMeta[] javaCodeGenClassMetas;

    /**
     * 控制器的全 类名
     * 例如：{"com.xxx.example.controller.area.AreaController"}
     */
    private String[] controllerNames;

    /**
     * 服务层的全类名
     * 例如：{"com.xxx.example.services.area.AreaServices"}
     */
    private String[] serviceNames;

    /**
     * dto resp 的全类名
     * 例如：{"com.xxx.example.services.area.info.AreaInfo"}
     */
    private String[] respDotNames;

    /**
     * dot的全类名
     * {"CREATE": "com.xxx.example.services.area.req.CreateAreaReq"}
     */
    private List<Map<DtoObjectType, String>> reqDotNames;


    private JavaSpringCodeGenClassMeta(String[] controllerNames,
                                       String[] serviceNames,
                                       String[] respDotNames,
                                       List<Map<DtoObjectType, String>> reqDotNames) {
        this.controllerNames = controllerNames;
        this.serviceNames = serviceNames;
        this.respDotNames = respDotNames;
        this.reqDotNames = reqDotNames;
    }

    public static JavaSpringCodeGenClassMeta newInstance(Class<?> entityClass,
                                                         String basePackage) {
        return newInstance(new Class[]{entityClass}, basePackage, null);
    }

    public static JavaSpringCodeGenClassMeta newInstance(Class<?>[] entityClasses,
                                                         String basePackage,
                                                         String moduleName) {


        List<Class<?>> classes = Arrays.asList(entityClasses);

        // 数据传输对象占位符
//        List<String> dtoPlaceholders = new ArrayList<>();
//        dtoPlaceholders.addAll(Arrays.stream(DTO_REQ_PREFIX_NAMES).map(prefix -> MessageFormat.format("req.{0}{1}", prefix, "{}")).collect(Collectors.toList()));
//        dtoPlaceholders.addAll(Stream.of(DTO_RESP_SUFFIX).map(suffix -> MessageFormat.format("info.{0}{1}", "{}", suffix)).collect(Collectors.toList()));

        String[] controllerNames = classes.stream().map(clazz ->
                MessageFormat.format(
                        "{0}.{1}.{2}.{3}{4}",
                        basePackage,
                        CONTROLLER_NAME_SUFFIX.toLowerCase(),
                        getModuleName(clazz, moduleName),
                        clazz.getSimpleName(),
                        CONTROLLER_NAME_SUFFIX))
                .toArray(String[]::new);

        String[] serviceNames = classes.stream().map(clazz -> MessageFormat.format(
                "{0}.{1}.{2}.{3}{4}",
                basePackage,
                SERVICE_NAME_SUFFIX.toLowerCase() + "s",
                getModuleName(clazz, moduleName),
                clazz.getSimpleName(),
                SERVICE_NAME_SUFFIX)).toArray(String[]::new);


        String[] respDotNames = classes.stream()
                .map(clazz -> Stream.of(DTO_RESP_SUFFIX)
                        .map(suffix -> MessageFormat.format("{0}{1}", clazz.getSimpleName(), suffix))
                        .map(dotNme -> MessageFormat.format(
                                "{0}.{1}.{2}.{3}",
                                basePackage,
                                getModuleName(clazz, moduleName),
                                "info",
                                dotNme)))
                .toArray(String[]::new);


        List<Map<DtoObjectType, String>> dtoNames = classes.stream().map(clazz -> {
            Map<DtoObjectType, String> names = new HashMap<>();
            DOT_TYPE_REQ_PREFIX.forEach((key, prefix) -> {
                String dotName = MessageFormat.format("{0}.{1}.{2}.{3}{4}}",
                        basePackage,
                        getModuleName(clazz, moduleName),
                        "req",
                        prefix,
                        clazz.getSimpleName());

                names.put(key, dotName);
            });
            return names;
        }).collect(Collectors.toList());


        return newInstance(
                controllerNames,
                serviceNames,
                respDotNames,
                dtoNames
        );


    }

    public static JavaSpringCodeGenClassMeta newInstance(String[] controllerNames,
                                                         String[] serviceNames,
                                                         String[] respDotNames,
                                                         List<Map<DtoObjectType, String>> dtoNames) {

        return new JavaSpringCodeGenClassMeta(controllerNames, serviceNames, respDotNames, dtoNames);
    }

    private static String getModuleName(Class<?> entityClass, String moduleName) {
        if (!StringUtils.hasText(moduleName)) {
            return moduleName;
        }

        Tag annotation = entityClass.getAnnotation(Tag.class);
        String name = annotation.name();
        if (IS_PACKAGE_NAME.matcher(name).find()) {
            return name.toLowerCase();
        } else {
            return entityClass.getSimpleName().toLowerCase();
        }
    }


}
