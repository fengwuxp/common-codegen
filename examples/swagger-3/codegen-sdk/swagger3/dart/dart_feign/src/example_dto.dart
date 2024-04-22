import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './serializers.dart';

    part 'example_dto.g.dart';



abstract class ExampleDto implements Built<ExampleDto, ExampleDtoBuilder>, JsonSerializableObject {

ExampleDto._();

factory ExampleDto([Function(ExampleDtoBuilder) updates]) = _$ExampleDto;

            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'querySize')
        int? get querySize;
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'queryPage')
        int? get queryPage;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(ExampleDto.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<ExampleDto> get serializer => _$exampleDtoSerializer;

static ExampleDto? formJson(String json) {
return serializers.deserializeWith(ExampleDto.serializer, jsonDecode(json));
}

}
