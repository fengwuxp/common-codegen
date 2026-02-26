package ${packagePath?replace('.'+name,'')};

import lombok.AllArgsConstructor;
import lombok.Getter;
<#assign enumFieldHasLength=(fieldMetas?size>0)/>
<#assign enumConstantsHasLength=(enumConstants?size>0)/>
<#if (comments?size>0)>
  /**
    <#list comments as cmment>
   * ${cmment}
    </#list>
   **/
</#if>
<#if enumFieldHasLength>
@AllArgsConstructor
@Getter
</#if>
public enum  ${name}{

<#if enumConstantsHasLength>
<#list enumConstants as enumConstant>
    ${enumConstant.name}<#if (enumConstant.enumFiledValues?size>0)>(<#list enumConstant.enumFiledValues as enumValue>${enumValue}<#if enumValue_has_next>,</#if></#list>)</#if><#if  enumConstant_has_next>,<#else>;</#if>
</#list>
</#if>

<#if enumConstantsHasLength>
    <#list fieldMetas as field>
        ${field.accessPermissionName} final ${customizeMethod.combineType(field.filedTypes)} ${field.name};
    </#list>
</#if>


}
