package com.wuxp.codegen.server.codegen;

import com.wuxp.codegen.server.task.CodegenTaskException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 代码生成的文件管理者
 *
 * @author wuxp
 */
public interface SdkCodeManager {
    
    /**
     * 上传生成的sdk文件
     *
     * @param descriptor sdk code 描述
     * @param file       上传的sdk代码文件liu
     */
    default void storage(SdkCodeDescriptor descriptor, File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw new CodegenTaskException(exception);
        }
        storage(descriptor, inputStream);
    }

    void storage(SdkCodeDescriptor descriptor, InputStream inputStream);

    /**
     * 下载生成好的sdk文件
     *
     * @param descriptor sdk code 描述
     * @return sdk代码文件
     */
    File get(SdkCodeDescriptor descriptor);
}
