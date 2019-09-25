# Spring Security的OAuth2最小可用配置

本项目是在minimal项目基础上改造而来。

注意：

1. 本项目仅仅实现了password模式

2. 在访问获取access_token的过程中需要通过**Basic Auth**的方式提供client_id和client_secret，
具体的做法是对client_id和client_secret进行拼接，然后进行Base64的编码，然后在请求头中进行设置，具体如下：

client_id: client

client_secret: secret

拼接方式：client_id:client_secret => client:secret

Base64加密：client:secret =>Base64加密=> Y2xpZW50JTNBc2VjcmV0==

请求头部分加上参数：key: Authorization, value: Basic Y2xpZW50JTNBc2VjcmV0==，即 Authorization： Basic Y2xpZW50JTNBc2VjcmV0==

最后在请求体中带上grant_type，username，password三个参数，即可完成访问获取access_token

要使用OAuth2需要注意几点：

1. 要使用password模式，需要提供spring security的AuthenticationManager并注入**认证服务器中**，默认情况下该模式是关闭的。

2. 项目中最少需要设置一个Client

3. ResourceServerConfigurerAdapter与WebSecurityConfigurerAdapter在设置资源访问上存在重叠的部分，需要额外注意。

### ResourceServerConfigurerAdapter与WebSecurityConfigurerAdapter的关系

ResourceServerConfigurerAdapter与WebSecurityConfigurerAdapter均能通过**HttpSecurity**设置访问拦截，但是却存在先后顺序，
ResourceServerConfigurerAdapter优先级是3，WebSecurityConfigurerAdapter优先级是100，优先级数值越小则优先度越高，
所以两者应该是一种互补的关系，ResourceServerConfigurerAdapter主要负责资源相关的部分。
