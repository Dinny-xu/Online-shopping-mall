package com.changgou.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 核心配置文件
 * //校验 消费方(用户)信息
 * //   对消费者(用户)请求 判断 有些请求是放行的 有些请求是必须拦截的
 *
 */
@Configuration
@EnableWebSecurity  //开启安全框架功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 查询用户信息 及权限的 自定义实现类 可连接数据库 查询数据库中的用户信息 及授权信息
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     *    目的：判断用户名密码与数据库中的是否一致  默认使用的加密的密码
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService);
    }

    /**
     *  设置不需要拦截的请求
     *  包含静态资源
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/user/login",
                "/user/logout",
                "/oauth/check_token");
    }

    /**
     * 配置必须拦截的请求
     * 配置登录表单
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //验证所有请求 唯一不开启csrf 跨域请求伪造
        http.csrf().disable()
                .httpBasic()        //启用Http基本身份验证
                .and()
                .formLogin()       //启用表单身份验证
                //.loginPage("/login") //启用自定义登录页面
                //.loginProcessingUrl("/login") //启用自定义提交请求
                //.failureForwardUrl("/login.html") //启用自定义登录失败后转发页面  这里采用继继续登录
                //.usernameParameter("username") //启用登录表单用户名称  默认是username
                //.passwordParameter("password") //启用登录表单密码名称  默认是password
                .and()
                .authorizeRequests()    //限制基于Request请求访问
                .anyRequest()           //任何请求
                .authenticated();       //都需要经过验证
    }


    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);
    }

}
