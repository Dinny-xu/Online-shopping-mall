/*
package com.example.canatest.config;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

*/
/**
 * @author chen.qian
 * @date 2018/3/19
 *//*

@CanalEventListener
public class MyEventListener {

   */
/* @InsertListenPoint
    public void onEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList().forEach((c) -> System.err.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    @UpdateListenPoint
    public void onEvent1(CanalEntry.RowData rowData) {
        System.err.println("UpdateListenPoint");
        rowData.getAfterColumnsList().forEach((c) -> System.err.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    @DeleteListenPoint
    public void onEvent3(CanalEntry.EventType eventType) {
        System.err.println("DeleteListenPoint");
    }*//*


    @ListenPoint(destination = "example", schema = "changgou_business",
            table = {"tb_ad"}, eventType = {CanalEntry.EventType.UPDATE,
                                            CanalEntry.EventType.INSERT,
                                            CanalEntry.EventType.DELETE})
    public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告数据发生变化");

        rowData.getAfterColumnsList().forEach((c) ->
                System.err.println("tb_ad: " + c.getName() + " ::   " + c.getValue()));
    }
}
*/
