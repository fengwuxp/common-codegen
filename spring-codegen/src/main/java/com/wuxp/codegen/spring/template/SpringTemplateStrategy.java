package com.wuxp.codegen.spring.template;

import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.spring.model.DtoObjectType;
import com.wuxp.codegen.spring.model.JavaSpringCodeGenClassMeta;
import com.wuxp.codegen.templates.TemplateLoader;
import com.wuxp.codegen.utils.FileUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

import static com.wuxp.codegen.spring.model.JavaSpringCodeGenClassMeta.DOT_TYPE_REQ_PREFIX;

@Slf4j
public class SpringTemplateStrategy implements TemplateStrategy<JavaSpringCodeGenClassMeta> {

    //在LAST_MODIFIED_MINUTE内生成的文件不在生成
    public static final float LAST_MODIFIED_MINUTE = 0.1f;

    public static final String CONTROLLER_TEMPLATE = "spring/controller.ftl";

    public static final String SERVICE_TEMPLATE = "spring/service.ftl";

    public static final String SERVICE_IMPLEMENTS_TEMPLATE = "spring/service_impl.ftl";

    public static final String DTO_REQ_TEMPLATE = "spring/{}_req.ftl";

    public static final String DTO_RESP_TEMPLATE = "spring/info.ftl";

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
        String[] controllerNames = data.getControllerNames();
        String[] serviceNames = data.getServiceNames();
        List<Map<DtoObjectType, String>> reqDotNames = data.getReqDotNames();
        String[] respDotNames = data.getRespDotNames();

        int length = javaCodeGenClassMetas.length;
//        List<CommonCodeGenClassMeta> dtoDependencies = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            JavaCodeGenClassMeta javaCodeGenClassMeta = javaCodeGenClassMetas[i];

            // 生成DTO
            Map<DtoObjectType, String> packagesNames = reqDotNames.get(i);
            packagesNames.put(DtoObjectType.RESP_INFO, respDotNames[i]);
            List<CommonCodeGenClassMeta> dtoDependencies = this.generateDtoFile(javaCodeGenClassMeta, packagesNames);

            // 生成服务
            CommonCodeGenClassMeta commonCodeGenClassMeta = this.generateServiceFile(new JavaCodeGenClassMeta[]{javaCodeGenClassMeta}, serviceNames[i], dtoDependencies);

