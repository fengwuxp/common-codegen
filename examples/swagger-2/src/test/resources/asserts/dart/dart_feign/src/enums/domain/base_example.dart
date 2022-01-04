import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';


part 'base_example.g.dart';

class BaseExample extends EnumClass  {


static Serializer<BaseExample> get serializer => _$baseExampleSerializer;
/// 字段在java中的类型为：BaseExample
static const BaseExample A = _$A;


const BaseExample._(String name):super(name);

static BuiltSet<BaseExample> get values => _$values;

static BaseExample valueOf(String name) => _$valueOf(name);
}
