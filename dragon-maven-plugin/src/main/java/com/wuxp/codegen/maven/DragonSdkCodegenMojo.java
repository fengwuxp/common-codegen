package com.wuxp.codegen.maven;

import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.Collection;

/**
 * @author wuxp
 */
@Mojo(name = "dragon-sdk-codegen", defaultPhase = LifecyclePhase.COMPILE)
public class DragonSdkCodegenMojo extends AbstractSdkCodegenMojo {

    private DragonSdkCodeGenerator dragonSdkCodeGenerator;

    @Override
    protected Collection<CodegenBuilder> getCodeGeneratorBuilders() {
        if (dragonSdkCodeGenerator == null) {
            dragonSdkCodeGenerator = new DragonSdkCodeGenerator();
        }
        return dragonSdkCodeGenerator.getCodeGeneratorBuilders();
    }
}
