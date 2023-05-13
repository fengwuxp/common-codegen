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
    <#list enumConstants as field>
      <#if (field.comments?size>0)>
        /**
        <#list field.comments as cmment>
          *${cmment}
        </#list>
        **/
      </#if>
      public static readonly ${field.name}:Enum={
      name:"${field.name}",
      ordinal:${field_index},
       <#if (field.comments?size>0)>
       desc: "${field.comments[0]}"
      <#else >
       desc: "${field.name}"
      </#if>
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
    <#list enumConstants as field>
      <#if (field.comments?size>0)>
        /**
        <#list field.comments as cmment>
          *${(cmment)!}
        </#list>
        **/
      </#if>
        ${field.name}='${field.name}'<#if field_has_next>,</#if>
    </#list>
  }
</#if>
