#### codegen-server

#### 快速接入

-- 搭建codegen-server，拉取镜像、启动

```cmd
 拉取镜像命令：docker pull registry.cn-hangzhou.aliyuncs.com/fengwuxp/codegen-server
 启动命令：docker run -p 8080:8080 -d registry.cn-hangzhou.aliyuncs.com/fengwuxp/codegen-server:latest
  
 容器对外暴露的端口为8080，
 可以挂在的卷目录为：/home/codegen/
```

- 需要生成代码的Spring项目增加如下插件配置(server端)

```xml

<plugin>
    <groupId>com.wuxp.codegen</groupId>
    <artifactId>wuxp-codegen-loong-maven-plugin</artifactId>
    <version>xxx</version>
    <configuration>
        <skip>false</skip>
        <projectName>项目名称</projectName>
        <clientProviderTypes>需要生成的sdk类型(具体取值见下文)</clientProviderTypes>
        <pluginCodeGeneratorClass>定制化生成时的类，默认可以不配置</pluginCodeGeneratorClass>
        <loongCodegenServer>codegen 服务端地址，例如：http://localhost:8080</loongCodegenServer>
    </configuration>
</plugin>

```

##### 支持生成的sdk类型取值列表，不区分大小写

- [SPRING_CLOUD_OPENFEIGN](https://github.com/spring-cloud/spring-cloud-openfeign)
- [RETROFIT](https://github.com/square/retrofit)
- [TYPESCRIPT_FEIGN](https://github.com/fengwuxp/fengwuxp-typescript-spring/tree/master/feign)
- [UMI_REQUEST](https://github.com/umijs/umi-request)
- [DART_FEIGN](https://github.com/fengwuxp/fengwuxp_dart_feign)

#### client 端

##### java client maven plugin

```xml

<plugin>
    <groupId>com.wuxp.codegen</groupId>
    <artifactId>wuxp-codegen-loong-client-maven-plugin</artifactId>
    <version>${project.version}</version>
    <configuration>
        <projectName>项目名称，需要和服务端保持一致</projectName>
        <clientType>spring_open_feign</clientType>
        <loongCodegenServer>codegen 服务端地址，例如：http://localhost:8080</loongCodegenServer>
    </configuration>
</plugin>

```

##### npm client

```text
 安装依赖： yarn add fengwuxp-codegen-client
 npm run codegen gen 或者 npm run codegen gen -config configfile

```
- 默认配置文件名称为：codegenrc.json
- 配置文件格式

```json
{
  "projectName": "项目名称",
  "loongCodegenServer": "codegen server地址 例如：http://locahost:8080",
  "branch": "分支名称，非必填",
  "moduleName": "模块名称，非填",
  "type": "client lib type，例如：typescript_feign "
}
```