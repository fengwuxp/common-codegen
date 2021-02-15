package com.wuxp.codegen.format.typecript;

import com.wuxp.codegen.format.AbstractCommandCodeFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用 https://github.com/prettier/prettier 格式化代码
 * <p>
 * 1：必须要有nodejs环境
 * 2：需要安装prettier
 * </p>
 *
 * @author wuxp
 */
@Slf4j
public class PrettierCodeFormatter extends AbstractCommandCodeFormatter {

    /**
     * 用于格式化代码的脚步地址
     */
    private static final String FORMAT_SHELL_FILEPATH = PrettierCodeFormatter.class.getResource("/prettier-formatter.js").getFile();

    @Override
    protected String genFormatCommand(String filepath) {
        return genCommand("node", new String[]{FORMAT_SHELL_FILEPATH, filepath, "UTF-8"}, " ");
    }

    @Override
    protected boolean preCheckEnv() {
        // check nodejs env
        if (!runCommand("node --version")) {
            log.warn("not support node env");
            return false;
        }

        // check prettier
        if (!runCommand("prettier --version")) {
            // check prettier
            log.warn("prettier not installed,please install");
            return false;
        }
        return true;
    }
}
