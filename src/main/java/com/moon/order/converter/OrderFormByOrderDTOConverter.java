package com.moon.order.converter;

import com.alibaba.fastjson.JSONObject;
import com.moon.order.entity.OrderDetail;
import com.moon.order.entity.dto.OrderDTO;
import com.moon.order.entity.form.OrderForm;
import com.moon.order.enums.ResultEnum;
import com.moon.order.exception.OrderException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/***
 * 将OrderForm 转换成 OrderDTO
 */
@Slf4j
public class OrderFormByOrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        //转List
        List<OrderDetail> orderDetails = new ArrayList<>();
        try {
           orderDetails = JSONObject.parseArray(orderForm.getItems(),OrderDetail.class);
        }catch (Exception e){
            log.error("【json转换】错误，String={}",orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetails(orderDetails);

        return orderDTO;
    }
}
