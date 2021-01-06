#### 代码生成转换工具

- [生成例子说明](./docs/brief-description-of-the-code-generation-process.md)
- [接入文档](./docs/doc.md)

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
|--annotation-processor      注解处理(和编译器的注解处理器不同，应该叫注解解析更合适)
|--core                      核心模块，定义了顶层的接口
|--docs                      说明文档
|--dragon-codegen            dragon codegen 通用的代码生成，这里会聚合其他的代码生成器
|--examples                  生成例子集合
|-----codegen-result         生成结果目录,不同的语言会生成到不同的目录下
|-----swagger-2              基于swagger-2的生成例子
|--language-annotations      转换不同的请求工具库的注解（装饰器）
|--languages                 不同语言的处理
|--model                     数据模型
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

- 新的思路（通过文档生成） 1：增加通过解析swagger open api的json格式文档，构建统一的描述元数据 2：默认提供统一解析，通过提供不同语言的解析器进行自定义的处理 3：在模板层面进行抽象，可以实现根据不同的数据分发不同的模板（灵活的模板加载策略）
  