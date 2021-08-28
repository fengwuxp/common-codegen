package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合并超类上的字段
 * @author wuxp
 */
public class CombineSupperFiledPostProcessor implements LanguageDefinitionPostProcessor<DartClassMeta> {

    @Override
    public void postProcess(DartClassMeta meta) {
        meta.setFieldMetas(combineSupperClassFields(meta));
    }

    private CommonCodeGenFiledMeta[] combineSupperClassFields(DartClassMeta classMeta) {
        if (ObjectUtils.isEmpty(classMeta.getFieldMetas())) {
            return new CommonCodeGenFiledMeta[0];
        }
        List<CommonCodeGenFiledMeta> fieldMates = new ArrayList<>(Arrays.asList(classMeta.getFieldMetas()));
        CommonCodeGenClassMeta supperMeta = classMeta.getSuperClass();
        if (supperMeta == null || ObjectUtils.isEmpty(supperMeta.getFieldMetas())) {
            return classMeta.getFieldMetas();
        }
        fieldMates.addAll(Arrays.stream(supperMeta.getFieldMetas()).collect(Collectors.toList()));
        return fieldMates.stream().distinct().toArray(CommonCodeGenFiledMeta[]::new);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, DartClassMeta.class);
    }
}
