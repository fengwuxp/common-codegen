package com.wuxp.codegen.templates;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
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

    protected String language;

    public FreemarkerTemplateLoader(String language) {
        super(language);
        //创建一个合适的Configuration对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28).build());
        //这个一定要设置，不然在生成的页面中 会乱码
        configuration.setDefaultEncoding("UTF-8");

        //支持从jar中加载模板
        configuration.setClassForTemplateLoading(FreemarkerTemplateLoader.class, "/");
        this.language = language;


        this.configuration = configuration;
    }

    @Override
    public Template load(String templateName) {

        try {
            Template template = configuration.getTemplate(MessageFormat.format("{0}/{1}", this.language, templateName));
            template.setAutoImports(AUTO_IMPORT_TEMPLATES);
            return template;
        } catch (IOException e) {
            log.error("获取模板失败，模板名称：" + templateName, e);
        }
        return null;
    }
}
