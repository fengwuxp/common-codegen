package com.wuxp.codegen.swagger2.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.collect.Iterables;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;
import springfox.documentation.spring.web.ObjectMapperConfigurer;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;

public class MyObjectMapperConfigurer extends ObjectMapperConfigurer {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HttpMessageConverter<?>> messageConverters = adapter.getMessageConverters();
            adapter.setMessageConverters(configureMessageConverters(messageConverters));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }

    private List<HttpMessageConverter<?>> configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterable<MappingJackson2HttpMessageConverter> jackson2Converters = jackson2Converters(converters);
        int size = Iterables.size(jackson2Converters);
        if (size > 0) {
            for (MappingJackson2HttpMessageConverter each : jackson2Converters) {
                ObjectMapper objectMapper = each.getObjectMapper();
//                objectMapper.getSerializationConfig()
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
                fireObjectMapperConfiguredEvent(objectMapper);
//                fireObjectMapperConfiguredEvent(this.objectMapper());
            }
        } else {
            converters.add(configuredMessageConverter());
        }
        return newArrayList(converters);
    }

    private Iterable<MappingJackson2HttpMessageConverter> jackson2Converters
            (List<HttpMessageConverter<?>> messageConverters) {
        return from(messageConverters).filter(MappingJackson2HttpMessageConverter.class);
    }

    private MappingJackson2HttpMessageConverter configuredMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = this.objectMapper();
        messageConverter.setObjectMapper(objectMapper);
        fireObjectMapperConfiguredEvent(objectMapper);
        return messageConverter;
    }

    private void fireObjectMapperConfiguredEvent(ObjectMapper objectMapper) {
        applicationEventPublisher.publishEvent(new ObjectMapperConfigured(this, objectMapper));
    }
}
