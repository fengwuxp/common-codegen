import 'dart:io';
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:fengwuxp_dart_basic/index.dart';
import 'package:fengwuxp_dart_openfeign/index.dart';



      /// 接口：GET
@Feign
  @FeignClient(value:"/file",)
class FileService extends FeignProxyClient {

FileService() : super() {

}


      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：HttpEntity
      /// 3:返回值在java中的类型为：InputStreamResource
          @GetMapping(value:"/download",consumes:[HttpMediaType.APPLICATION_STREAM],)
  Future<File>  download(
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<File>("download",
  [name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: File,
          )
    
  );
  }

      /// 1:Http请求方法：GET
      /// 2:返回值在java中的类型为：void
          @GetMapping(value:"/download_2",consumes:[HttpMediaType.APPLICATION_STREAM],)
  Future<File>  download2(
        String name,
  [UIOptions? feignOptions]) {
  return this.delegateInvoke<File>("download2",
  [name,],
          feignOptions: feignOptions,
          serializer: BuiltValueSerializable(
                serializeType: File,
          )
    
  );
  }
}


final fileService = FileService();
