前后端分离项目接口联调时期存在的几个问题：

- 接口设计滞后
- 接口不规范
- 接口文档更新不及时，或者遗忘更新

对应的解决方案：

- 接口设计先行
- 统一的接口规范
- 使用 Swagger 自定生成 API 文档

操作步骤：

- 在 pom.xml 中引入相关依赖

  - Swagger 依赖 io.springfox: springfox-swagger2
  - Swagger-UI 依赖 io.springfox: springfox-swagger-ui

```xml
            <!--      Swagger 依赖      -->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>2.6.1</version>
            </dependency>
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>2.6.1</version>
            </dependency>
```

- <font color=red> Spring boot 暂未内置 Swagger 需要我们自定义配置类</font>
  - Spring boot 是怎么做到内置这些好用的工具的 ？？ 
  - 对于文档怎么构建的，很明显是 反射、注释匹配这一套，呵呵哒
- 简单的创建 启动类，并编写自己的 controller 即可

```java
// swagger2 相关注释注解
@Api 作用域 class，标记改类作为 Swagger 文档资源
@ApiOperation 标记当前方法为一个 API 操作

```

