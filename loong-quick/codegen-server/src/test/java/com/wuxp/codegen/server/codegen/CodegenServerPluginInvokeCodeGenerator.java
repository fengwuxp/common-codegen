package com.wuxp.codegen.server.codegen;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.MavenPluginInvokeCodeGenerator;
import com.wuxp.codegen.server.controller.LoongCodegenController;
import com.wuxp.codegen.starter.LoongSdkCodeGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class CodegenServerPluginInvokeCodeGenerator implements MavenPluginInvokeCodeGenerator {

    @Override
    public void generate(String output, List<ClientProviderType> types) {

        log.info("output:{}，type：{}", output, types);
        LoongSdkCodeGenerator loongSdkCodeGenerator = new LoongSdkCodeGenerator("com.wuxp.codegen.server.controller");
        loongSdkCodeGenerator.setOutputPath(output);
        loongSdkCodeGenerator.setClientProviderTypes(types);
        loongSdkCodeGenerator.setIgnoreClasses(Arrays.asList(LoongCodegenController.class));
        loongSdkCodeGenerator.generate();
    }
}
