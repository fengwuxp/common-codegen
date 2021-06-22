package ${packagePath?replace('.'+name,'')};
import retrofit2.http.*;


<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
      import ${customizeMethod.pathResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#include "../../commons/api_client_comments.ftl">

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
    <#include "../../commons/api_method_comments.ftl">
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
    ${customizeMethod.combineType(method.returnTypes)}  ${method.name} (
    <#list params as paramName,paramType>
        <#assign paramAnnotation= paramAnnotations[paramName]/>
        <#if (paramAnnotation?size>0)>
            <#assign annotation= paramAnnotation[0]/>
            <#assign len=annotation.namedArguments?size />
            <#assign currentIndex=0 />
          @${annotation.name}<#if annotation.namedArguments??>(<#list annotation.namedArguments as name,val>${name} = ${val!""} <#if currentIndex<len-1>,</#if><#assign currentIndex=currentIndex+1 /></#list>)</#if></#if>  ${customizeMethod.combineType(paramType.typeVariables)} ${paramName}<#if currentParamIndex<paramLen-1>,</#if>
        <#assign currentParamIndex=currentParamIndex+1 />
    </#list>
  );
</#list>
}
