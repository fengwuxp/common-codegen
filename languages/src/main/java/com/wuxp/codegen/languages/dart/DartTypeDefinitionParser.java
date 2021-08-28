package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractLanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.wuxp.codegen.meta.transform.spring.TypeScriptRequestMappingTransformer.TS_FEIGN_CLIENT_ANNOTATION_NAME;
import static com.wuxp.codegen.model.languages.dart.DartClassMeta.BUILT_SERIALIZERS;

public class DartTypeDefinitionParser extends AbstractLanguageTypeDefinitionParser<DartClassMeta> {

    public static final String DART_FEIGN_CLIENT_ANNOTATION_NAME = "FeignClient";

    public DartTypeDefinitionParser(LanguageTypeDefinitionPublishParser<?> languageTypeDefinitionPublishParser, PackageNameConvertStrategy packageNameConvertStrategy) {
        super(languageTypeDefinitionPublishParser, packageNameConvertStrategy);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DartClassMeta parse(Class<?> source) {
        DartClassMeta dartClassMeta = super.parse(source);
        if (dartClassMeta == null) {
            return null;
        }

        if (isRequestObject(dartClassMeta)) {
            Map<String, CommonCodeGenClassMeta> dependencies = (Map<String, CommonCodeGenClassMeta>) dartClassMeta.getDependencies();
            dependencies.put(BUILT_SERIALIZERS.getName(), BUILT_SERIALIZERS);
        } else {
            getFeignAnnotation(dartClassMeta).ifPresent(commonCodeGenAnnotation -> commonCodeGenAnnotation.setName(DART_FEIGN_CLIENT_ANNOTATION_NAME));
        }

        if (StringUtils.hasText(dartClassMeta.getPackagePath())) {
            String packagePath = DartFileNamedUtils.dartFileNameConverter(dartClassMeta.getPackagePath());
            dartClassMeta.setPackagePath(packagePath);
        }
        return dartClassMeta;
    }

    private boolean isRequestObject(DartClassMeta dartClassMeta) {
        return ObjectUtils.isEmpty(dartClassMeta.getMethodMetas());
    }

    private Optional<CommonCodeGenAnnotation> getFeignAnnotation(DartClassMeta dartClassMeta) {
        return Arrays.stream(dartClassMeta.getAnnotations())
                .filter(annotation -> TS_FEIGN_CLIENT_ANNOTATION_NAME.equals(annotation.getName()))
                .findFirst();
    }

    @Override
    public DartClassMeta newElementInstance() {
        return new DartClassMeta();
    }


}
