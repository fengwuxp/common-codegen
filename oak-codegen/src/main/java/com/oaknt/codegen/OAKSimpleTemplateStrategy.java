package com.oaknt.codegen;

import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.templates.TemplateLoader;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;


/**
 * 模板处理策略
 * <p>
 * 负责将数据和模板合并，生成最终的文件
 * </p>
 *
 * @param <C>
 */
@Slf4j
public class OAKSimpleTemplateStrategy implements TemplateStrategy<CommonCodeGenClassMeta> {

    /**
     * 模板加载器
     */
    protected TemplateLoader<Template> templateLoader;

    public OAKSimpleTemplateStrategy(TemplateLoader<Template> templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override
    public void build(CommonCodeGenClassMeta data) {

        //根据是否为接口类型的元数据还是dto的类型的元数据加载不同的模板
        String templateName = null;
        if (data.getMethodMetas().length > 0) {
            //api 接口
            templateName = TemplateStrategy.API_SERVICE_TEMPLATE_NAME;
        } else {
            //DTO or enum
            if (ClassType.ENUM.equals(data.getClassType())) {
                templateName = TemplateStrategy.API_ENUM_TEMPLATE_NAME;
            } else {
                //区分请求对象还是响应对象
                templateName = TemplateStrategy.API_REQUEST_TEMPLATE_NAME;
            }
        }

//        data.getClassType()


//        this.templateLoader.load()

    }
}
