//@FeignLib
library ${tags.sdkLibName};

<#if tags.dependencies??>
<#--依赖导入处理-->
    <#list tags.dependencies as val >
        <#if !val.packagePath?starts_with("package:")>
          export '${customizeMethod.pathResolve(packagePath,val.packagePath)?replace(".","src")}.dart';
        </#if>
    </#list>
</#if>
export 'src/serializers.dart';
