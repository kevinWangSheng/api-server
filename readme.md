# OpenApi共享平台

基本流程图

![image-20230911003215931](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230911003215931.png)



## 知识点

一般对应controller层的异常处理，我们可以自己定义一个GlobalExceptionHandler配合注解@RestControllerAdvice进行处理，这个注解可以理解为controller的aop，通过动态代理的方式，在配合注解@ExceptionHandler指定对应的注解异常，来进行处理controller注解。

就是出现异常之后，他通过spring的aop的动态代理来进行执行handler操作。

### 基础配置

#### 微信公众号平台

**WxMpMessageRoute**

1. 消息路由：根据不同的消息类型和内容，将消息分发给不同的处理器进行处理。
2. 消息过滤：可以根据消息的内容、用户标识等条件，对消息进行过滤，决定是否要处理这条消息。
3. 消息处理链：可以配置多个处理器构成一个处理链，依次处理消息，每个处理器负责不同的逻辑。
4. 消息转发：可以将某条消息转发到其他处理器进行二次处理。
5. 消息回复：处理器可以返回响应消息，用于回复用户的消息。

一个简单的例子



```java
WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

// 处理文本消息的处理器
router.rule().async(false).content("hello").handler(handler1).end();

// 处理图片消息的处理器
router.rule().msgType(WxConsts.XmlMsgType.IMAGE).handler(handler2).end();

// 处理事件消息的处理器
router.rule().msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(handler3).end();

WxMpXmlMessage wxMessage = ...; // 收到的微信消息对象
WxMpXmlOutMessage response = router.route(wxMessage); // 进行消息路由，获取响应消息

if (response != null) {
    // 返回响应消息
    responseWriter(response);
}

```



#### @JsonComponent

`@JsonComponent` 是 Spring Framework 提供的注解之一，用于自定义 JSON 序列化和反序列化的行为。它允许你定义自己的 JSON 序列化器和反序列化器，并将它们注册到 Spring 应用的 JSON 处理器中，从而实现对特定类型的定制化处理。

配置Json序列化问题

```java
 @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        return objectMapper;
    }
```

1. `ObjectMapper` 是 Jackson 库中的主要类，它负责处理对象和 JSON/XML 之间的转换。
2. `ObjectMapper.Builder` 是用于构建 `ObjectMapper` 实例的构建器。
3. `builder.createXmlMapper(false)` 创建了一个不支持 XML 映射的 ObjectMapper 实例。在这里，`false` 参数表示不支持 XML 映射，只用于处理 JSON 数据。
4. `.build()` 调用完成构建，并返回一个配置好的 ObjectMapper 实例。
5. `SimpleModule` 是 Jackson 库提供的类，用于自定义序列化和反序列化的规则。
6. `module.addSerializer(Long.class, ToStringSerializer.instance)` 和 `module.addSerializer(Long.TYPE, ToStringSerializer.instance)` 注册了一个自定义的序列化器，将 `Long` 类型的数据序列化为字符串。这是因为在处理大整数时，如果直接序列化为 JSON 时可能会丢失精度，将其序列化为字符串可以避免这个问题。
7. `ToStringSerializer.instance` 是一个 `ToStringSerializer` 类的单例实例，用于将数据转换为字符串。
8. `objectMapper.registerModule(module)` 将自定义的 `SimpleModule` 注册到 ObjectMapper 中，以便在序列化和反序列化过程中使用。





### Knife4j

 介绍

 ![image-20230821164134183](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230821164134183.png)

示例代码

```java
@Bean
    public Docket default2pi2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("接口文档")
                        .description("springboot-init-project")
                        .version("1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.kevin.wang.springpatternkevinwang.controller"))
                .paths(PathSelectors.any())
                .build();
```

这段代码是用于配置 Springfox Swagger 生成 API 文档的一部分。它的作用是创建一个 `Docket` 实例，然后配置它的各种属性，以定义要生成的 API 文档的规则和基本信息。让我逐步解释每个部分的含义：

