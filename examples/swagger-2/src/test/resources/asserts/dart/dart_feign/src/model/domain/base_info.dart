import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import '../../enums/domain/base_example.dart';
            import '../../serializers.dart';

    part 'base_info.g.dart';



abstract class BaseInfo<ID> implements Built<BaseInfo<ID>, BaseInfoBuilder<ID>>, JsonSerializableObject {

BaseInfo._();

factory BaseInfo([Function(BaseInfoBuilder<ID>) updates]) = _$BaseInfo<ID>;

            /// 属性说明：id 我的 \n 你的  他的 \r 不是的，示例输入：
        @BuiltValueField(wireName: 'id')
        ID? get id;
            /// 字段在java中的类型为：BaseExample
        @BuiltValueField(wireName: 'example')
        BaseExample? get example;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(BaseInfo.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<BaseInfo> get serializer => _$baseInfoSerializer;

static BaseInfo? formJson(String json) {
return serializers.deserializeWith(BaseInfo.serializer, jsonDecode(json));
}

}
