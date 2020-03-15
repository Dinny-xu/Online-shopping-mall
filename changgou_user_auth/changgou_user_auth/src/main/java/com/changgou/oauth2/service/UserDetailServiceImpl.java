package com.changgou.oauth2.service;

import com.changgou.oauth2.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义用户信息实现类
 *
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserMapper userMapper;

    //入参: 用户名
    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.changgou.pojo.User user = (com.changgou.pojo.User) userMapper.selectByPrimaryKey(username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("brand_findAll"));
        authorities.add(new SimpleGrantedAuthority("brand_add"));

        return new User(username,user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
    }
}
