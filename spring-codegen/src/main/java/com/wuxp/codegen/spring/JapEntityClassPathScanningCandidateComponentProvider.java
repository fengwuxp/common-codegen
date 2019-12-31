package com.wuxp.codegen.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.persistence.Entity;

/**
 * 扫描被entity注解的类
 */
@Slf4j
public class JapEntityClassPathScanningCandidateComponentProvider extends ClassPathScanningCandidateComponentProvider {

    public JapEntityClassPathScanningCandidateComponentProvider() {
    }

    public JapEntityClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
        super(useDefaultFilters);
    }

    public JapEntityClassPathScanningCandidateComponentProvider(boolean useDefaultFilters, Environment environment) {
        super(useDefaultFilters, environment);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.hasAnnotation(Entity.class.getName());
    }
}
