//@FeignLib

import 'package:reflectable/reflectable.dart';
import './${tags.sdkLibName?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.reflectable.dart';
<#if tags.dependencies??>
<#--依赖导入处理-->
    <#list tags.dependencies as val >
        <#if !val.packagePath?starts_with("package:")>
          import '${customizeMethod.pathResolve(packagePath?replace("..",""),"/src"+val.packagePath)}.dart';
        </#if>
    </#list>
</#if>

void main(){
//  initializeReflectable();
}
