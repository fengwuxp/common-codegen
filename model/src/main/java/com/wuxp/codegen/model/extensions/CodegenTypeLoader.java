package com.wuxp.codegen.model.extensions;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.List;

/**
 * 用于加载
 *
 * @author wuxp
 */
public interface CodegenTypeLoader<C extends CommonCodeGenClassMeta> {


    List<C> load();
}
