/* tslint:disable */
<#if enumImportPath??>
import {Enum} from "${enumImportPath}";

<#if (comments?size>0)>
    /**
    <#list comments as cmment>
     * ${cmment}
    </#list>
     **/
</#if>
export class ${name}{

constructor() {}

<#list fieldMetas as field>
    public static readonly ${field.name}:Enum={
    name:"${field.name}",
    ordinal:${field_index},
    desc: "${field.comments[0]}"
    };
</#list>
}
<#else >
 <#if (comments?size>0)>
  /**
  <#list comments as cmment>
   * ${cmment}
 </#list>
  **/
 </#if>
export enum ${name}{
<#assign len=fieldMetas?size/>
 <#list fieldMetas as field>
     ${field.name}='${field.name}'<#if field_index<len-1>,</#if>
 </#list>
}
</#if>
