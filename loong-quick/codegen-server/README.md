

#### codegen server
- 认证权限控制使用spring security
- 数据库为了部署使用h2

#### 相关资料
- [H2数据库连接 URL说明](https://blog.csdn.net/weixin_33913332/article/details/92007241)

#### docker
-- 拉取镜像启动
```cmd
  docker pull registry.cn-hangzhou.aliyuncs.com/fengwuxp/codegen-server:latest
  docker run -p 8080:8080 -d registry.cn-hangzhou.aliyuncs.com/fengwuxp/codegen-server:latest
```