#### 一些设计思路

- 扫描的需要生成代码的包路径

```
 那些包下的类需要进行代码生成，一般是控制器的目录，支持ant匹配模式，例如：
     com.wuxp.codegen.swagger2.**.controller
```  

- 类型的映射，将一个java类转换为其他java类或者其他语言类型
```
  设置java的类型和生成目标语言类型的映射关系，例如：
  AbstractTypeMapping.setBaseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE);
  AbstractTypeMapping.setBaseTypeMapping(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE);
  AbstractTypeMapping.setBaseTypeMapping(ServiceResponse.class, TypescriptClassMeta.PROMISE);
  
  自定义的类型映射，将一个java映射为一个或多个其他的java类型，例如：
  Map<Class<?>, Class<?>[]> customTypeMapping = new HashMap<>();
  customTypeMapping.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});
```

- 包名映射，将java的包路径转换为其他目录，不同的语言可能需要不同的映射策略
```
  配置一个需要生成类所属的java包对应到输出路径下的包结构（目录结构，例如：
     //包名映射关系
     Map<String, String> packageMap = new LinkedHashMap<>();
     // com.wuxp.codegen.swagger2.example.controller下的类生成到输出路径的com/wuxp/codegen/swagger2/retrofits路径下
     packageMap.put("com.wuxp.codegen.swagger2.example.controller", "com.wuxp.codegen.swagger2.retrofits");
     
    // com.wuxp.codegen.swagger2.controlle下的类生成到输出路径下的 clients目录下
    packageMap.put("com.wuxp.codegen.swagger2.controller", "clents");
    //其他类（DTO、VO等）所在的包
    // com.wuxp.codegen.swagger2.example的类生成到输出路径下的models目录下
    packageMap.put("com.wuxp.codegen.swagger2.example", "models");
```
#### 注解处理
```
   注解在java开发中有着重要的地位，用来标记不同的信息，用于在编译时、运行时等阶段交由其他代码解释处理，然而在其他语言中不一定java这么丰富的注解
，甚至压根就不支持注解，例如：C。所以我们在生成代码时需要根据不同的语言特性和对注解的支持程度，将java的注解转换为其他语言的注解或者是注释
```
#### 注解处理例子
这是一段java的代码示例
```java

@Api("订单服务")
@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController<String> {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取订单列表", notes = "")
    @GetMapping(value = {"get_order"})
    public List<Order> getOrder(String[] names, List<Integer> ids, Set<Order> moneys) {
        return Collections.EMPTY_LIST;
    }

    @ApiOperation(value = "获取订单列表", notes = "")
    @RequestMapping(method = RequestMethod.GET)
    public PageInfo<Order> queryOrder(QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }


    @ApiOperation(value = "获取订单列表", notes = "")
    @PostMapping(value = {"queryOrder2"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ServiceQueryResponse<Order> queryOrder2(@ApiParam("订单id")
                                                   @RequestParam(name = "order_id", required = false) Long oderId,
                                                   @ApiParam(value = "订单号", required = false) String sn,
                                                   @ApiParam(value = "用户id", hidden = true) Long memberId) {

        return new ServiceQueryResponse<>();
    }

    @ApiOperation(value = "查询分页", notes = "")
    @PostMapping(value = {"queryPage"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "订单", required = true, dataType = "String"),
            }
    )
    public ServiceResponse<PageInfo<Order>> queryPage(String id) {

        return new ServiceResponse<>();
    }


    @ApiOperation(value = "创建订单", notes = "")
    @PostMapping(value = {"createOrder"})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "evt", value = "创建订单", required = true),
            }
    )
    public ServiceResponse<Long> createOrder(@RequestBody CreateOrderEvt evt) {

        return new ServiceResponse<>();
    }


    @ApiOperation(value = "test hello", notes = "")
    @PostMapping(value = {"hello"})
    public ServiceResponse hello() {

        return new ServiceResponse<>();
    }

}


@ApiModel("创建订单")
@Data
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateOrderEvt extends BaseEvt {

    @ApiModelProperty(value = "订单ns", example = "test method", required = true)
    @Size(max = 50)
    private String sn;

    @ApiModelProperty("订单总价")
    @NotNull
    private Integer totalAmount;

}
```
生成的typescript代码
```typescript
/* tslint:disable */

/**
 * 订单服务
 * 接口：GET
 **/
@Feign({
    value:"/order",
})
class OrderService{

    /**
     * 1:获取订单列表
     * 2:接口方法：GET
     * 3:返回值在java中的类型为：List
     * 4:返回值在java中的类型为：Order
     **/
    @GetMapping({
        value:"get_order",
        produces:[HttpMediaType.FORM_DATA],
    })
    getOrder!:(req: OrderServiceGetOrderReq, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
     * 1:获取订单列表
     * 2:接口方法：GET
     * 3:返回值在java中的类型为：PageInfo
     * 4:返回值在java中的类型为：Order
     **/
    @GetMapping({
        produces:[HttpMediaType.FORM_DATA],
    })
    queryOrder!:(req: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
     * 1:获取订单列表
     * 2:接口方法：POST
     * 3:返回值在java中的类型为：ServiceQueryResponse
     * 4:返回值在java中的类型为：Order
     **/
    @PostMapping({
        produces:[HttpMediaType.MULTIPART_FORM_DATA],
    })
    queryOrder2!:(req: OrderServiceQueryOrder2Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
     * 1:查询分页
     * 2:接口方法：POST
     * 3:<pre>
     *参数列表：
     *参数名称：id，参数说明：属性名称：id，属性说明：订单，示例输入：
     *</pre>
     * 4:返回值在java中的类型为：ServiceResponse
     * 5:返回值在java中的类型为：PageInfo
     * 6:返回值在java中的类型为：Order
     **/
    @PostMapping({
        produces:[HttpMediaType.FORM_DATA],
    })
    queryPage!:(req: OrderServiceQueryPageReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
     * 1:创建订单
     * 2:接口方法：POST
     * 3:<pre>
     *参数列表：
     *参数名称：evt，参数说明：属性名称：evt，属性说明：创建订单，示例输入：
     *</pre>
     * 4:返回值在java中的类型为：ServiceResponse
     * 5:返回值在java中的类型为：Long
     **/
    @PostMapping({
        produces:[HttpMediaType.APPLICATION_JSON_UTF8],
    })
    createOrder!:(req: CreateOrderEvt, option?: FeignRequestOptions) => Promise<number>;
    /**
     * 1:test hello
     * 2:接口方法：POST
     * 3:返回值在java中的类型为：ServiceResponse
     **/
    @PostMapping({
        produces:[HttpMediaType.FORM_DATA],
    })
    hello!:(req: OrderServiceHelloReq, option?: FeignRequestOptions) => Promise<any>;
}

export default new OrderService();


/**
 * 创建订单
 **/
export interface CreateOrderEvt extends BaseEvt {

    /**
     *属性说明：订单ns，示例输入：test method
     *属性：sn输入字符串的最小长度为：0，输入字符串的最大长度为：50
     *在java中的类型为：String
     **/
    sn?: string;

    /**
     *属性说明：订单总价，示例输入：
     *属性：totalAmount为必填项，不能为空
     *在java中的类型为：Integer
     **/
    totalAmount: number;

}
```
可以看到程序将一部分(RequestMapping相关)java注解装换成了typescript的装饰器（注解），swagger和java.validation相关的注解转换成了对应的
注释，还有Controller这个注解就直接被忽略。

