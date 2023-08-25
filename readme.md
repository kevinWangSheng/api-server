# Springboot初始化模板

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

