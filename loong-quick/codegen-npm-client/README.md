
#### 从codegen-server下载sdk

#### 安装依赖后，在项目中执行

```text
 codegen gen  # 读取项目根目录的 codegenrc.json 文件
 codegen gen -config [filepath]  指定配置文件路径，filepath相对于项目根路径
 codegen g -config [filepath]   指定配置文件路径，filepath相对于项目根路径
```

#### 配置列表
```typescript
export interface DownloadCodegenSdkOptions {

    /**
     * 代码保存的服务器地址
     */
    loongCodegenServer: string;

    /**
     * 项目名称
     */
    projectName: string;

    /**
     * 分支名称
     * 默认：master
     */
    branch?: string;

    /**
     * 需要生成的模块名称
     * 默认：web
     */
    moduleName: string;

    /**
     * 生成的 sdk 类型
     * 默认: typescript_feign
     */
    type: "spring_cloud_openfeign" | "retrofit" | "dart_feign" | "typescript_feign" | "umi_request";

    /**
     * 代码下载的目录
     */
    downloadPath: string;

    /**
     * 代码输出目录
     */
    output?: string;
}
```