package com.moon.order.service.impl;

import com.moon.order.client.ProductClient;
import com.moon.order.dao.OrderDetailDao;
import com.moon.order.dao.OrderMasterDao;
import com.moon.order.dao.dto.CarDTO;
import com.moon.order.entity.OrderDetail;
import com.moon.order.entity.OrderMaster;
import com.moon.order.entity.ProductInfo;
import com.moon.order.entity.dto.OrderDTO;
import com.moon.order.enums.OrderStatusEnum;
import com.moon.order.enums.PayStatusEnum;
import com.moon.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailDao detailDao;

    @Autowired
    private OrderMasterDao masterDao;

    @Autowired
    private ProductClient productClient;

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = UUID.randomUUID().toString();

        // 1. 查询商品信息
        List<String> productIdList = orderDTO.getOrderDetails().stream().map(OrderDetail::getProductId)
                .collect(Collectors.toList());

        // 获取商品列表
        List<ProductInfo> productInfos = productClient.listForOrder(productIdList);

        // 2. 计算总价
        BigDecimal orderAmout = new BigDecimal(0);
        //获取商品详情
        for(OrderDetail orderDetail : orderDTO.getOrderDetails()){
            String orderProductId = orderDetail.getProductId();
            for(ProductInfo productInfo : productInfos){
                String productProductId = productInfo.getProductId();
                // 如果两个id相等
                if(productProductId.equals(orderProductId)){
                    // 总价 = 单价 * 数量
                    orderAmout = orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout); // add 代表就是累积
                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(UUID.randomUUID().toString());

                    //订单详情入库
                    detailDao.save(orderDetail);
                }
            }
        }
        // 3.商品库存
        List<CarDTO> carDTOList = orderDTO.getOrderDetails().stream().map(e -> new CarDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(carDTOList);

        // 4.录入数据
        /* 订单入库*/
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());


        masterDao.save(orderMaster);


        return orderDTO;
    }
}
