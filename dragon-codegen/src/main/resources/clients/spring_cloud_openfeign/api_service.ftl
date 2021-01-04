import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import ${customize_method.pathoResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#if (comments?size>0)>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>

<#list annotations as annotation>
    <#assign len=annotation.namedArguments?size />
    <#assign currentIndex=0 />
    @${annotation.name}(
    <#list annotation.namedArguments as name,val>
        ${name} = ${val} <#if currentIndex<len-1>,</#if>
        <#assign currentIndex=currentIndex+1 />
    </#list>
    )
</#list>
public interface ${name}{

<#list methodMetas as method>
    <#if (method.comments?size>0)>
    /**
    <#list method.comments as cmment>
        * ${cmment_index+1}:${cmment}
    </#list>
    **/
    </#if>
    <#list method.annotations as annotation>
        <#assign len=annotation.namedArguments?size />
        <#assign currentIndex=0 />
        @${annotation.name}(<#list annotation.namedArguments as name,val>${name} = ${val} <#if (currentIndex<len-1)>,</#if><#assign currentIndex=currentIndex+1 /></#list>)
    </#list>
    <#--参数遍历-->
    <#assign params=method.params/>
    <#assign paramAnnotations=method.paramAnnotations/>
    <#assign paramLen=params?size />
    <#assign currentParamIndex=0 />
    ${customize_method.combineType(method.returnTypes)}  ${method.name} (
    <#list params as paramName,paramType>
        <#assign paramAnnotation= paramAnnotations[paramName]/>
        <#if (paramAnnotation?size>0)>
            <#assign annotation= paramAnnotation[0]/>
            <#assign len=annotation.namedArguments?size />
            <#assign currentIndex=0 />
            @${annotation.name}<#if annotation.namedArguments??>(<#list annotation.namedArguments as name,val>${name} = ${val!""} <#if currentIndex<len-1>,</#if><#assign currentIndex=currentIndex+1 /></#list>)</#if></#if>  ${customize_method.combineType(paramType.typeVariables)} ${paramName}<#if currentParamIndex<paramLen-1>,</#if>
        <#assign currentParamIndex=currentParamIndex+1 />
    </#list>
    );
</#list>
}
