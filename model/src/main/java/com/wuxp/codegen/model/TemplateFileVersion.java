package com.wuxp.codegen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wxup
 */
@Getter
@AllArgsConstructor
public enum TemplateFileVersion {

    /**
     * default
     */
    DEFAULT(""),

    /**
     * 1.0.0 version
     */
    V_1_0_0("1.0.0"),

    /**
     * 2.0.0 version
     */
    V_2_0_0("2.0.0");


    private String version;


}
