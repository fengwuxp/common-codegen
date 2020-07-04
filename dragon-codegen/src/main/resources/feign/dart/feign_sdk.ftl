//@FeignLib
library ${sdkLibName};

import 'package:reflectable/reflectable.dart';

<#--import  'feign_sdk.reflectable.dart';-->
import './${sdkLibName?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.reflectable.dart';
<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as val >
        <#if !val.packagePath?starts_with("package:")>
            import '${customize_method.pathoResolve(packagePath,val.packagePath)?replace(".","src")}.dart';
        </#if>
    </#list>
</#if>

//class DartFeignClientLib extends Reflectable {
//  const DartFeignClientLib()
//      : super(
//    libraryCapability,
//    declarationsCapability,
////    instanceInvokeCapability,
//    libraryCapability,
//  );
//}
//
//const FeignLib = DartFeignClientLib();

void main(){
//  initializeReflectable();
}
