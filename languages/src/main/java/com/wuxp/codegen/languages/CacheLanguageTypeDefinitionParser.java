package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxp
 */
public class CacheLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends DelegateLanguageTypeDefinitionParser<C>{

    /**
     * 处理结果缓存
     */
    private final Map<Class<?>, C> resultCaches = new HashMap<>(256);

    private final ClassCircleDependsAnalyser classCircleDependsAnalyser;

    public CacheLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate) {
        super(delegate);
        this.classCircleDependsAnalyser = new ClassCircleDependsAnalyser();
    }

    @Override
    public C parse(Class<?> source) {
        if (classCircleDependsAnalyser.joinNode(source)) {
            return getDelegate().parse(source);
        }
        // 出现环形依赖
        return getCodeGenClassMeta(source);
    }

    public C put(C meta) {
        resultCaches.put(meta.getSource(), meta);
        return meta;
    }

    private C getCodeGenClassMeta(Class<?> clazz) {
        return resultCaches.get(clazz);
    }
}
