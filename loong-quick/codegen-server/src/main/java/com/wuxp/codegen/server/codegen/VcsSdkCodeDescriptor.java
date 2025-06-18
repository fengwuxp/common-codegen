package com.wuxp.codegen.server.codegen;

import com.wuxp.codegen.core.ClientProviderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.NotNull;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_GIT_BRANCH_NAME;
import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_MODULE_NAME;

@AllArgsConstructor
@Getter
public class VcsSdkCodeDescriptor implements SdkCodeDescriptor {

    private static final long serialVersionUID = -2178935124312947924L;

    @NotNull
    private final String projectName;

    @NotNull
    private final ClientProviderType type;

    private final String branch;

    private final String moduleName;

    @Override
    public String getModuleName() {
        if (StringUtils.hasText(moduleName)) {
            return DEFAULT_MODULE_NAME;
        }
        return moduleName;
    }

    @Override
    public String getBranch() {
        if (StringUtils.hasText(branch)) {
            return DEFAULT_GIT_BRANCH_NAME;
        }
        return branch;
    }
}