##### api sdk 代码生成接入指南

- 引用依赖

```
 <!--swagger2-->
   <dependency>
            <groupId>com.wuxp.codegen</groupId>
            <artifactId>wuxp-codegen-swagger2</artifactId>
            <version>xx</version>
            <scope>test</scope>
   </dependency>
   
    <!--swagger3-->
    <dependency>
            <groupId>com.wuxp.codegen</groupId>
            <artifactId>wuxp-codegen-swagger3</artifactId>
            <version>xx</version>
            <scope>test</scope>
   </dependency>
```

- 生成代码示例

```java
// 用于生成retrofit clients的例子
public class Swagger2FeignSdkCodegenRetrofitTest {


    @Test
    public void testCodeGenRetrofitApiByStater() {


        //设置基础数据类型的映射关系
        Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new HashMap<>();
        AbstractTypeMapping.setBaseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE);

        //自定义的类型映射
        Map<Class<?>, Class<?>[]> customTypeMapping = new HashMap<>();
        customTypeMapping.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.example.controller", "com.wuxp.codegen.swagger2.retrofits");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger2";
        packageMap.put("com.wuxp.codegen.swagger2.example", basePackageName);

        String language = LanguageDescription.JAVA_ANDROID.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.RETROFIT.name().toLowerCase(), "swagger2", "src"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};

        Swagger2FeignJavaCodegenBuilder.builder()
                // 是否使用rxjava，该参数仅支持用于生成java的client
                .useRxJava(true)
                .build()
                .baseTypeMapping(baseTypeMapping)
                // 设置生成目标的语言
                .languageDescription(LanguageDescription.JAVA_ANDROID)
                // 请求client工具的提供者类型
                .clientProviderType(ClientProviderType.RETROFIT)
                .customJavaTypeMapping(customTypeMapping)
                // 包名映射策略
                .packageMapStrategy(new JavaPackageMapStrategy(packageMap, basePackageName))
                // 输出目录
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                // 扫描的包路径
                .scanPackages(packagePaths)
                // 是否删除原本的输出目录
                .isDeletedOutputDirectory(false)
                // 生成dto对象时字段是否使用下划线方式命名（驼峰==>下划线）
                .enableFieldUnderlineStyle(false)
                // 构建代码生成器
                .buildCodeGenerator()
                // 生成
                .generate();

    }
}


```

- 额外说明，即时项目中没有swagger相关的注解也不影响生成，swagger的注解不是必须的，它只是用来生成注释的，例如这样：
```typescript
// 有swagger注解

/* tslint:disable */
import {BaseQueryEvt} from "./BaseQueryEvt";

/**
 * 测试的API接口方法一的请求参数
 **/

export interface QueryOrderEvt extends BaseQueryEvt {

    /**
     *属性说明：订单sn，示例输入：test method
     *属性：sn输入字符串的最小长度为：0，输入字符串的最大长度为：50
     *在java中的类型为：String
     **/
    sn?: string;

    /**
     *属性说明：id列表，示例输入：
     *在java中的类型为：数组
     *在java中的类型为：int
     **/
    ids?: number[];

}

// 如果没有对应的swagger注解

/* tslint:disable */

import {BaseQueryEvt} from "./BaseQueryEvt";


export interface QueryOrderEvt extends BaseQueryEvt {

    /**
     *属性：sn输入字符串的最小长度为：0，输入字符串的最大长度为：50
     *在java中的类型为：String
     **/
    sn?: string;

    /**
     *在java中的类型为：数组
     *在java中的类型为：int
     **/
    ids?: number[];

}

```