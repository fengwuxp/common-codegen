import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './base_example.dart';

    part 'base_info.g.dart';



abstract class BaseInfo<I,T> implements Built<BaseInfo<I,T>, BaseInfoBuilder<I,T>>, JsonSerializableObject {

BaseInfo._();

factory BaseInfo([Function(BaseInfoBuilder<I,T>) updates]) = _$BaseInfo<I,T>;

        @BuiltValueField(wireName: 'id')
        I? get id;
        @BuiltValueField(wireName: 'data')
        T? get data;
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
