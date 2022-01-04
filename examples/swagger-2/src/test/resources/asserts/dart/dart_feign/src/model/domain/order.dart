import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './user.dart';
            import '../../serializers.dart';
            import './base_info.dart';

    part 'order.g.dart';

        /// 订单


abstract class Order implements Built<Order, OrderBuilder>, JsonSerializableObject {

Order._();

factory Order([Function(OrderBuilder) updates]) = _$Order;

            /// 属性说明：sn，示例输入：order_sn_199223
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'sn')
        String? get sn;
            /// 属性说明：下单用户，示例输入：
            /// 字段在java中的类型为：User
        @BuiltValueField(wireName: 'user')
        User? get user;
            /// 字段在java中的类型为：Date
        @BuiltValueField(wireName: 'addTime')
        DateTime? get addTime;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(Order.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<Order> get serializer => _$orderSerializer;

static Order? formJson(String json) {
return serializers.deserializeWith(Order.serializer, jsonDecode(json));
}

}
