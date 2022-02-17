package com.wuxp.codegen.loong.freemarker;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import freemarker.template.DefaultArrayAdapter;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 合并Dart Built_value FullType
 *
 * @author wxup
 */
@Slf4j
public final class DartFullTypeCombineMethod implements TemplateMethodModelEx {

    private final CombineTypeDescStrategy combineTypeDescStrategy = new DartFullTypeCombineTypeDescStrategy();

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Assert.notEmpty(arguments, "arguments is null or is empty");
        DefaultArrayAdapter arrayAdapter = (DefaultArrayAdapter) arguments.get(0);
        CommonCodeGenClassMeta[] commonCodeGenClassMetas = (CommonCodeGenClassMeta[]) arrayAdapter
                .getAdaptedObject(CommonCodeGenClassMeta.class);
        String combine = this.combineTypeDescStrategy.combine(commonCodeGenClassMetas);
        if (log.isInfoEnabled()) {
            log.info("Built Value FullType: {}", combine);
        }
        return combine;
    }
}
