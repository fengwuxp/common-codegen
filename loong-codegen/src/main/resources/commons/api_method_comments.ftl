<#--方法注释-->
<#if (method.comments?size>0)>
    /**
    <#list method.comments as cmment>
    <#if cmment??>
      * <#if !(cmment?starts_with("@")||cmment?starts_with("<"))>${cmment_index+1}:</#if>${cmment}
    </#if>
    </#list>
     **/
</#if>