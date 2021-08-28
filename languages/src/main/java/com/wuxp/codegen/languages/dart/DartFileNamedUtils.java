package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.meta.util.JavaMethodNameUtils;
import com.wuxp.codegen.model.LanguageDescription;

import java.text.MessageFormat;

public final class DartFileNamedUtils {

    private static final String RIGHT_SLASH = "/";

    private DartFileNamedUtils() {
        throw new AssertionError();
    }

    public static String dartFileNameConverter(String filepath) {

        if (filepath.endsWith(MessageFormat.format(".{0}", LanguageDescription.DART.getSuffixName()))) {
            return filepath;
        }

        String[] split = filepath.split(RIGHT_SLASH);
        String s = split[split.length - 1];
        split[split.length - 1] = JavaMethodNameUtils.humpToLine(s);
        return String.join(RIGHT_SLASH, split);
    }
}
