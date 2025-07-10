import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';


    part 'base_evt.g.dart';



abstract class BaseEvt implements Built<BaseEvt, BaseEvtBuilder>, JsonSerializableObject {

BaseEvt._();

factory BaseEvt([Function(BaseEvtBuilder) updates]) = _$BaseEvt;


@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(BaseEvt.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<BaseEvt> get serializer => _$baseEvtSerializer;

static BaseEvt? formJson(String json) {
return serializers.deserializeWith(BaseEvt.serializer, jsonDecode(json));
}

}
