package com.wuxp.codegen.scanner.scanner;


/**
 * 扫描者
 */
public interface Scanner<T,P> {


    T scan(P p);
}
