//@FeignLib
library ${sdkLibName};

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as val >
        <#if !val.packagePath?starts_with("package:")>
            export '${customize_method.pathoResolve(packagePath,val.packagePath)?replace(".","src")}.dart';
        </#if>
    </#list>
</#if>
export 'src/serializers.dart';
