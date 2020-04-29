import 'dart:convert';

<#--import 'package:built_collection/built_collection.dart';-->
<#--import 'package:built_value/built_value.dart';-->
<#--import 'package:built_value/serializer.dart';-->
import 'package:fengwuxp_dart_basic/index.dart';

part '${finallyClassName?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.g.dart';

<#if dependencies??>
<#--依赖导入处理-->
     <#list dependencies as key,val >
         <#if !val.packagePath?starts_with("package:")>
          import '${customize_method.pathoResolve(packagePath,val.packagePath)}';
       </#if>
    </#list>
</#if>

<#if comments??>
    <#list comments as cmment>
     /// ${cmment}
    </#list>
</#if>

abstract class ${finallyClassName} implements Built<${finallyClassName}, ${finallyClassName}Builder>, JsonSerializableObject {

       ${finallyClassName}._();

      factory ${finallyClassName}([Function(${finallyClassName}Builder) updates]) = _${"$"}${finallyClassName};


       <#if fieldMetas??>
            <#list fieldMetas as field>

         <#list field.comments as cmment>
           /// ${cmment}
         </#list>
         <#if field.requered??>
          @nullable
         </#if>
         <#list field.annotations as annotation>
             @${annotation.name}({
             <#list annotation.namedArguments as name,val>
                 ${name}:${val},
             </#list>
             })
         </#list>
        ${customize_method.combineType(field.filedTypes)} ${field.name};
    </#list>
       </#if>

       static Serializer<${finallyClassName}> get serializer => _${"$"}${finallyClassName?uncap_first}Serializer;

        static ${finallyClassName} formJson(String json) {
           return serializers.deserializeWith(${finallyClassName}.serializer, jsonDecode(json));
        }

        @override
        Map<String, dynamic> toMap() {
            return serializers.serializeWith(${finallyClassName}.serializer, this);
        }

        @override
        String toJson() {
           return json.encode(toMap());
        }

}