1. `return new Docket(DocumentationType.SWAGGER_2)` 这行代码创建了一个新的 `Docket` 实例，并指定了要使用的 Swagger 版本，这里是 `SWAGGER_2`。
2. `.apiInfo(new ApiInfoBuilder()...)` 这部分设置了 API 文档的基本信息，包括标题、描述和版本等。通过 `ApiInfoBuilder` 类的链式调用，你可以构建一个包含这些信息的 `ApiInfo` 实例，然后将其传递给 `Docket`。
   - `.title("接口文档")`：设置 API 文档的标题为 "接口文档"。
   - `.description("springboot-init")`：设置 API 文档的描述为 "springboot-init"。
   - `.version("1.0")`：设置 API 文档的版本为 "1.0"。
3. `.select()` 这部分表示你要对哪些 API 进行配置。你可以使用 `select()` 方法来定义要扫描的 API 控制器类、API 路径等。
4. `.apis(RequestHandlerSelectors.basePackage("com.yupi.springbootinit.controller"))` 这行代码指定了要扫描的 API 控制器所在的包路径。在这个例子中，它将会扫描名为 `com.yupi.springbootinit.controller` 的包中的所有 API 控制器类。
5. `.paths(PathSelectors.any())` 这行代码表示所有的 API 路径都会被包括在生成的文档中。`PathSelectors.any()` 指示选择所有路径。
6. `.build()` 最后，使用 `build()` 方法将前面设置的配置选项和规则应用到 `Docket` 实例，从而完成了配置。



#### Spring是如何解决循环依赖问题的

当存在循环依赖时，Spring 使用以下步骤来初始化相关的 Bean：

1. **Bean 的实例化**：首先，Spring 会实例化每个 Bean，但是并不会立即注入它们的依赖。在实例化过程中，Spring 会将对象的引用放入早期阶段的缓存中。
2. **属性注入**：在实例化之后，Spring 开始进行属性注入。对于循环依赖的情况，Spring 使用一种特殊的机制，先创建 Bean 实例，然后再注入循环依赖的属性。
3. **Autowiring by Type**：Spring 使用自动装配（Autowiring）时，会在一个 Bean 的属性上搜索匹配的类型的其他 Bean。如果发现循环依赖，Spring 会将属性设置为未解析的代理对象，以防止循环依赖问题。
4. **解析循环依赖**：当出现循环依赖时，Spring 使用代理模式来解决。对于 Bean A 和 Bean B，如果 Bean A 依赖于 Bean B，而 Bean B 同时依赖于 Bean A，Spring 会为其中一个 Bean（例如 Bean A）创建一个代理对象，用于提供未解析的依赖。当另一个 Bean（例如 Bean B）需要访问被代理的 Bean（Bean A）时，代理对象会被触发，Spring 会使用尚未完全初始化的原始对象来满足依赖。

#### redis存储的时候出现了key序列化情况
这个一般是使用JDK默认的序列化机制进行对应的序列化操作的。
你可以使用StringRedisSerializer 进行对应的设置，将他的设置默认是string的序列化方式，这样在存储的key的时候就不会出现一些二进制的东西了

#### shiro配合获取当前登录用户

使用shiro的认证机制，并且做好realm的配置，在用户登录的时候，调用一个userInfo将用户的信息进行登录验证。

然后把这些信息放到Shiro的SecurityUtils中的principal中，这个你想要放的东西取决于你在配置的时候对应的参数设置。

举例如下：

 

```java
@Component
public class AuthModel {
    public boolean userInfo(LoginUserVO loginUserVO,String password){
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(loginUserVO.getUserName(),password);
            ThrowUtils.throwIf(token==null, ErrorCode.OPERATION_ERROR);
            subject.login(token);
            return true;
        } catch (UnknownAccountException e) {
            throw new BussinessException(ErrorCode.NOT_FOUND_ERROR,"用户名不存在");
        }catch (IncorrectCredentialsException e) {
            throw new BussinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
        }
    }
}
```

这个是用户在登录完成，验证用户账户和密码通过的时候在进行对应的信息记录和用户认证

并且他这个验证信息是根据你传入的realName，把他收集成为一个集合。

