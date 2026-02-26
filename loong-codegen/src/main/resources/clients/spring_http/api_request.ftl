package ${packagePath?replace('.'+name,'')};

<#if tags?has_content && tags['jNamespace']??>
    <#assign javaNamespace = tags['jNamespace']>
<#else>
    <#assign javaNamespace = 'jakarta'>
</#if>
import lombok.Data;
import lombok.experimental.Accessors;
import ${javaNamespace}.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
<#if annotations??>
    <#list annotations as annotation>
        @${annotation.name}({
        <#list annotation.namedArguments as name,val>
            ${name}=${val}
        </#list>
        })
    </#list>
</#if>
@Data
@Accessors(chain = true)
public class  ${finallyClassName}<#if superClass??> extends ${superClass.finallyClassName}</#if> {

<#if fieldMetas??>
    <#list fieldMetas as field>
        <#if (field.comments?size>0)>
            /**
            <#list field.comments as cmment>
                *${cmment}
            </#list>
            **/
        </#if>
        <#if field.annotations??>
            <#list field.annotations as annotation>
                @${annotation.name}<#if (annotation.namedArguments?size>0)>(
                <#list annotation.namedArguments?keys as name>
                    ${name}=${annotation.namedArguments[name]}<#if name_has_next>,</#if>
                </#list>
                )</#if>
            </#list>
        </#if>
        ${field.accessPermissionName} ${customizeMethod.combineType(field.filedTypes)} ${field.name};

    </#list>
</#if>
}
