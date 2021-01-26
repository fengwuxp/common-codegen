package com.wuxp.codegen.model.constant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

/**
 * @author wuxp
 */
public final class SpringAnnotationClassConstant {

    public static final Class<? extends Annotation>[] SPRING_MAPPING_ANNOTATIONS = new Class[]{
            RequestMapping.class,
            PostMapping.class,
            GetMapping.class,
            DeleteMapping.class,
            PutMapping.class,
            PatchMapping.class,
    };

    public static final Class<? extends Annotation>[] SPRING_CONTROLLER_ANNOTATIONS = new Class[]{
            Controller.class,
            RestController.class
    };
}
