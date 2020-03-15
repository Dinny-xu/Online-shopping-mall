package com;

import com.changgou.util.IdWorker;

/**
 * @author JamesXu
 * @version 1.0.0
 * @ClassName IdWorkTest.java
 * @Description TODO
 * @createTime 2020年02月25日 15:21:00
 */
public class IdWorkTest {

    public static void main(String[] args) {

        IdWorker idWorker = new IdWorker(0,0);
        for (int i = 0; i < 200; i++) {
            System.out.println(idWorker.nextId());
        }
    }

}
