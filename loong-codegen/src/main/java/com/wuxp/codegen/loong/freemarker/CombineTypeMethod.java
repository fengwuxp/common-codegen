package com.wuxp.codegen.loong.freemarker;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import freemarker.template.DefaultArrayAdapter;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author wxup
 */
@Slf4j
public class CombineTypeMethod implements TemplateMethodModelEx {

    /**
     * 泛型合并策略
     */
    protected CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Assert.isTrue(!CollectionUtils.isEmpty(arguments), "arguments is null or is empty");
        DefaultArrayAdapter arrayAdapter = (DefaultArrayAdapter) arguments.get(0);
        CommonCodeGenClassMeta[] classMetas = (CommonCodeGenClassMeta[]) arrayAdapter.getAdaptedObject(CommonCodeGenClassMeta.class);
        Assert.isTrue(!ObjectUtils.isEmpty(classMetas), "combine type  classMetas is null or is empty");
        if (log.isDebugEnabled()) {
            log.debug("要合并的泛型描述，length={}", classMetas.length);
        }
        return this.combineTypeDescStrategy.combine(classMetas);
    }
}
