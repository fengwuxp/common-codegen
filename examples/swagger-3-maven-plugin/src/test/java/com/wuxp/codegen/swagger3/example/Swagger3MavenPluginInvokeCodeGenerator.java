package com.wuxp.codegen.swagger3.example;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.MavenPluginInvokeCodeGenerator;
import com.wuxp.codegen.starter.LoongCodeGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Swagger3MavenPluginInvokeCodeGenerator implements MavenPluginInvokeCodeGenerator {

    @Override
    public void generate(String output, List<ClientProviderType> types) {

        log.info("output:{}，type：{}", output, types);
        LoongCodeGenerator loongSdkCodeGenerator = new LoongCodeGenerator("com.wuxp.codegen");
        loongSdkCodeGenerator.setOutputPath(output);
        loongSdkCodeGenerator.setClientProviderTypes(types);
        loongSdkCodeGenerator.generate();
    }
}
