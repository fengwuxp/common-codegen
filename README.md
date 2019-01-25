

#### 代码生成转换工具
[生成例子说明](./docs/brief-description-of-the-code-generation-process.md)

#### 模块说明
```
|--annotation-processor   注解处理(和编译器的注解处理器不同，应该叫注解解析更合适)
|--core                   核心模块，定义了顶层的接口
|--docs                   说明文档
|--example-swagger-2      写了一个
|--languages              不同语言的处理
|--model                  数据模型
|--oak-codegen            "oak" 风格的代码生成（风格主要是指模板，解析规则等）
|--swagger-codegen        基于swagger注解规则的生成
|--template               模板的通用处理
|--codegen-result-example 生成结果
```

- 期望（目标）

      1：将java代码转化为其他语言的代码

- 实现思路

      1：通过扫描java类生成的统一java类描述元数据


- 新的期望

     1：可以通过java代码或 open Api 规范进行生成 (java =>any => any => any)

- 新的思路

     1：增加通过解析open api的json格式文档，构建统一的描述元数据
     2：默认提供统一解析，通过提供不同语言的解析器进行自定义的处理
     3：在模板层面进行抽象，可以实现根据不同的数据分发不同的模板（灵活的模板加载策略）