<#if !finallyClassName?ends_with("Query")>
    <#include "../spring_cloud_openfeign/api_request.ftl"/>
<#else >
package ${packagePath?replace('.'+name,'')};

<#if queryObjectShareDependencies??>
    <#list queryObjectShareDependencies as className>
        import ${className};
    </#list>
</#if>
<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import ${customizeMethod.pathResolve(packagePath,val.packagePath)};
    </#list>
</#if>

<#if (comments?size>0)>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>
public class ${finallyClassName}<#if queryObjectShareClassName??> extends ${queryObjectShareClassName}</#if> {

<#if fieldMetas??>
    <#list fieldMetas as field>
        <#if (field.comments?size>0)>
            /**
            <#list field.comments as cmment>
                *${cmment}
            </#list>
            **/
        </#if>
        public ${customizeMethod.combineType(field.filedTypes)} get${field.name?cap_first}(){
          return (${customizeMethod.combineType(field.filedTypes)})get("${field.name}");
        }

        public ${finallyClassName} set${field.name?cap_first}(${customizeMethod.combineType(field.filedTypes)} ${field.name}){
             put("${field.name}",${field.name});
            return this;
        }

    </#list>
</#if>
}
</#if>