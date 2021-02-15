<#--api client-->
<#if (comments?size>0)>
    /**
    <#list comments as cmment>
        <#if cmment??>
     * ${cmment}
        </#if>
    </#list>
    **/
</#if>