import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';

            import './_order.dart';
            import '../enums/_sex.dart';
            import '../serializers.dart';

    part 'user.g.dart';

        ///  用户信息 用户信息描述，默认值：，示例输入：


abstract class User implements Built<User, UserBuilder>, JsonSerializableObject {

User._();

factory User([Function(UserBuilder) updates]) = _$User;

            ///  id 用户ID，默认值：，示例输入：
            /// 字段在java中的类型为：Long
        @BuiltValueField(wireName: 'id')
        int? get id;
            ///  姓名 用户名称，默认值：，示例输入：
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'name')
        String? get name;
            /// 字段在java中的类型为：Integer
        @BuiltValueField(wireName: 'age')
        int? get age;
            /// 字段在java中的类型为：Order
        @BuiltValueField(wireName: 'order')
        Order? get order;
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Order
        @BuiltValueField(wireName: 'orderList')
        BuiltList<Order>? get orderList;
            /// 字段在java中的类型为：Sex
        @BuiltValueField(wireName: 'sex')
        Sex? get sex;
            /// 字段在java中的类型为：Map
            /// 字段在java中的类型为：String
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'other')
        BuiltMap<String,String>? get other;
            /// 字段在java中的类型为：Map
            /// 字段在java中的类型为：Object
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'other2')
        BuiltMap<Object,Object>? get other2;
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'list')
        BuiltList<Object>? get list;
            /// 字段在java中的类型为：List
            /// 字段在java中的类型为：Object
        @BuiltValueField(wireName: 'list2')
        BuiltList<Object>? get list2;
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'myFriends')
        String? get myFriends;
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'demos')
        BuiltList<BuiltList<String>>? get demos;
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'demos2')
        BuiltList<BuiltList<BuiltList<String>>>? get demos2;
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：Map
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：Sex
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：数组
            /// 字段在java中的类型为：String
        @BuiltValueField(wireName: 'demos3')
        BuiltList<BuiltList<BuiltList<BuiltMap<BuiltList<Sex>,BuiltList<BuiltList<BuiltList<String>>>>>>>? get demos3;
            /// 字段在java中的类型为：Boolean
        @BuiltValueField(wireName: 'boy')
        bool? get boy;

@override
Map<String, dynamic> toMap() {
return serializers.serializeWith(User.serializer, this) as Map<String, dynamic>;
}

@override
String toJson() {
return json.encode(toMap());
}

static Serializer<User> get serializer => _$userSerializer;

static User? formJson(String json) {
return serializers.deserializeWith(User.serializer, jsonDecode(json));
}

}
