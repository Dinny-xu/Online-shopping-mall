package com.changgou.order.service.impl;

import com.changgou.order.dao.OrderMapper;
import com.changgou.order.service.OrderService;
import com.changgou.pojo.Order;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param order
     */
    @Override
    public void add(Order order){
        orderMapper.insert(order);
    }


    /**
     * 修改
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Order> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Order> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Order>)orderMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Order> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Order>)orderMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 订单id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
           	}
            // 支付类型，1、在线支付、0 货到付款
            if(searchMap.get("pay_type")!=null && !"".equals(searchMap.get("pay_type"))){
                criteria.andLike("pay_type","%"+searchMap.get("pay_type")+"%");
           	}
            // 物流名称
            if(searchMap.get("shipping_name")!=null && !"".equals(searchMap.get("shipping_name"))){
                criteria.andLike("shipping_name","%"+searchMap.get("shipping_name")+"%");
           	}
            // 物流单号
            if(searchMap.get("shipping_code")!=null && !"".equals(searchMap.get("shipping_code"))){
                criteria.andLike("shipping_code","%"+searchMap.get("shipping_code")+"%");
           	}
            // 用户名称
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
           	}
            // 买家留言
            if(searchMap.get("buyer_message")!=null && !"".equals(searchMap.get("buyer_message"))){
                criteria.andLike("buyer_message","%"+searchMap.get("buyer_message")+"%");
           	}
            // 是否评价
            if(searchMap.get("buyer_rate")!=null && !"".equals(searchMap.get("buyer_rate"))){
                criteria.andLike("buyer_rate","%"+searchMap.get("buyer_rate")+"%");
           	}
            // 收货人
            if(searchMap.get("receiver_contact")!=null && !"".equals(searchMap.get("receiver_contact"))){
                criteria.andLike("receiver_contact","%"+searchMap.get("receiver_contact")+"%");
           	}
            // 收货人手机
            if(searchMap.get("receiver_mobile")!=null && !"".equals(searchMap.get("receiver_mobile"))){
                criteria.andLike("receiver_mobile","%"+searchMap.get("receiver_mobile")+"%");
           	}
            // 收货人地址
            if(searchMap.get("receiver_address")!=null && !"".equals(searchMap.get("receiver_address"))){
                criteria.andLike("receiver_address","%"+searchMap.get("receiver_address")+"%");
           	}
            // 订单来源：1:Item，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(searchMap.get("source_type")!=null && !"".equals(searchMap.get("source_type"))){
                criteria.andLike("source_type","%"+searchMap.get("source_type")+"%");
           	}
            // 交易流水号
            if(searchMap.get("transaction_id")!=null && !"".equals(searchMap.get("transaction_id"))){
                criteria.andLike("transaction_id","%"+searchMap.get("transaction_id")+"%");
           	}
            // 订单状态
            if(searchMap.get("order_status")!=null && !"".equals(searchMap.get("order_status"))){
                criteria.andLike("order_status","%"+searchMap.get("order_status")+"%");
           	}
            // 支付状态
            if(searchMap.get("pay_status")!=null && !"".equals(searchMap.get("pay_status"))){
                criteria.andLike("pay_status","%"+searchMap.get("pay_status")+"%");
           	}
            // 发货状态
            if(searchMap.get("consign_status")!=null && !"".equals(searchMap.get("consign_status"))){
                criteria.andLike("consign_status","%"+searchMap.get("consign_status")+"%");
           	}
            // 是否删除
            if(searchMap.get("is_delete")!=null && !"".equals(searchMap.get("is_delete"))){
                criteria.andLike("is_delete","%"+searchMap.get("is_delete")+"%");
           	}

            // 数量合计
            if(searchMap.get("totalNum")!=null ){
                criteria.andEqualTo("totalNum",searchMap.get("totalNum"));
            }
            // 金额合计
            if(searchMap.get("totalMoney")!=null ){
                criteria.andEqualTo("totalMoney",searchMap.get("totalMoney"));
            }
            // 优惠金额
            if(searchMap.get("preMoney")!=null ){
                criteria.andEqualTo("preMoney",searchMap.get("preMoney"));
            }
            // 邮费
            if(searchMap.get("postFee")!=null ){
                criteria.andEqualTo("postFee",searchMap.get("postFee"));
            }
            // 实付金额
            if(searchMap.get("payMoney")!=null ){
                criteria.andEqualTo("payMoney",searchMap.get("payMoney"));
            }

        }
        return example;
    }

}
