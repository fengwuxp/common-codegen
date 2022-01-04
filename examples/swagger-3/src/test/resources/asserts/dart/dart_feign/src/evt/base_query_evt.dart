import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './base_evt.dart';
            import '../serializers.dart';

    part 'base_query_evt.g.dart';



abstract class BaseQueryEvt implements Built<BaseQueryEvt, BaseQueryEvtBuilder>, JsonSerializableObject {

BaseQueryEvt._();

factory BaseQueryEvt([Function(BaseQueryEvtBuilder) updates]) = _$BaseQueryEvt;

            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'querySize')
        int? get querySize;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(BaseQueryEvt.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<BaseQueryEvt> get serializer => _$baseQueryEvtSerializer;

static BaseQueryEvt? formJson(String json) {
return serializers.deserializeWith(BaseQueryEvt.serializer, jsonDecode(json));
}

}
