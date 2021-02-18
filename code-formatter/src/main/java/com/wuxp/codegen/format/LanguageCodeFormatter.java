package com.wuxp.codegen.format;

import com.wuxp.codegen.core.CodeFormatter;
import com.wuxp.codegen.core.TaskWaiter;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.format.dart.FlutterCodeFormatter;
import com.wuxp.codegen.format.java.GoogleCodeFormatter;
import com.wuxp.codegen.format.typecript.PrettierCodeFormatter;
import com.wuxp.codegen.model.LanguageDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * 通过识别语言，调用不同的语言的code formatter
 * 支持不同语言的code format
 *
 * @author wuxp
 */
@Slf4j
public class LanguageCodeFormatter implements CodeFormatter {

    private final Map<LanguageDescription, CodeFormatter> codeFormatters = new ConcurrentReferenceHashMap<>(16);

    private LanguageDescription languageDescription;

    public LanguageCodeFormatter(LanguageDescription languageDescription) {
        this.languageDescription = languageDescription;
    }

    public LanguageCodeFormatter() {
        this(CodegenConfigHolder.getCurrentLanguageDescription());
    }

    @Override
    public void format(String filepath) {
        if (CodegenConfigHolder.isEnabledCodeFormatter()) {
            Optional<CodeFormatter> optional = getFormatter();
            optional.ifPresent(codeFormatter -> codeFormatter.format(filepath));
        }

    }

    @Override
    public String format(String sourcecode, Charset charsetName) {
        if (CodegenConfigHolder.isEnabledCodeFormatter()) {
            Optional<CodeFormatter> optional = getFormatter();
            return optional.map(codeFormatter -> codeFormatter.format(sourcecode, charsetName)).orElse(sourcecode);
        } else {
            return sourcecode;
        }
    }

    private Optional<CodeFormatter> getFormatter() {
        return getFormatter(languageDescription);
    }

    private Optional<CodeFormatter> getFormatter(LanguageDescription languageDescription) {
        switch (languageDescription) {
            case JAVA_ANDROID:
            case JAVA:
                return Optional.of(codeFormatters.computeIfAbsent(languageDescription, key -> this.buildJavaCodeFormatter()));
            case TYPESCRIPT:
                return Optional.of(codeFormatters.computeIfAbsent(languageDescription, key -> this.buildTypescriptCodeFormatter()));
            case DART:
                return Optional.of(codeFormatters.computeIfAbsent(languageDescription, key -> this.buildDartCodeFormatter()));
            default:
        }
        return Optional.empty();
    }

    private CodeFormatter buildJavaCodeFormatter() {
        // TODO load java code style options
        return new GoogleCodeFormatter();
    }

    public CodeFormatter buildTypescriptCodeFormatter() {
        return new PrettierCodeFormatter();
    }

    public CodeFormatter buildDartCodeFormatter() {
        return new FlutterCodeFormatter();
    }

    public void setLanguageDescription(LanguageDescription languageDescription) {
        this.languageDescription = languageDescription;
    }

    @Override
    public void waitTaskCompleted() {
        if (CodegenConfigHolder.isEnabledCodeFormatter()) {
            Optional<CodeFormatter> optional = getFormatter();
            optional.ifPresent(TaskWaiter::waitTaskCompleted);
        }
    }
}
