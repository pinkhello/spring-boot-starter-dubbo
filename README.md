# spring-boot-starter-dubbo
## 代码地址
gitee地址
```
https://gitee.com/hello-go/spring-boot-starter-dubbo.git
```
github地址
```
https://github.com/lee123lee123/spring-boot-starter-dubbo.git
```

## 使用方式
maven仓库地址
```
https://oss.sonatype.org/#nexus-search;quick~spring-boot-starter-dubbo
```

在项目的pom文件中增加如下依赖：
```
<dependency>
  <groupId>io.gitee.hello-go</groupId>
  <artifactId>spring-boot-starter-dubbo</artifactId>
  <version>1.0.6-RELEASE</version>
</dependency>
```


在application.yml文件中增加如下配置(只是例子，具体配置见提示)：

```
dubbo:
  application:
    name: xxx                           #必须
  annotation:
    package-name: xxx.xxx.xxx           #必须
  protocol:
    port: -1                            #协议端口 非必须
  registry:
    registries[0]:                      #必须有一组
      name: xxx_register_0              #多注册中心在 bean的唯一标示 不能重名
      address: 127.0.0.1:2181           #注册中心
      group: xxx                        #dubbo 根节点组，可以不配置，使用默认的
    registries[1]:
      name: xxx_register_1              #多注册中心在 bean的唯一标示 不能重名
      address: 127.0.0.1:2181           #注册中心
      group: xxx                        #dubbo 根节点组，可以不配置，使用默认的

```

在spring-boot Bootstrap 上增加如下元注解配置:

```
@EnableDubbo
```

服务提供方 在service实现类是添加上增加dubbo服务元注解配置:

```
@Service                               #多注册中心可以指定注册中心 非必须
```
服务消费方 在引用服务上是添加上增加dubbo依赖元注解配置:

```
@Reference                             #多注册中心可以指定注册中心 非必须
```

## DUBBO官方启动脚手架
dubbo-2.5.8 后dubbo内置了EnableDubbo注解，为支持SpringBoot做了扩展
```
https://github.com/alibaba/dubbo-spring-boot-starter
```



