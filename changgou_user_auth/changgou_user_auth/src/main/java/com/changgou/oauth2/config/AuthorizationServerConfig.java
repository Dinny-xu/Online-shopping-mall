package com.changgou.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;


/**
 * oauth2 核心配置文件
 */
@Configuration
@EnableAuthorizationServer  //开启认证服务
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;



    /**
     *授权服务的安全配置
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
               // .passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 畅购用户中心
     * 校验 申购应用是否 oauth_client_details 是否有此用户
     * 方式:   数据库(持久化保存用户信息)
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     *encrypt:
     key-store:
     alias: changgou  #别名
     location: changgou.jks  #KeyStore 证书库名称
     password: changgou   #证书库密码
     secret: changgou   #秘钥
     */
    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    /**
     * 生成使用RSA方式加密的 Jwt令牌
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){


        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(
                        keyProperties.getKeyStore().getLocation(),
                        keyProperties.getKeyStore().getPassword().toCharArray());
        //钥匙对
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(
                keyProperties.getKeyStore().getAlias(),
                keyProperties.getKeyStore().getSecret().toCharArray());

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }


    /**
     * 令牌保存
     * @return
     */
/*    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
        //return new JwtTokenStore(jwtAccessTokenConverter());
    }*/

    /**
     * 端点设置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.tokenStore(tokenStore()) //保存令牌
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService) //校验 消费者信息 并授权
                .authenticationManager(authenticationManager);//认证管理器

    }


}
