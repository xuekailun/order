package com.moon.order.client;

import com.moon.order.dao.dto.CarDTO;
import com.moon.order.entity.ProductInfo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PRODUCT")
public interface ProductClient {

    @GetMapping("/listForOrder")
    List<ProductInfo> listForOrder(@RequestParam(value = "productIdList",required = false) List<String> productIdList);

    /* 扣库存*/
    @PostMapping("/decreaseStock")
    void decreaseStock(@RequestBody List<CarDTO> carDTOS);
}
