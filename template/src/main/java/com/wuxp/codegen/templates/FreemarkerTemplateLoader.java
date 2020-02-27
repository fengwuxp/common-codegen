package com.wuxp.codegen.templates;

import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;
import freemarker.ext.beans.MapModel;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * freemarker的模板加载器
 */
@Slf4j
public class FreemarkerTemplateLoader extends AbstractTemplateLoader<Template> {


    protected static final Map<String, String> AUTO_IMPORT_TEMPLATES = new LinkedHashMap<>();


    static {
        //导入自定义方法 @link {dragon-codegen/src/main/resources/typescript/common/customize_method.ftl}
        AUTO_IMPORT_TEMPLATES.put("customize_method", "common/customize_method.ftl");
    }

    protected Configuration configuration;


    public FreemarkerTemplateLoader(LanguageDescription language) {
        this(language, null);
    }


    public FreemarkerTemplateLoader(LanguageDescription language, Map<String, Object> sharedVariables) {
        this(language, TemplateFileVersion.DEFAULT.getVersion(), sharedVariables);
    }

    public FreemarkerTemplateLoader(LanguageDescription language, String templateFileVersion, Map<String, Object> sharedVariables) {
        this(language, templateFileVersion, initConfiguration(sharedVariables));
    }

    public FreemarkerTemplateLoader(LanguageDescription language, TemplateFileVersion templateFileVersion, Map<String, Object> sharedVariables) {
        this(language, templateFileVersion.getVersion(), initConfiguration(sharedVariables));
    }


    public FreemarkerTemplateLoader(LanguageDescription language, String templateFileVersion, Configuration configuration) {
        super(language, templateFileVersion);
        this.configuration = configuration;
    }

    @Override
    public Template load(String templateName) {

        try {
            String templatePath = MessageFormat.format("{0}/{1}{2}/{3}",
                    this.language.getCodeGenType().name().toLowerCase(),
                    this.language.getTemplateDir(),
                    StringUtils.hasText(this.templateFileVersion) ? MessageFormat.format("/{0}", this.templateFileVersion) : "",
                    templateName);
            Template template = configuration.getTemplate(templatePath);
//            String name = template.getName();
//            Map<String, String> autoImportTemplates = new LinkedHashMap<>();
//            for (Map.Entry<String, String> entry : AUTO_IMPORT_TEMPLATES.entrySet()) {
//                autoImportTemplates.put(entry.getKey(),  entry.getValue());
//            }
            template.setAutoImports(AUTO_IMPORT_TEMPLATES);
            return template;
        } catch (IOException e) {
            log.error("获取模板失败，模板名称：" + templateName, e);
        }
        return null;
    }

    private static Configuration initConfiguration(Map<String, Object> sharedVariables) {
        //创建一个合适的Configuration对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        DefaultObjectWrapper objectWrapper = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28).build();
        configuration.setObjectWrapper(objectWrapper);
        //这个一定要设置，不然在生成的页面中 会乱码
        configuration.setDefaultEncoding("UTF-8");

        //支持从jar中加载模板
        configuration.setClassForTemplateLoading(FreemarkerTemplateLoader.class, "/");

        if (sharedVariables == null) {
            throw new RuntimeException("sharedVariables is null");
        }
        if (!sharedVariables.containsKey(CODE_RUNTIME_PLATFORM_KEY)) {
            throw new RuntimeException(String.format("sharedVariables need variable ：%s", CODE_RUNTIME_PLATFORM_KEY));
        }

        try {
            configuration.setAllSharedVariables(new MapModel(sharedVariables, objectWrapper));
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }

        return configuration;
    }

}
