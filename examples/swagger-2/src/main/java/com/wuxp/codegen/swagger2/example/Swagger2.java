package com.wuxp.codegen.swagger2.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


@Configuration
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wuxp.codegen.swagger2.example"))
                .paths(PathSelectors.any())
                .build();
    }


//    @Bean
////    @Primary()
//    public static ObjectMapperConfigurer objectMapperConfigurer() {
//        return new MyObjectMapperConfigurer();
//    }

    /**
     * 统一输出风格
     * See {@link com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy} for details.
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter<?> httpMessageConverter = converters.get(i);
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                // 统一返回数据的输出风格
                MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                ObjectMapper objectMapper = converter.getObjectMapper();
                //排除null
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //设置下划线转换
                objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
            }
        }
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("codegen example")
                .description("通用的java to any api sdk 模块")
                .contact("fengwuxp")
                .version("0.0.1")
                .build();
    }

}
