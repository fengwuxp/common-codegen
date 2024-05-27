package com.wuxp.codegen.swagger2.clients;
import retrofit2.http.*;


      import com.wuxp.codegen.swagger2.model.domain.Order;
      import com.wuxp.codegen.swagger2.model.domain.User;
      import java.util.List;
      import java.io.File;
      import java.util.Map;

    /**
     * 用户服务
     * 接口：GET
     * 用户服务（源码注释）
    **/

public interface UserRetrofitClient{

    /**
      * 1:GET /users
      * 2:获取用户列表
      * 3:Http请求方法：GET
      * 4:获取用户列表信息
      * @return 用户列表
      * 6:返回值在java中的类型为：List
      * 7:返回值在java中的类型为：User
     **/
      @GET(value = "/users" )
    List<User>  getUserList (
  );
    /**
      * 1:POST /users
      * 2:创建用户
      * 3:属性名称：user，属性说明：用户详细实体user，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:根据前端的提交内容创建用户
      * @return 用户Id
      * 7:返回值在java中的类型为：Long
     **/
      @POST(value = "/users" )
    Long  postUser (
          @Body()  User user,
  Order order
  );
    /**
      * 1:GET /users/{id}
      * 2:获取用户详细信息
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：User
     **/
      @GET(value = "/users/{id}" )
    User  getUser (
          @Path(value = "id" )  Long id
  );
    /**
      * 1:PUT /users/{id}
      * 2:更新用户详细信息
      * <pre>
      * 4:参数列表：
      * 5:参数名称：id，参数说明：null
      * 6:参数名称：user，参数说明：null
      * </pre>
      * 8:Http请求方法：PUT
      * 9:返回值在java中的类型为：String
     **/
      @PUT(value = "/users/{id}" )
    String  putUser (
          @Path(value = "id" )  Long id,
          @Body()  User user
  );
    /**
      * 1:DELETE /users/{id}
      * 2:删除用户
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：DELETE
      * 5:返回值在java中的类型为：String
     **/
      @DELETE(value = "/users/{id}" )
    String  deleteUser (
          @Path(value = "id" )  Long id,
  String name
  );
    /**
      * 1:GET /users/sample
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：String
     **/
      @GET(value = "/users/sample" )
    String  sample (
  Long[] ids,
  String name
  );
    /**
      * 1:GET /users/sample2
      * 2:sample
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：User
     **/
      @GET(value = "/users/sample2" )
    Map<String,User>  sampleMap (
  Long[] ids,
  String name,
  Map<String,String[]>[] testParam
  );
    /**
      * 1:POST /users/uploadFile
      * 2:文件上传
      * 3:属性名称：file，属性说明：文件，默认值：，示例输入：
      * 4:Http请求方法：POST
      * 5:返回值在java中的类型为：void
     **/
      @POST(value = "/users/uploadFile" )
    void  uploadFile (
          @Field(value = "file" )  File multipartFile
  );
    /**
      * 1:GET /users/test
      * 2:test3
      * 3:属性名称：id，属性说明：用户ID，默认值：，示例输入：
      * 4:Http请求方法：GET
      * 5:返回值在java中的类型为：Map
      * 6:返回值在java中的类型为：String
      * 7:返回值在java中的类型为：Object
     **/
      @GET(value = "/users/test" )
    Map<String,Object>  test3 (
  Long id
  );
}
