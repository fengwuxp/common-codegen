package com.oak.codegen;

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
public class OAKTemplateStrategy<C extends CommonCodeGenClassMeta> implements TemplateStrategy<C> {

    protected TemplateLoader<Template> templateLoader;

    @Override
    public void build(C data) {

        //根据是否为接口类型的元数据还是dto的类型的元数据加载不同的模板
        if (data.getMethodMetas().length > 0) {
            //接口类型
        } else {
            //DTO or enum
            if (ClassType.ENUM.equals(data.getClassType())) {

            } else {
                //区分请求对象还是响应对象
            }
        }

//        data.getClassType()


//        this.templateLoader.load()

    }
}
