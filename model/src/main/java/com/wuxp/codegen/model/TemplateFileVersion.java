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
    DEFAULT("");


    private final String version;


}
