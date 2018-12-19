package com.wuxp.codegen.scanner;

import com.wuxp.codegen.scanner.filter.ScannerFilter;
import com.wuxp.codegen.scanner.scanner.ClassPathScanner;
import com.wuxp.codegen.scanner.scanner.Scanner;
import lombok.extern.slf4j.Slf4j;


/**
 * 静态的类扫描工具
 */
@Slf4j
public final class StaticClassPathScanner {

    private static final Scanner<ScannerFilter, String[]> classPathScanner = new ClassPathScanner();


    public static ScannerFilter scan(String... packageNames) {

        return classPathScanner.scan(packageNames);
    }
}
