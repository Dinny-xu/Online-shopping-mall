package com.changgou;

import com.changgou.util.BCrypt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/****
 * @Author:lx
 * @Description:
 * @Date 2019/9/24 10:21
 *****/
@RunWith(SpringRunner.class)
public class BcryptTest {

    //测试加密
    @Test
    public void testBcrypt(){

        String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
        System.out.println(hashpw);
        //   $2a$10$mtla0mI1WBLAkpd9m4Ib9.yWHYka/KS8uH9ZCM5NxH/W1VT8dvpp2
        //   $2a$10$zoR9twZ6JHeAf9g/6fus3.ZmHHhCxXZi5YPwXkZXUeXGxtxzWXN4i


        boolean checkpw = BCrypt.checkpw("123456", hashpw);
        System.out.println("是否正确：" + checkpw);

    }
}
