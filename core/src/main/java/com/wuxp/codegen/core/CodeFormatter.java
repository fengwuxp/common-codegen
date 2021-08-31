package com.wuxp.codegen.core;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * code formatter
 * 对生成的代码做格式化
 *
 * @author wuxp
 */
public interface CodeFormatter extends CodeGenerateAsyncTaskFuture {

    default CompletableFuture<Void> asyncFormat(String filepath) {
        return CompletableFuture.runAsync(() -> format(filepath));
    }

    /**
     * 格式化代码
     *
     * @param filepath 代码文件目录
     */
    default void format(String filepath) {
        Charset charset = StandardCharsets.UTF_8;
        try {
            File sourceFile = new File(filepath);
            Assert.isTrue(sourceFile.exists(), String.format("filepath = %s 的文件不存在", filepath));
            Assert.isTrue(sourceFile.canRead(), String.format("filepath = %s 的文件不可读", filepath));
            String sourcecode = FileUtils.readFileToString(sourceFile, charset);
            FileUtils.write(sourceFile, format(sourcecode, charset), charset);
        } catch (Exception exception) {
            throw new CodegenRuntimeException(exception);
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
}
