<#if tags.clients??>
<#--clients-->
    <#list tags.clients as val >
        <#if val.methodMetas??>
            <#if tags.functional>
                export {${val.name}} form '${customizeMethod.pathResolve(packagePath?replace(".",""),val.packagePath)}';
            <#else >
                export {<#list val.methodMetas as method>${method.name},</#list>} from '${customizeMethod.pathResolve(packagePath?replace(".",""),val.packagePath)}';
            </#if>
        </#if>
    </#list>
</#if>

<#if tags.models??>
<#--models-->
    <#list tags.models as val >
        export {${val.name}} from '${customizeMethod.pathResolve(packagePath?replace(".",""),val.packagePath)}';
    </#list>
</#if>
