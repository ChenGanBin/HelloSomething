# Spring Security OAuth2的Jwt使用

本项目是在oauth-minimal项目基础上改造而来。

要使用JWT只需要简单的为认证服务器和资源服务器设置为使用同一套的JwtTokenStore和JwtAccessTokenConverter即可（即JwtTokenStore和JwtAccessTokenConverter要有相同的实现）。

使用access_token的方法：

1. 在请求头中设置client_id和client_secret，如oauth-minimal中的一致

2. 然后grant_type类型为refresh_token

3. 最后带上参数refresh_token

以这样的格式访问/oauth/token即可获取新的access_token

特别注意：

在认证服务器和资源服务器中的JwtAccessTokenConverter均需要加上@Bean注解，否则会使得程序无法正常运行
（i.e. 认证服务器中的JwtAccessTokenConverter没有添加@Bean，然后使用refresh_token，会使的verify为null，从而抛出异常）。

其中有几点需要注意：

1. 认证服务器和资源服务器要使用同一个Jwt实现（在本项目中是JwtTokenStore、CustomTokenEnhancer（JwtAccessTokenConverter派生类）），这样才能正确转换。

2. 如果要自定义Jwt的信息，可以简单的通过继成JwtAccessTokenConverter来实现

3. 要在HttpSecurity中显式的设置requestMatchers，这样才能调用OAuth2的拦截器**OAuth2AuthenticationProcessingFilter**从而根据token来注入用户信息，完成认证

4. 如果需要**refresh token**则需要填入在authorizedGrantTypes方法中填入相应的参数“refresh_token”，详情见下面。
其次还需要在endpoints中注入UserDetailServices，否则会出错是的refresh_token失效，相关描述见[OAuth 2 Developers Guide的Grant Type中的UserDetailService](https://github.com/spring-projects/spring-security-oauth/blob/master/docs/oauth2.md)部分

5. 关于重用refresh_token：
在AuthorizationServerEndpointsConfigurer中有个方法reuseRefreshTokens是设置是否重用refresh_token（默认是true），
当为true的时候将会重用传入的refresh_token，当为false的时候会重新生成一个，在使用jwt的情况下比较特别，因为当为设置为true的时候，
refresh_token请求前和返回后应该会是完全一样才对，可是却每次都发生了变化，这是因为重新生成的access_token的拥有了新的ati所以导致了refresh_token的略微不同，但这不影响使用，其它信息还是一模一样的
（即：false会使得refresh_token的信息完全改变（ati，exp等），true(默认值)-仅仅改变ati对应的access_token的值，所以在使用jwt的情况下会使得编码后的refresh_token看起来不一样），是完全正常不影响使用的。
具体参考[在spring boot中结合OAuth2使用JWT时，刷新token时refresh token一直变化的原因](https://www.jianshu.com/p/2ccc5cee3cf2)

[JWT参考](http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html)

jwt负载部分的参数：

* iss (issuer)：签发人
* exp (expiration time)：过期时间
* sub (subject)：主题
* aud (audience)：受众
* nbf (Not Before)：生效时间
* iat (Issued At)：签发时间
* jti (JWT ID)：编号

关于authorizedGrantTypes方法中的参数：

具体参数可参考[OAuth2授权类型](https://oauth.net/2/grant-types/)，该方法指定了授权类型，
即如果是密码模式需要填入"password"，如果需要**refresh token**则需要填入“refresh_token”。
如果为密码模式则需要填入“password”。
