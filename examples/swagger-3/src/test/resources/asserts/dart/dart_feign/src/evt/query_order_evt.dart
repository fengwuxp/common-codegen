import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './base_query_evt.dart';

    part 'query_order_evt.g.dart';



abstract class QueryOrderEvt implements Built<QueryOrderEvt, QueryOrderEvtBuilder>, JsonSerializableObject {

QueryOrderEvt._();

factory QueryOrderEvt([Function(QueryOrderEvtBuilder) updates]) = _$QueryOrderEvt;

            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：int
        @BuiltValueField(wireName: 'ids')
        BuiltList<num>? get ids;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(QueryOrderEvt.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<QueryOrderEvt> get serializer => _$queryOrderEvtSerializer;

static QueryOrderEvt? formJson(String json) {
return serializers.deserializeWith(QueryOrderEvt.serializer, jsonDecode(json));
}

}
