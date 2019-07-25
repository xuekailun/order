package com.moon.order.controller;

import com.moon.order.client.ProductClient;
import com.moon.order.converter.OrderFormByOrderDTOConverter;
import com.moon.order.entity.ProductInfo;
import com.moon.order.entity.dto.OrderDTO;
import com.moon.order.entity.form.OrderForm;
import com.moon.order.entity.vo.ResultVO;
import com.moon.order.enums.ResultEnum;
import com.moon.order.exception.OrderException;
import com.moon.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductClient productClient;

    /**
     * 1. 参数的校验
     * 2. 查询商品信息
     * 3. 计算总价
     * 4. 扣库存
     * 5. 订单入库
     */
    @PostMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("创建订单 参数不正确，orderForm ={}",orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        //数据转换 orderForm -> orderDTO
        OrderDTO orderDTO = OrderFormByOrderDTOConverter.convert(orderForm);

        //转换为需要进行判断购物车是否为空，因为转换前是String，现在是list
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetails())){
            log.error("【创建订单】 购物车信息为空");
            throw new OrderException(ResultEnum.CART_EMPTY);
        }

        OrderDTO result = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",result.getOrderId());

        return ResultVO.resultVO(200,"success",map);
    }

    @PostMapping("/getProductList")
    public String getProductList(@Valid @RequestBody OrderForm orderForm, BindingResult bindingResult){
        List<ProductInfo> productInfos = productClient.listForOrder(Arrays.asList("157875196366160022","157875227953464068"));
        log.info("response={}",productInfos);
        return "ok";
    }
}
