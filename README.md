### Codegen 
  为高效开发而生。 简单快速、准确的生成Api sdk

#### 代码生成转换工具

- [生成例子说明](./docs/brief-description-of-the-code-generation-process.md)
- [接入文档](./docs/doc.md)
- [通过maven-plugin接入](./docs/doc.md#maven-plugin)

#### examples

##### swagger2

- [dart](./examples/swagger-2/src/test/java/test/com/wuxp/codegen/swagger2/Swagger2FeignSdkCodegenDartTest.java)
- [spring cloud openfeign](./examples/swagger-2/src/test/java/test/com/wuxp/codegen/swagger2/Swagger2FeignSdkCodegenFeignClientTest.java)
- [retrofit](./examples/swagger-2/src/test/java/test/com/wuxp/codegen/swagger2/Swagger2FeignSdkCodegenRetrofitTest.java)
- [typescript feign](./examples/swagger-2/src/test/java/test/com/wuxp/codegen/swagger2/Swagger2FeignSdkCodegenTypescriptTest.java)
- [umi request](./examples/swagger-2/src/test/java/test/com/wuxp/codegen/swagger2/Swagger2FeignSdkCodegenUmiRequestTest.java)

##### swagger3

- [dart](./examples/swagger-3/src/test/java/test/com/wuxp/codegen/swagger3/Swagger3FeignSdkCodegenDartTest.java)
- [spring cloud openfeign](./examples/swagger-3/src/test/java/test/com/wuxp/codegen/swagger3/Swagger3FeignSdkCodegenFeignClientTest.java)
- [retrofit](./examples/swagger-3/src/test/java/test/com/wuxp/codegen/swagger3/Swagger3FeignSdkCodegenRetrofitTest.java)
- [typescript feign](./examples/swagger-3/src/test/java/test/com/wuxp/codegen/swagger3/Swagger3FeignSdkCodegenTypescriptTest.java)
- [umi request](./examples/swagger-3/src/test/java/test/com/wuxp/codegen/swagger3/Swagger3FeignSdkCodegenUmiRequestTest.java)

#### 模块说明

```
|--annotation-mate           注解元数据解析
|--annotation-processor      maven注解处理
|--core                      核心模块，定义了顶层的接口
|--docs                      说明文档
|--loong-codegen             loong codegen 通用的代码生成，这里会聚合其他的代码生成器
|--loong-codegen-starter     基于约定和探测，提供更简易的Api
|--examples                  生成例子集合
|-----codegen-result         生成结果目录,不同的语言会生成到不同的目录下
|-----swagger-2              基于swagger-2的生成例子
|--language-annotations      转换不同的请求工具库的注解（装饰器）
|--languages                 不同语言的处理
|--model                     数据模型
|--source-code-support       加载解析java源代码的支持
|--swagger-codegen           基于swagger注解规则的生成
|--template                  模板的通用处理
```

- 期望（目标）

      1：将java代码转化为其他语言的代码

- 实现思路

      1：通过扫描java类（通过注解标记）生成的统一java类描述元数据
      2：将java类的元数据转换为不同于语言（类型）的描述元数据，通过模板输出最终代码

- 期望

  1：可以支持java代码或open Api规范进行生成 (java =>any => any => any)
  2：可以支持自定义的规则

- 新的思路（通过文档生成）

```text
 1：增加通过解析swagger open api的json格式文档，构建统一的描述元数据 
 2：默认提供统一解析，通过提供不同语言的解析器进行自定义的处理 
 3：在模板层面进行抽象，可以实现根据不同的数据分发不同的模板（灵活的模板加载策略）
```

- 通过源码增强

```text
在面对没有swagger注解或者注解不够全面的的项目，通过源码上的注释输出sdk的注释说明增强
```

### maven-plugin

- 通过插件生成sdk
- 支持指定生成的sdk lib(生成 ts、dart、oepnFeign等)
- 支持调用指定的[CodeGenerator](./core/src/main/java/com/wuxp/codegen/core/CodeGenerator.java)的子类进行生成
- 支持调用指定的测试用例生成（用于需要定制化时使用）

### codegen-quick-support 快速接入支持

### codegen-server

- [unibeautify](https://github.com/Unibeautify/unibeautify)
- [awesome-code-formatters](https://github.com/rishirdua/awesome-code-formatters)

```text
    为了更方便使用codegen，提供vcs server模块从代码版本控制平台（git/svn）将代码拉取到本地，自动调用
 mvn插件进行代码生成。这个的前提是，被拉取的代码库需要集成codegen-maven-plugin
    提供client端通过restful接口调用代码生成，在生成完成后下载到本地。
    npm-codegen-cli
    java-codegen-client-plugin
```

- [loong-quick](./loong-quick)
- codegen-server

```text
  1：支持通过代码库名称+分支名称 从git/svn中拉取代码
  2：支持调用mvn命令支持代码库的codegen-maven-plugin
  3：支持代码生成状态轮询和代码生成结果下载接口
  4：提供代码仓库的管理
```

- codegen-client

```text
  1：通过设置codegen-server地址和服务端代码的代码库和分支名称进行sdk生成下载
```
- codegen-server docker support

#### features

- 代码生成插件增加将生成结果上传到codegen-server，codegen-client通过接口下载
- gradle项目的支持
- 支持输出文档（openapi文档或自定义格式的文档）
- 支持通过openApi文档生成