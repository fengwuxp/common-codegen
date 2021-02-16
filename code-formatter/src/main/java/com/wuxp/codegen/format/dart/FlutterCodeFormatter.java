package com.wuxp.codegen.format.dart;

import com.wuxp.codegen.format.AbstractCommandCodeFormatter;

/**
 * 使用 flutter format filepath 格式化代码
 * <p>
 * 1：需要安装flutter环境
 * </p>
 *
 * @author wuxp
 */
public class FlutterCodeFormatter extends AbstractCommandCodeFormatter {


    @Override
    protected String genFormatCommand(String filepath) {
        // TODO flutter 命令行调用过慢
        return genCommand("flutter format", new String[]{filepath}, " ");
    }

    @Override
    protected boolean preCheckEnv() {
        return runCommand("flutter --help");
    }
}
