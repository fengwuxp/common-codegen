import io.reactivex.Observable;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import ${customize_method.pathoResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#if comments??>
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
    /**
    <#list method.comments as cmment>
        * ${cmment_index+1}:${cmment}
    </#list>
    **/
    <#list method.annotations as annotation>
        <#assign len=annotation.namedArguments?size />
        <#assign currentIndex=0 />
        @${annotation.name}(
        <#list annotation.namedArguments as name,val>
            ${name} = ${val} <#if currentIndex<len-1>,</#if>
            <#assign currentIndex=currentIndex+1 />
        </#list>
        )
    </#list>
    ${customize_method.combineType(method.returnTypes)}  ${method.name} (
    <#--参数遍历-->
    <#assign params=method.params/>
    <#assign paramAnnotations=method.paramAnnotations/>
    <#list params as paramName,paramType>
        <#assign paramAnnotation= paramAnnotations[paramName]/>
        <#if (paramAnnotation?size>0)>
            <#assign annotation= paramAnnotation[0]/>
            <#if annotation.positionArguments??>
                @${annotation.name}(<#list annotation.positionArguments as item>${item}</#list>)
            </#if>
        </#if>  ${customize_method.combineType(paramType.typeVariables)} ${paramName},
    </#list>
    );
</#list>
}
