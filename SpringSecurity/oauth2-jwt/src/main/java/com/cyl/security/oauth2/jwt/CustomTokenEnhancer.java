package com.cyl.security.oauth2.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT添加定义信息
 */
public class CustomTokenEnhancer extends JwtAccessTokenConverter {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> info = new LinkedHashMap<String, Object>(
      accessToken.getAdditionalInformation());
    info.put("domain", "com.cyl");
    info.put("username", authentication.getName());
    DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
    customAccessToken.setAdditionalInformation(info);
    return super.enhance(customAccessToken, authentication);
  }
}
