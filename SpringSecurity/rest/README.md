# Spring Security的REST

Spring Security本身不提供Rest化，因此要实现Rest化，只需要实现自己的登陆、登出、权限不足等处理器从而可以返回自己所需的信息。

常用的处理器接口：

1. AuthenticationSuccessHandler：成功登陆系统的接口
2. AuthenticationFailureHandler：登陆系统失败的接口
3. LogoutSuccessHandler：成功登出系统的接口
4. AccessDeniedHandler：登陆后权限不足的接口
5. AuthenticationEntryPoint：登陆前权限不足的接口

只要实现了以上接口并显式的进行设置，即可把Spring Security进行Rest化。
