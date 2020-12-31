import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

<#if dependencies??>
<#--依赖导入处理-->
    <#list dependencies as key,val >
        <#if !val.packagePath?starts_with("package:")>
            import '${customize_method.pathoResolve(packagePath,val.packagePath)}.dart';
        </#if>
    </#list>
</#if>

<#assign genericIndex=finallyClassName?index_of("<")/>
<#assign className=finallyClassName/>

<#assign serializerName=finallyClassName/>
<#--泛型描述-->
<#assign genericDesc=""/>
<#if (genericIndex>0)>
    <#assign serializerName=className?substring(0,genericIndex)/>
    <#assign genericDesc=className?substring(genericIndex)/>
</#if>
<#if (genericIndex>0)>
    part '${serializerName?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.g.dart';
<#else >
    part '${finallyClassName?replace("([a-z])([A-Z]+)","$1_$2","r")?lower_case}.g.dart';
</#if>

<#if (comments?size>0)>
    <#list comments as cmment>
     /// ${cmment}
    </#list>
</#if>




abstract class ${className} implements Built<${className}, ${serializerName}Builder${genericDesc}>, JsonSerializableObject {

       ${serializerName}._();

      factory ${serializerName}([Function(${serializerName}Builder${genericDesc}) updates]) = _${"$"}${className};

       <#if fieldMetas??>
             <#list fieldMetas as field>
                <#list field.comments as cmment>
                    /// ${cmment}
                </#list>
    <#--            <#list field.annotations as annotation>-->
    <#--                @${annotation.name}({-->
    <#--                <#list annotation.namedArguments as name,val>-->
    <#--                    ${name}:${val},-->
    <#--                </#list>-->
    <#--                })-->
    <#--            </#list>-->
                <#if !field.requered??>
                @nullable
                </#if>
                @BuiltValueField(wireName: '${field.name}')
                ${customize_method.combineType(field.filedTypes)} get ${field.name};
            </#list>
       </#if>

        @override
        Map<String, dynamic> toMap() {
            return serializers.serializeWith(${serializerName}.serializer, this);
        }

        @override
        String toJson() {
           return json.encode(toMap());
        }

        static Serializer<${serializerName}> get serializer => _${"$"}${serializerName?uncap_first}Serializer;

        static ${serializerName} formJson(String json) {
             return serializers.deserializeWith(${serializerName}.serializer, jsonDecode(json));
        }

}
