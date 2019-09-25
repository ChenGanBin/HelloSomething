# Spring Security最小配置

Spring Security项目最小可用的配置说明：

1. 设置了一个用户**admin**(密码：1234)
2. 设置了登陆点（“/login/login”），必须使用POST请求方式才能生效
3. 设置了登出点（“/login/logout”），这里需要注意的是如果开启了**CSRF**防御必须使用POST方式进行请求
4. 分别设置了一个被保护的资源（“/protected/res”，也是本程序的主页）和公开的资源（“/public/res”）

通过上面的配置，一个常见的具备登陆登出可以保护指定资源的Spring Security就配置完成了。
