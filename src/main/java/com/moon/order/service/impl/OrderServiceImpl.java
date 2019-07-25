package com.moon.order.service.impl;

import com.moon.order.dao.OrderDetailDao;
import com.moon.order.dao.OrderMasterDao;
import com.moon.order.entity.OrderMaster;
import com.moon.order.entity.dto.OrderDTO;
import com.moon.order.enums.OrderStatusEnum;
import com.moon.order.enums.PayStatusEnum;
import com.moon.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailDao detailDao;

    @Autowired
    private OrderMasterDao masterDao;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {


        // 1. 查询商品信息

        // 2. 计算总价

        // 3.商品库存

        // 4.录入数据
        /* 订单入库*/
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //TODO 目前以下的数据写死
        orderMaster.setOrderAmount(new BigDecimal(5));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());


        masterDao.save(orderMaster);


        return orderDTO;
    }
}
