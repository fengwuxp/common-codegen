package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.CodeFormatter;
import com.wuxp.codegen.core.constant.FeignApiSdkTemplateName;
import com.wuxp.codegen.core.strategy.FileNameGenerateStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.core.util.PathResolveUtils;
import com.wuxp.codegen.format.LanguageCodeFormatter;
import com.wuxp.codegen.meta.util.FileUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.templates.TemplateLoader;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.MessageFormat;

import static com.wuxp.codegen.core.event.CodeGenEventListener.TEMPLATE_PATH_TAG_NAME;


/**
 * 模板处理策略
 * <p>
 * 负责将数据和模板合并，生成最终的文件
 * </p>
 *
 * @author wuxp
 */
@Slf4j
public class LoongSimpleTemplateStrategy implements TemplateStrategy<CommonCodeGenClassMeta> {

    /**
     * 在LAST_MODIFIED_MINUTE分钟内生成的文件不在生成，为了打断递归和循环
     *
     * @see #ONE_MINUTE_MILLIS
     */
    public static final float LAST_MODIFIED_MINUTE = 0.1f;

    /**
     * 1分钟的毫秒数据
     */
    private static final int ONE_MINUTE_MILLIS = 60 * 1000;


    /**
     * 模板加载器
     */
    protected TemplateLoader<Template> templateLoader;

    /**
     * 输出的根目录
     */
    protected String outputPath;

    /**
     * 文件扩展名称
     */
    protected String extName;

    /**
     * 文件生成策略
     */
    protected FileNameGenerateStrategy fileNameGenerateStrategy;

    /**
     * 代码格式化
     */
    private final CodeFormatter codeFormatter;

    public LoongSimpleTemplateStrategy(TemplateLoader<Template> templateLoader,
                                       String outputPath,
                                       String extName,
                                       boolean isDeletedOutputDirectory,
                                       FileNameGenerateStrategy fileNameGenerateStrategy,
                                       CodeFormatter codeFormatter) {
        this.templateLoader = templateLoader;
        // 计算路径中的相对路径
        this.outputPath = PathResolveUtils.relative(outputPath.endsWith(File.separator) ? outputPath : outputPath + File.separator, ".");
        this.extName = extName;
        this.fileNameGenerateStrategy = fileNameGenerateStrategy;

        if (isDeletedOutputDirectory) {
            //删除原本的目录
            boolean r = FileUtils.deleteDirectory(this.outputPath);
            if (log.isInfoEnabled()) {
                log.info("删除原本的输出目录{}，删除{}", this.outputPath, r ? "成功" : "失败");
            }
        }
        if (codeFormatter == null) {
            codeFormatter = new LanguageCodeFormatter();
        }
        this.codeFormatter = codeFormatter;
    }

    public LoongSimpleTemplateStrategy(TemplateLoader<Template> templateLoader,
                                       String outputPath,
                                       String extName,
                                       boolean isDeletedOutputDirectory, CodeFormatter codeFormatter) {
        this(templateLoader, outputPath, extName, isDeletedOutputDirectory, FileNameGenerateStrategy.DEFAULT, codeFormatter);
    }

    @Override
    public void build(CommonCodeGenClassMeta data) throws Exception {

        //根据是否为接口类型的元数据还是dto的类型的元数据加载不同的模板
        String templatePath = this.getTemplatePath(data);
        Template template = this.templateLoader.load(templatePath);
        Assert.notNull(template, "获取模板失败，templatePath = " + templatePath);
        String packagePath = this.fileNameGenerateStrategy.generateName(data.getPackagePath());
        String outputPath = Paths.get(MessageFormat.format("{0}{1}.{2}", this.outputPath, FileUtils.packageNameToFilePath(packagePath), extName)).toString();
        // 如果生成的文件没有文件名称，即输出如今形如 /a/b/.extName的格式
        if (outputPath.contains(MessageFormat.format("{0}.{1}", File.separator, extName))) {
            log.warn("类{}，的生成输入路径有误,{}", data.getName(), outputPath);
        }

        if (fileIsCodegen(outputPath)) {
            log.warn("文件{}在{}分钟内已经生成过，跳过生成", outputPath, LAST_MODIFIED_MINUTE);
            return;
        }
        FileUtils.createDirectoryRecursively(outputPath.substring(0, outputPath.lastIndexOf(File.separator)));
        if (log.isInfoEnabled()) {
            log.info("生成类{}的文件，输出到{}目录", data.getName(), outputPath);
        }
        //输出
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8)) {
            // 添加自定义方法
            template.process(data, writer);
        }
        // 格式化代码
        codeFormatter.format(outputPath);
    }

    private boolean fileIsCodegen(String output) {
        File file = new File(output);
        if (file.exists()) {
            // 文件是否还有效
            return System.currentTimeMillis() - file.lastModified() <= LAST_MODIFIED_MINUTE * ONE_MINUTE_MILLIS;
        }
        return false;
    }

    protected String getTemplatePath(CommonCodeGenClassMeta data) {
        CommonCodeGenMethodMeta[] methodMetas = data.getMethodMetas();
        String templatePath = data.getTag(TEMPLATE_PATH_TAG_NAME);
        if (StringUtils.hasText(templatePath)) {
            return templatePath;
        }
        if (methodMetas == null || methodMetas.length == 0) {
            //DTO or enum
            if (ClassType.ENUM.equals(data.getClassType())) {
                templatePath = FeignApiSdkTemplateName.API_ENUM_TEMPLATE_NAME;
            } else {
                //区分请求对象还是响应对象
                templatePath = FeignApiSdkTemplateName.API_REQUEST_TEMPLATE_NAME;
            }
        } else {
            //api 接口
            templatePath = FeignApiSdkTemplateName.API_SERVICE_TEMPLATE_NAME;

        }
        return templatePath;
    }


}
