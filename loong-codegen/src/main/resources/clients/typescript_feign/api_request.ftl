/* tslint:disable */
/* eslint-disable */

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        import {${key}} from "${customizeMethod.pathResolve(packagePath,val.packagePath)}";
    </#list>
</#if>
import {DefaultOrderField} from "feign-client";

<#if (comments?size>0)>
    /**
    <#list comments as cmment>
        * ${cmment}
    </#list>
    **/
</#if>


export interface  ${finallyClassName}<#if superClass??> extends ${superClass.finallyClassName}</#if> {

<#if fieldMetas??>
    <#list fieldMetas as field>
        <#if (field.comments?size>0)>
            /**
            <#list field.comments as cmment>
                *${cmment}
            </#list>
            **/
        </#if>
        ${field.name}<#if field.required?string('true', 'false')=='false'>?</#if>: ${customizeMethod.combineType(field.filedTypes)};
    </#list>
</#if>
}