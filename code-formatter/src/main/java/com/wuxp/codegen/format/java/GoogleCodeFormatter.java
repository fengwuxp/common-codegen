package com.wuxp.codegen.format.java;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.wuxp.codegen.core.CodeFormatter;
import lombok.extern.slf4j.Slf4j;
//import com.intellij.psi.codeStyle.CodeStyleManager;
import java.nio.charset.Charset;

/**
 * 使用google格式化java代码
 *
 * @author wuxp
 */
@Slf4j
public class GoogleCodeFormatter implements CodeFormatter {

    private final JavaFormatterOptions formatterOptions;

    public GoogleCodeFormatter() {
        this(JavaFormatterOptions.defaultOptions());
    }

    public GoogleCodeFormatter(JavaFormatterOptions formatterOptions) {
        this.formatterOptions = formatterOptions;
    }

    @Override
    public String format(String sourcecode, Charset charsetName) {

        String formattedSource = null;
        try {
            formattedSource = new Formatter(formatterOptions).formatSource(sourcecode);
        } catch (FormatterException e) {
            log.warn("格式化代码失败", e);
            return sourcecode;
        }
        return formattedSource;
    }
}