他这个设计思路你值得借鉴一下，就是首先先从缓存里面进行用户验证，如果缓存里面有，就不需要验证了，否则就进行验证，然后在把用户放到缓存中，下次就不需要在验证了。

在验证的过程中，通过你传入的new 的那个 `SimpleAuthenticationInfo(Object principal, Object credentials, String realmName)`这个的principal就是你需要验证的对象，credentials就是验证对象的密码，其中会通过你new 的那个Token然后进行密码和账户的比较。

验证完成会返回你的基本信息info，就是你传入的那个principal

这个subject的对象参数

 ![image-20230907222707477](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230907222707477.png)

这个是他创建的subject方法

```java
protected Subject createSubject(AuthenticationToken token, AuthenticationInfo info, Subject existing) {
        SubjectContext context = createSubjectContext();
        context.setAuthenticated(true);
        context.setAuthenticationToken(token);
        context.setAuthenticationInfo(info);
        context.setSecurityManager(this);
        if (existing != null) {
            context.setSubject(existing);
        }
        return createSubject(context);
    }
```

然后还会将你的cookie内容进行更新

```java
public void removeFrom(HttpServletRequest request, HttpServletResponse response) {
        String name = this.getName();
        String value = "deleteMe";
        String comment = null;
        String domain = this.getDomain();
        String path = this.calculatePath(request);
        int maxAge = 0;
        int version = this.getVersion();
        boolean secure = this.isSecure();
        boolean httpOnly = false;
        Cookie.SameSiteOptions sameSite = this.getSameSite();
        this.addCookieHeader(response, name, value, (String)comment, domain, path, maxAge, version, secure, httpOnly, sameSite);
        log.trace("Removed '{}' cookie by setting maxAge=0", name);
    }

```



防止重放攻击，使用redis+消息队列的方式进行

#### 使用rabbitmq配合redis完成用户随机数删除

rabbitmq配合docker-compose安装

教程地址：https://x-team.com/blog/set-up-rabbitmq-with-docker-compose/#:~:text=Open%20a%20terminal%2C%20navigate%20to,to%20http%3A%2F%2Flocalhost%3A15672.

docker-compose.yml

```yml
version: "3.12.4"  # 当前的版本
services:        #对应的服务
  rabbitmq:      #包含rabbitmq的信息，rabbitmq容器和他的版本，这里如果想登录管理界面，你应该下载的是rabbitmq:management
    image: rabbitmq:latest
    container_name: 'rabbitmq'   #制定的容器名字
    ports:             #对应映射的端口号
      - 5672:5672
      - 15672:15672
    volumes:          #容器挂载的数据卷，用于数据同步
      - /opt/dev/software/rabbitmq/data/:/var/lib/rabbitmq/
      - /opt/dev/software/rabbitmq/log/:/var/log/rabbitmq
    network:         #使用自己配置的网络，通过docker network create rabbitnet 方式创建，默认是使用bridge桥接的网络方式
      - rabbitnet

networks:
  rabbitnet:
    driver: bridge
```

```
ARG PLUGIN_VERSION=3.10.2
ARG BASE_VERSION=3.10.13

FROM ubuntu:18.04 AS builder

ARG PLUGIN_VERSION

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y curl

RUN mkdir -p /plugins && \
	curl -fsSL \
	-o "/plugins/rabbitmq_delayed_message_exchange-3.11.1.ez" \
	https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.11.1/rabbitmq_delayed_message_exchange-3.11.1.ez

FROM rabbitmq:3.11.1-management

ARG PLUGIN_VERSION

COPY --from=builder --chown=rabbitmq:rabbitmq \
	/plugins/rabbitmq_delayed_message_exchange-3.11.1.ez \
	/opt/dev/software/rabbitmq/plugins/rabbitmq_delayed_message_exchange-3.11.1.ez

RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

RUN rabbitmq-plugins enable --offline rabbitmq_consistent_hash_exchange
```

![image-20230909102644672](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230909102644672.png) 