            // 生成控制器
            this.generateControllerFile(javaCodeGenClassMeta, controllerNames[i], dtoDependencies,commonCodeGenClassMeta);
        }


    }


    /**
     * 生成DTO对象
     *
     * @param meta
     * @param packagesNames
     */
    private List<CommonCodeGenClassMeta> generateDtoFile(JavaCodeGenClassMeta meta, Map<DtoObjectType, String> packagesNames) {

        List<CommonCodeGenClassMeta> list = new ArrayList<>(4);

        for (Map.Entry<DtoObjectType, String> entry : packagesNames.entrySet()) {
            Template dtoTemplate = this.getReqDtoTemplate(entry.getKey().getPlaceholder());
            JavaCodeGenClassMeta javaCodeGenClassMeta = new JavaCodeGenClassMeta();

            String packagePath = entry.getValue();
            javaCodeGenClassMeta.setName(this.getClassName(packagePath));
            javaCodeGenClassMeta.setPackagePath(packagePath);
            javaCodeGenClassMeta.setFieldMetas(meta.getFieldMetas());
            // TODO 按照生成DTO的不同的类型过滤数据

            this.generateCodeFile(javaCodeGenClassMeta, dtoTemplate);
            list.add(javaCodeGenClassMeta);
        }

        return list;
    }


    /**
     * 生成服务层
     *
     * @param metas
     * @param servicePackage
     * @param codeGenClassMetas dto的依赖列表
     */
    private CommonCodeGenClassMeta generateServiceFile(JavaCodeGenClassMeta[] metas,
                                                       String servicePackage,
                                                       List<CommonCodeGenClassMeta> codeGenClassMetas) {

        CommonCodeGenClassMeta codeGenClassMeta = new CommonCodeGenClassMeta();
        codeGenClassMeta.setName(this.getClassName(servicePackage));
        codeGenClassMeta.setPackagePath(servicePackage);

        // 合并方法
        List<CommonCodeGenMethodMeta> methodMetas = new ArrayList<>(16);
        Arrays.asList(metas).forEach(meta -> {

            List<CommonCodeGenMethodMeta> javaMethodMetas = new ArrayList<>();
            DOT_TYPE_REQ_PREFIX.forEach((key, methodPrefix) -> {
                CommonCodeGenMethodMeta javaMethodMeta = new CommonCodeGenMethodMeta();
                javaMethodMeta.setName(MessageFormat.format("{0}{1}", methodPrefix.toLowerCase(), meta.getName()));
                String paramName = MessageFormat.format("{0}{1}{2}", methodPrefix, meta.getName(), "Req");
                Map<String, Object> tags = new HashMap<>();
                tags.put("req", paramName);
                javaMethodMeta.setTags(tags);
            });
            methodMetas.addAll(javaMethodMetas);
        });

        HashMap<String, CommonCodeGenClassMeta> dependenciesMap = new HashMap<>();
        codeGenClassMetas.forEach(meta -> {
            dependenciesMap.put(meta.getName(), meta);
        });
        codeGenClassMeta.setDependencies(dependenciesMap);
        //生成接口
        this.generateCodeFile(codeGenClassMeta, this.getServiceTemplate());

        // 生成接口实现
        JavaCodeGenClassMeta metaImpl = new JavaCodeGenClassMeta();
        BeanUtils.copyProperties(codeGenClassMeta, metaImpl);
        metaImpl.setName(metaImpl.getName() + "Impl");
        metaImpl.setPackagePath(metaImpl.getPackagePath() + "Impl");
        this.generateCodeFile(metaImpl, this.templateLoader.load(SERVICE_IMPLEMENTS_TEMPLATE));

        return codeGenClassMeta;
    }

    /**
     * 生成控制器
     *
     * @param javaCodeGenClassMeta
     * @param controllerPage
     * @param codeGenClassMetas
     */
    private void generateControllerFile(JavaCodeGenClassMeta javaCodeGenClassMeta,
                                        String controllerPage,
                                        List<CommonCodeGenClassMeta> codeGenClassMetas,
                                        CommonCodeGenClassMeta classMeta) {
        CommonCodeGenClassMeta codeGenClassMeta = new CommonCodeGenClassMeta();
        codeGenClassMeta.setName(this.getClassName(controllerPage));
        codeGenClassMeta.setPackagePath(controllerPage);

        HashMap<String, CommonCodeGenClassMeta> dependenciesMap = new HashMap<>();
        codeGenClassMetas.forEach(meta -> {
            dependenciesMap.put(meta.getName(), meta);
        });
        codeGenClassMeta.setDependencies(dependenciesMap);
        Map<String, Object> tags = codeGenClassMeta.getTags();
        String name = classMeta.getName();
        tags.put("serviceClassName", name);
        tags.put("serviceVariableName", ToggleCaseUtil.toggleFirstChart(name));

        this.generateCodeFile(codeGenClassMeta, this.getControllerTemplate());

    }

    /**
     * 生成代码文件
     *
     * @param meta
     * @param template
     */
    private void generateCodeFile(CommonCodeGenClassMeta meta, Template template) {
        // 全类路径名称
        String packagePath = meta.getPackagePath();
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

    private Template getReqDtoTemplate(String name) {
        return this.templateLoader.load(DTO_REQ_TEMPLATE.replace("{}", name.toLowerCase()));
    }

    private Template getRespDtoTemplate(String name) {
        return this.templateLoader.load(DTO_RESP_TEMPLATE.replace("{}", name.toLowerCase()));
    }

    private String getClassName(String packageName) {

        String[] names = packageName.split("\\.");
        return names[names.length - 1];
    }
}
