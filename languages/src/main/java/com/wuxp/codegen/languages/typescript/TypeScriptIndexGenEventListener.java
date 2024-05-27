package com.wuxp.codegen.languages.typescript;

import com.google.common.collect.ImmutableSet;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.event.CodeGenEvent;
import com.wuxp.codegen.core.event.CodeGenEventListener;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * typescript index 文件生成
 *
 * @author wuxp
 * @date 2024-05-27 13:23
 **/
public class TypeScriptIndexGenEventListener implements CodeGenEventListener {


    // dto 对象
    private final Set<CommonCodeGenClassMeta> dtoMetas = new TreeSet<>();

    // feign client 对象
    private final Set<CommonCodeGenClassMeta> feignClientMetas = new TreeSet<>();

    @Override
    public void onApplicationEvent(CodeGenEvent event) {
        if (CodeGenEvent.CodeGenEventStatus.SCAN_CODEGEN.equals(event.getStatus())) {
            putEventMeta(event);
        }
    }

    @Override
    public Set<CommonCodeGenClassMeta> getEventCodeGenMetas() {
        return ImmutableSet.of(buildSdkIndexMeta());
    }

    private void putEventMeta(CodeGenEvent event) {
        CommonCodeGenClassMeta genData = event.getSource();
        boolean isClientObject = genData.getMethodMetas() != null && genData.getMethodMetas().length > 0;
        if (isClientObject) {
            feignClientMetas.add(genData);
        } else {
            dtoMetas.add(genData);
        }
    }

    private TypescriptClassMeta buildSdkIndexMeta() {
        TypescriptClassMeta result = new TypescriptClassMeta();
        Map<String, Object> tags = new HashMap<>();
        tags.put("clients", feignClientMetas);
        tags.put("models", dtoMetas);
        tags.put("functional", Objects.equals(CodegenConfigHolder.getConfig().getProviderType(), ClientProviderType.TYPESCRIPT_FEIGN));
        tags.put(TEMPLATE_PATH_TAG_NAME, "index");
        result.setTags(tags);
        result.setPackagePath("./index");
        result.setNeedGenerate(true);
        return result;
    }
}
