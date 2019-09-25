package com.cyl.security.oauth2.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author cyl
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  @Qualifier("userDetailsServiceBean")
  private UserDetailsService userDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
      .withClient("client")
      .secret("{noop}secret")
      .scopes("all")
      //指定refresh token的有效时间（单位：秒）
      //.refreshTokenValiditySeconds(60)
      //指定access token的有效时间（单位：秒）
      //.accessTokenValiditySeconds(5)
      //设置授权模式
      .authorizedGrantTypes("password", "refresh_token");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //注入AuthenticationManager从而启用password的授权模式
    endpoints.authenticationManager(authenticationManager);

    endpoints
      //在使用jwt的情况下：false会使得refresh_token的信息完全改变（ati，exp等），true(默认值)-仅仅改变ati对应的access_token的值同时会使得编码后的refresh_token看起来有点不一样
      //.reuseRefreshTokens(false)
      //使用refresh_token需要注入UserDetailService
      .userDetailsService(userDetailsService)
      .tokenStore(tokenStore())
      .accessTokenConverter(converter());
  }

  public JwtTokenStore tokenStore() {
    JwtTokenStore tokenStore = new JwtTokenStore(converter());
    return tokenStore;
  }

  @Bean
  public JwtAccessTokenConverter converter() {
    JwtAccessTokenConverter converter = new CustomTokenEnhancer();
    converter.setSigningKey("123");
    return converter;
  }


}
