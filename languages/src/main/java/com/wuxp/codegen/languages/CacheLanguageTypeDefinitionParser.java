package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxp
 */
public class CacheLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    /**
     * 处理结果缓存
     */
    private final Map<Class<?>, C> resultCaches = new HashMap<>(256);

    private final ClassCircleDependsAnalyser classCircleDependsAnalyser;

    private final LanguageTypeDefinitionParser<C> delegate;

    public CacheLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate) {
        this.delegate = delegate;
        this.classCircleDependsAnalyser = new ClassCircleDependsAnalyser();
    }

    @Override
    public C parse(Class<?> source) {
        if (classCircleDependsAnalyser.joinNode(source)) {

            return delegate.parse(source);
        }
        // 出现环形依赖
        return getCodeGenClassMeta(source);
    }

    @Override
    public C newInstance() {
        return delegate.newInstance();
    }

    public void put(C meta) {
        resultCaches.put(meta.getSource(), meta);
    }

    private C getCodeGenClassMeta(Class<?> clazz) {
        return resultCaches.get(clazz);
    }
}
