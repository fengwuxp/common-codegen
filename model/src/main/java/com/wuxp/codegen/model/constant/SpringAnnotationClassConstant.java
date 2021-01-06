package com.wuxp.codegen.model.constant;

import java.lang.annotation.Annotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
