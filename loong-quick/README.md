### 快速接入支持

- codegen-server 从版本控制服务获取代码然后调用mvn命令编译、执行代码生成，并提供下载生成结果的的接口
- restful接口文档地址：http://host:port/swagger-ui/index.html?url=/v3/api-docs
- [springdoc-openapi-ui接入](https://www.jianshu.com/p/b6f31966c5e1)

#### 命令行执行插件

- [Maven的插件：命令行执行](https://blog.csdn.net/weixin_33937499/article/details/89629491)
- [Maven的插件：命令行执行](http://www.dovov.com/maven-8.html)

```text

 使用mvn命令调用 wuxp-codegen-loong-maven-plugin 插件
 mvn compile -P dist_repo_profile,jcenter && mvn compile test -Dmaven.test.skip=true -P dist_repo_profile,jcenter  && mvn com.wuxp.codegen:wuxp-codegen-loong-maven-plugin:1.1.2-SNAPSHOT:api-sdk-codegen -P dist_repo_profile,jcenter
 
 mvn org.apache.maven.plugins:maven-dependency-plugin:get -Dartifact=mvn com.wuxp.codegen:wuxp-codegen-loong-maven-plugin:1.1.2-SNAPSHOT:api-sdk-codegen -P dist_repo_profile,jcenter
 
 mvn package -Dmaven.test.skip=true  -P dist_repo_profile,jcenter
 

```
