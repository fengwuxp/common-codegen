package ${packagePath?replace('.'+name,'')};
<#if (comments?size>0)>
  /**
    <#list comments as cmment>
   * ${cmment}
    </#list>
   **/
</#if>
public enum  ${name}{

<#assign enumFieldHasLength=(fieldMetas?size>0)/>
<#assign enumConstantsHasLength=(enumConstants?size>0)/>
<#if enumConstantsHasLength>
<#list enumConstants as enumConstant>
    ${enumConstant.name}<#if (enumConstant.enumFiledValues?size>0)>(<#list enumConstant.enumFiledValues as enumValue>${enumValue}<#if enumValue_has_next>,</#if></#list>)</#if><#if  enumConstant_has_next>,<#else>;</#if>
</#list>
</#if>

<#if enumConstantsHasLength>
    <#list fieldMetas as field>
        private String ${field.name};
    </#list>
</#if>

<#if enumFieldHasLength>
${name}(<#list fieldMetas as field>${field.filedTypes[0].name} ${field.name}<#if field_has_next>,</#if></#list>) {
  <#list fieldMetas as field>
   this.${field.name} = ${field.name};
  </#list>
}
</#if>

<#if enumFieldHasLength>
<#list fieldMetas as field>
  public String get${field.name?cap_first}() {
      return ${field.name};
  }
</#list>
</#if>


}
