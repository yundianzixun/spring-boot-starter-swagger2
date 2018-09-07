# 简介

该项目主要利用Spring boot2.0 +Swagger2 方便进行测试后台的restful形式的接口，实现动态的更新，当我们在后台的接口修改了后，swagger可以实现自动的更新，而不需要认为的维护这个接口进行测试。

*   源码地址
    *   GitHub：[https://github.com/yundianzixun/spring-boot-starter-swagger2](https://github.com/yundianzixun/spring-boot-starter-swagger2)
*   联盟公众号：IT实战联盟
*   我们社区：[https://100boot.cn](https://100boot.cn)

**小工具一枚，欢迎使用和Star支持，如使用过程中碰到问题，可以提出Issue，我会尽力完善该Starter**

# 版本基础

*   Spring Boot：2.0.4
*   Swagger2：2.7.0
# [](https://github.com/SpringForAll/spring-boot-starter-swagger#%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8)

### 操作步骤
#### 第一步：下载SpringBoot2.0项目
*   GitHub地址：https://github.com/yundianzixun/spring-boot-starter
*   参考文档：https://www.jianshu.com/p/7dc2240f010e

#### 第二步：添加maven依赖
```
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.7.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.7.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
		</dependency>
		<!-- 打war包用 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```
#### 第三步：application.properties 增加swagger配置
```
#开启swagger服务
swagger.enable=true
```
#### 第四步：使用注解配置Swagger
```
@Configuration
@EnableSwagger2
public class Swagger2Config {
    public static final String BASE_PACKAGE = "com.itunion";
    @Value("${swagger.enable}")
    private boolean enableSwagger;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())                // 生产环境的时候关闭 swagger 比较安全
                .enable(enableSwagger)                //将Timestamp类型全部转为Long类型
                .directModelSubstitute(Timestamp.class, Long.class)                //将Date类型全部转为Long类型
                .directModelSubstitute(Date.class, Long.class)
                .select()                // 扫描接口的包路径，不要忘记改成自己的
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }    private ApiInfo apiInfo() {        return new ApiInfoBuilder()
            .title("Swagger RESTful APIs")
            .description("Swagger API 服务")
            .termsOfServiceUrl("http://swagger.io/")
            .contact(new Contact("Swagger", "127.0.0.1", "zhenghhgz@163.com"))
            .version("1.0")
            .build();
    }

}

```
## 备注
  * 正常项目上线后应该是关闭掉 swagger 的，所以这边增加了一个配置 enableSwagger
  * 可以使用 directModelSubstitute 做一些期望的类型转换

#### 第五步：创建用户实体类UserInfo
```
public class UserInfo {
    @ApiModelProperty("编号")
    private Long id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("姓")
    private String firstName;
    @ApiModelProperty("名")
    private String lastName;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty(hidden = true)// 密码不传输
    @JsonIgnore
    private String password;
    @ApiModelProperty("状态")
    private Integer userStatus;
   /**此处省略get、set **/
 }
```

#### 第六步：编写一个首页的Controller
```
@Api(value = "首页", description = "首页")
@RequestMapping("/")
@RestController
public class IndexController {
    @ApiOperation(value = "Hello Spring Boot", notes = "Hello Spring Boot")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello Spring Boot";
    }
    @ApiOperation(value = "API 页面", notes = "接口列表")
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public void api(HttpServletResponse response) throws IOException {
        response.sendRedirect("swagger-ui.html");
    }
}
```
* 为了方便访问swagger ui 页面，我们做了一个重定向 api 更方便些

#### 第七步：编写一个登陆的Controller
```
@Api(value = "用户", description = "用户")
@RequestMapping("/userInfo")
@RestController
public class UserInfoController {
    @ApiOperation(value = "登录接口-多值传值方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserInfo.class, responseContainer = "userInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParam(name = "map", value = "{\"userName\":\"JackMa\",\"passWord\":\"123\"}")
    @RequestMapping(value = "loginForMap", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UserInfo> loginForMap(@RequestBody Map<String, String> map) {
        if (!map.get("userName").equalsIgnoreCase("JackMa") || !map.get("passWord").equalsIgnoreCase("123")) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setUserName("JackMa");
        user.setFirstName("马");
        user.setLastName("云");
        user.setEmail("zhenghhgz@163.com");
        user.setUserStatus(1);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "登录接口-多值传输方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserInfo.class, responseContainer = "userInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "passWord",value = "密码", required = true, dataType = "string",paramType = "query"),
    })
    @RequestMapping(value = "loginForParams", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UserInfo> loginForMap(@RequestParam String userName, @RequestParam String passWord) {
        if (!userName.equalsIgnoreCase("JackMa") || !passWord.equalsIgnoreCase("123")) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setUserName("JackMa");
        user.setFirstName("马");
        user.setLastName("云");
        user.setEmail("jackma@163.com");
        user.setUserStatus(1);
        return ResponseEntity.ok(user);
    }

}

```
## 备注
* 使用Params和Param 实现了两种不同的数据传输方式
* 建议使用Spring的 ResponseEntity 类做统一的返回结果
* swagger 对 response code 的支持还算好，我们可以把可能出现的异常代码都一一罗列出来，方便对接的时候对异常的处理

#### 第八步：启动运行
```
http://127.0.0.1:8081/api
```
## 备注
* 端口号已自己配置为准

如下图所示：
![swagger2.jpg](https://upload-images.jianshu.io/upload_images/8122772-7a2236318ec533ac.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 第九步：执行
![输入.jpg](https://upload-images.jianshu.io/upload_images/8122772-8e4679d65d00ab27.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![输出.jpg](https://upload-images.jianshu.io/upload_images/8122772-ca1bb5031b394120.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## 贡献者

*   [IT实战联盟-Line](https://www.jianshu.com/u/283f93ada597)
*   [IT实战联盟-咖啡](https://www.jianshu.com/u/29d607600e98)


#### 更多精彩内容可以关注“IT实战联盟”公众号哦~~~

![image](http://upload-images.jianshu.io/upload_images/8122772-b78dee4c5818c874?imageMogr2/auto-orient/strip%7CimageView2/2/w/500)
