package com.wuxp.codegen.spring.template;

import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.spring.model.JavaSpringCodeGenClassMeta;
import com.wuxp.codegen.templates.TemplateLoader;
import com.wuxp.codegen.utils.FileUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wuxp.codegen.spring.model.JavaSpringCodeGenClassMeta.*;

@Slf4j
public class SpringTemplateStrategy implements TemplateStrategy<JavaSpringCodeGenClassMeta> {

    //在LAST_MODIFIED_MINUTE内生成的文件不在生成
    public static final float LAST_MODIFIED_MINUTE = 0.1f;

    public static final String CONTROLLER_TEMPLATE = "spring/controller.ftl";

    public static final String SERVICE_TEMPLATE = "spring/service.ftl";

    public static final String DTO_TEMPLATE = "spring/dto.ftl";

    private static final String EXT_NAME = "java";

    /**
     * 模板加载器
     */
    protected TemplateLoader<Template> templateLoader;

    /**
     * 输出的根目录
     */
    protected String outputPath;


    public SpringTemplateStrategy(TemplateLoader<Template> templateLoader,
                                  String outputPath,
                                  boolean isDeletedOutputDirectory) {
        this.templateLoader = templateLoader;
        this.outputPath = outputPath.endsWith(File.separator) ? outputPath : outputPath + File.separator;
        if (isDeletedOutputDirectory) {
            //删除原本的目录
            boolean r = FileUtil.deleteDirectory(this.outputPath);
            log.info("删除原本的输出目录{}，删除{}", this.outputPath, r ? "成功" : "失败");
        }

    }

    @Override
    public void build(JavaSpringCodeGenClassMeta data) {

        JavaCodeGenClassMeta[] javaCodeGenClassMetas = data.getJavaCodeGenClassMetas();
        String[] controllerNames = this.controllerNames(data.getControllerNames(), javaCodeGenClassMetas);
        String[] serviceNames = this.serviceNames(data.getServiceNames(), javaCodeGenClassMetas);
        List<String[]> dtoNames = this.dtoNames(data.getDtoNames(), javaCodeGenClassMetas);

        int length = javaCodeGenClassMetas.length;
        for (int i = 0; i < length; i++) {
            JavaCodeGenClassMeta javaCodeGenClassMeta = javaCodeGenClassMetas[i];
            // 生成DTO
            this.generateDtoFile(javaCodeGenClassMeta, dtoNames.get(i));
            // 生成 Service
            this.generateCodeFile(javaCodeGenClassMeta, this.getServiceTemplate(), serviceNames[i]);
            // 生成Controller
            this.generateCodeFile(javaCodeGenClassMeta, this.getControllerTemplate(), controllerNames[i]);
        }

    }

    private void generateDtoFile(JavaCodeGenClassMeta meta, String[] names) {
        for (String name : names) {
            this.generateCodeFile(meta, this.getDtoTemplate(), name);
        }
    }


    private void generateCodeFile(JavaCodeGenClassMeta meta, Template template, String fileName) {
        String packagePath = meta.getPackagePath();
        packagePath = packagePath.replace(meta.getName(), fileName);
        if (packagePath.endsWith(EXT_NAME)) {
            packagePath = packagePath.replace(EXT_NAME, "").replace(".", "");
        }
        String output = Paths.get(
                MessageFormat.format("{0}{1}.{2}",
                        this.outputPath,
                        this.normalizationFilePath(packagePath),
                        EXT_NAME)).toString();

        //如果生成的文件没有文件名称，即输出如今形如 /a/b/.extName的格式
        if (output.contains(MessageFormat.format("{0}.{1}", File.separator, EXT_NAME))) {
            log.warn("类{}，的生成输入路径有误,{}", meta.getName(), output);
        }
        File file = new File(output);
        if (file.exists()) {
            if (System.currentTimeMillis() - file.lastModified() <= LAST_MODIFIED_MINUTE * 60 * 1000) {
                log.warn("文件{}在{}分钟内已经生成过，跳过生成", output, LAST_MODIFIED_MINUTE);
                return;
            }
        }
        FileUtil.createDirectory(output.substring(0, output.lastIndexOf(File.separator)));
        log.info("生成类{}的文件，输出到{}目录", meta.getName(), output);
        Writer writer = null;
        try {
            //输出
            writer = new OutputStreamWriter(new FileOutputStream(output),
                    StandardCharsets.UTF_8);
            //添加自定义方法
            template.process(meta, writer);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String normalizationFilePath(String packagePath) {

        return packagePath.replaceAll("\\.", PathResolve.RIGHT_SLASH);
    }

    private Template getControllerTemplate() {
        return this.templateLoader.load(CONTROLLER_TEMPLATE);
    }

    private Template getServiceTemplate() {
        return this.templateLoader.load(SERVICE_TEMPLATE);
    }

    private Template getDtoTemplate() {
        return this.templateLoader.load(DTO_TEMPLATE);
    }

    private String[] controllerNames(String[] controllerNames, JavaCodeGenClassMeta[] metas) {
        boolean isNotNull = controllerNames != null;
        if (isNotNull && controllerNames.length == metas.length) {
            return controllerNames;
        }

        if (isNotNull && controllerNames.length == 1) {
            return Arrays.stream(metas)
                    .map(meta -> controllerNames[0])
                    .toArray(String[]::new);
        }

        return Arrays.stream(metas)
                .map(meta -> MessageFormat.format("{0}{1}", meta.getName(), CONTROLLER_NAME_SUFFIX))
                .toArray(String[]::new);
    }

    private String[] serviceNames(String[] serviceNames, JavaCodeGenClassMeta[] metas) {
        boolean isNotNull = serviceNames != null;
        if (isNotNull && serviceNames.length == metas.length) {
            return serviceNames;
        }

        if (isNotNull && serviceNames.length == 1) {
            return Arrays.stream(metas)
                    .map(meta -> serviceNames[0])
                    .toArray(String[]::new);
        }
        return Arrays.stream(metas)
                .map(meta -> MessageFormat.format("{0}{1}", meta.getName(), SERVICE_NAME_SUFFIX))
                .toArray(String[]::new);
    }

    private List<String[]> dtoNames(List<String[]> dtoNames, JavaCodeGenClassMeta[] metas) {
        boolean isNotNull = dtoNames != null;
        if (dtoNames != null && dtoNames.size() == metas.length) {
            return dtoNames;
        }
        if (isNotNull && dtoNames.size() == 1) {
            return Arrays.stream(metas)
                    .map(meta -> dtoNames.get(0))
                    .collect(Collectors.toList());
        }

        return Arrays.stream(metas)
                .map(meta -> Stream.of(DTO_PREFIX_NAMES)
                        .map(prefix -> MessageFormat.format("{0}{1}", prefix, meta.getName()))
                        .toArray(String[]::new))
                .collect(Collectors.toList());

    }
}
