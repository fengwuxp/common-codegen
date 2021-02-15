package com.wuxp.codegen.templates;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.util.ClassLoaderUtils;
import com.wuxp.codegen.model.TemplateFileVersion;
import freemarker.ext.beans.MapModel;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * freemarker的模板加载器
 *
 * @author wxup
 */
@Slf4j
public class FreemarkerTemplateLoader extends AbstractTemplateLoader<Template> {


    private String templateBaseDir = "clients";

    protected Configuration configuration;


    public FreemarkerTemplateLoader(ClientProviderType clientProviderType) {
        this(clientProviderType, null);
    }


    public FreemarkerTemplateLoader(ClientProviderType clientProviderType, Map<String, Object> sharedVariables) {
        this(clientProviderType, TemplateFileVersion.DEFAULT.getVersion(), sharedVariables);
    }

    public FreemarkerTemplateLoader(ClientProviderType clientProviderType, String templateFileVersion, Map<String, Object> sharedVariables) {
        this(clientProviderType, templateFileVersion, initConfiguration(sharedVariables));
    }

    public FreemarkerTemplateLoader(ClientProviderType clientProviderType, TemplateFileVersion templateFileVersion,
                                    Map<String, Object> sharedVariables) {
        this(clientProviderType, templateFileVersion.getVersion(), initConfiguration(sharedVariables));
    }


    public FreemarkerTemplateLoader(ClientProviderType clientProviderType, String templateFileVersion, Configuration configuration) {
        super(clientProviderType, templateFileVersion);
        this.configuration = configuration;
    }

    @Override
    public Template load(String templateName) {

        try {
            String templatePath = MessageFormat.format("{0}/{1}/{2}{3}",
                    this.templateBaseDir,
                    this.clientProviderType.name().toLowerCase(),
                    StringUtils.hasText(this.templateFileVersion) ? MessageFormat.format("{0}", this.templateFileVersion) : "",
                    templateName);
            return configuration.getTemplate(templatePath);
        } catch (IOException e) {
            log.error("获取模板失败，模板名称：{}", templateName, e);
        }
        return null;
    }

    public void setTemplateBaseDir(String templateBaseDir) {
        this.templateBaseDir = templateBaseDir;
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
        Map<Object, Object> templateMethods = initCustomizeMethods();
        sharedVariables.put("customizeMethod", templateMethods);
        MapModel mapModel = new MapModel(sharedVariables, objectWrapper);
        try {
            configuration.setAllSharedVariables(mapModel);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }

        return configuration;
    }

    /**
     * 初始化自定义的freemarker 模板中调用的方法
     */
    private static Map<Object, Object> initCustomizeMethods() {
        Map<Object, Object> templateMethods = new HashMap<>();
        try {
            templateMethods.put("combineType",  ClassLoaderUtils.loadClass("com.wuxp.codegen.loong.freemarker.CombineTypeMethod").newInstance());
            templateMethods.put("pathResolve",  ClassLoaderUtils.loadClass("com.wuxp.codegen.loong.freemarker.PathResolveMethod").newInstance());
            templateMethods.put("combineDartFullType", ClassLoaderUtils.loadClass("com.wuxp.codegen.loong.freemarker.DartFullTypeCombineMethod").newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return templateMethods;
    }

}
