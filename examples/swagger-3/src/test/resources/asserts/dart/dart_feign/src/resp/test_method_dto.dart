import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';


    part 'test_method_dto.g.dart';



abstract class TestMethodDTO implements Built<TestMethodDTO, TestMethodDTOBuilder>, JsonSerializableObject {

TestMethodDTO._();

factory TestMethodDTO([Function(TestMethodDTOBuilder) updates]) = _$TestMethodDTO;

            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'name')
        String? get name;
            /// 字段在java中的类型为：Short
        @BuiltValueField(wireName: 'age')
        int? get age;
            /// 字段在java中的类型为：Boolean
        @BuiltValueField(wireName: 'flag')
        bool? get flag;
            /// 字段在java中的类型为：Date
        @BuiltValueField(wireName: 'birthDay')
        DateTime? get birthDay;
            /// 字段在java中的类型为：TestMethodDTO
        @BuiltValueField(wireName: 'example')
        TestMethodDTO? get example;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(TestMethodDTO.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<TestMethodDTO> get serializer => _$testMethodDTOSerializer;

static TestMethodDTO? formJson(String json) {
return serializers.deserializeWith(TestMethodDTO.serializer, jsonDecode(json));
}

}
