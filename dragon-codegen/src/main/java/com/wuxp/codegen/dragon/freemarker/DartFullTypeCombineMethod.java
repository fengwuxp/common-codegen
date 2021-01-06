package com.wuxp.codegen.dragon.freemarker;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.types.DartFullTypeCombineTypeDescStrategy;
import freemarker.template.DefaultArrayAdapter;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 合并Dart Built_value FullType
 *
 * @author wxup
 */
@Slf4j
public class DartFullTypeCombineMethod implements TemplateMethodModelEx {


  protected CombineTypeDescStrategy combineTypeDescStrategy = new DartFullTypeCombineTypeDescStrategy();

  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() == 0) {
      throw new RuntimeException("arguments size is 0");
    }
    DefaultArrayAdapter arrayAdapter = (DefaultArrayAdapter) arguments.get(0);

    CommonCodeGenClassMeta[] commonCodeGenClassMetas = (CommonCodeGenClassMeta[]) arrayAdapter
        .getAdaptedObject(CommonCodeGenClassMeta.class);

    String combine = this.combineTypeDescStrategy.combine(commonCodeGenClassMetas);
    log.info("Built Value FullType: {}", combine);
    return combine;
  }
}
