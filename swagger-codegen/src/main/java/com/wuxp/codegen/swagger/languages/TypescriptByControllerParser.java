package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.extern.slf4j.Slf4j;


/**
 * 解析控制器
 */
@Slf4j
public class TypescriptByControllerParser extends TypescriptParser {


    public TypescriptByControllerParser(PackageMapStrategy packageMapStrategy) {
        super(packageMapStrategy);
    }

    @Override
    public CommonCodeGenClassMeta parse(JavaClassMeta source) {

        CommonCodeGenClassMeta meta=new CommonCodeGenClassMeta();

        meta.setName(source.getName());
        meta.setPackagePath(this.packageMapStrategy.convert(source.getClazz()));
        meta.setClassType(ClassType.CLASS);
        meta.setIsAbstract(false);
        meta.setAccessPermission(AccessPermission.PUBLIC);
//        meta.setComments(source.findAnnotation());


        return meta;
    }
}
