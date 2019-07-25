package com.moon.order.dao;

import com.moon.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderDetailDao extends JpaRepository<OrderDetail,String>{

}
