package com.wuxp.codegen.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * code formatter
 * 对生成的代码做格式化
 *
 * @author wuxp
 */
public interface CodeFormatter extends TaskWaiter {

    /**
     * 格式化代码
     *
     * @param filepath 代码文件目录
     */
    default void format(String filepath) {
        Charset charset = StandardCharsets.UTF_8;
        try {
            File sourceFile = new File(filepath);
            if (!sourceFile.exists() || !sourceFile.canRead()) {
                return;
            }
            String sourcecode = FileUtils.readFileToString(sourceFile, charset);
            String result = format(sourcecode, charset);
            FileUtils.write(sourceFile, result, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化代码
     *
     * @param sourcecode  源代码
     * @param charsetName 字符集
     * @return 格式化成功后的代码
     */
    String format(String sourcecode, final Charset charsetName);

    @Override
    default void waitTaskCompleted() {
        // 默认不需要等待
    }
}