rabbitmq在处理发送消息的时候，首先会进行一些基础的配置，什么properties，然后检查一些基本的配置，什么`BasicProperties convertedMessageProperties = this.messagePropertiesConverter
       .fromMessageProperties(message.getMessageProperties(), this.encoding);`

然后底层最终会调用basicPublish，然后这个会调用根据你监听器实现的东西进行调用他对应的方法。他底层会创建一个AMQCommand

### RabbitMq延迟队列的配置

```java
@Configuration
public class RabbitMqConfig {
    
    @Bean
    CustomExchange pluginDelayedExchange(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-delayed-type","direct");
        return new CustomExchange(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getExchangeName(), "x-delayed-message",true,false,args);
    }

    @Bean
    public Queue accessKeyQueue(){
        return new Queue(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getQueueName());
    }

    @Bean
    Binding accessKeyBinding(CustomExchange pluginDelayedDirectExchange,Queue accessKeyQueue){
        return BindingBuilder
                .bind(accessKeyQueue)
                .to(pluginDelayedDirectExchange)
                .with(RabbitMQQueueEnum.QUEUE_ACCESSKEY_CHANNEL.getRouteKey())
                .noargs();
    }
}

```

### Shiro的配置和执行流程

配置，这个决定了你需要使用的是什么配置，这里定义了一个自己的SecurityManager

```java
@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        Map<String,String> filter = new HashMap<>();

        filter.put("/login","anon");
        filter.put("/*","anon");

        factoryBean.setLoginUrl("/login");
        factoryBean.setUnauthorizedUrl("403");
        factoryBean.setFilterChainDefinitionMap(filter);
        return factoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean("shiroRealm")
    public ShiroRealm shiroRealm(){
        return new ShiroRealm();
    }


}
```

```java
public class ShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("执行授权逻辑");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();
        ThrowUtils.throwIf(username==null, ErrorCode.NOT_LOGIN_ERROR);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount",username);
        User user = userService.getOne(wrapper);
        if(user==null){
            return null;
        }
        return  new SimpleAuthenticationInfo(user,user.getUserPassword(),"");
    }
}
```

首先他这个shiro通过拦截器，在你执行对应的restfull请求的时候，他会执行对应的doAuth...方法，就是验证的意思，然后最后会执行到你对应的Relam中的doGetAuthenticationInfo，这之前你需要使用一个auth进行登录，这个登录是你SecurityUtils.getSubject()获取信息，然后执行登录，登录就是你传递的token，这个token类似账号密码的东西，你可以在doGetAuthenticationInfo的时候进行一次用户验证，具体可以在这个时候进行数据库的查询用户以，后面你进行验证的时候，他会调用你最后返回的验证信息，return  new SimpleAuthenticationInfo(user,user.getUserPassword(),"");，通过这里的user.getPassword()方法来进行对应的验证，看看你之前设置的是否正确，这里的user是作为记录信息的，相当于subject()的principal，其中他是通过http线程上下文中的sessionId来进行获取的，所以你在走请求的时候，可以直接获取到你对应存储到里面的东西，一般存储登录的用户，他是一个Map，然后可以存储多个不同的登录用户，都是根据你的线程上下文，注意，这里的线程是http的，不是线程池的。

当所有验证都已经完成的时候，你才可以执行过滤完成之后的请求。



## 一些debug的东西

可以通过这里查看他的内存以及一些类的分布情况

 ![image-20230909151322801](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230909151322801.png)

可以查看他的spring总bean的注册情况

![image-20230909151404335](C:\Users\wang sheng hui\AppData\Roaming\Typora\typora-user-images\image-20230909151404335.png) 



## 接口调用

接口调用分为外部调用和内部调用

内部调用直接走请求就行，

不过今天懂了接口的调用，要么你就自己编写一个sdk，自己的sdk已经配置好了认证信息，签名什么之类的，然后用户在调用的话，他就是用在你网站注册的签名去调用，或者你定义一个通用的，用一张表存储起来，然后通过httpClient的方式调用，调用的时候通过接口id查找对应的接口的签名，然后将它放入到参数中。这样同样可以完成调用
