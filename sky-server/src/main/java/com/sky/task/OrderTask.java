package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;


    /**
     * 支付超时，订单自动取消
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理支付超时订单：{}", new Date());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        List<Orders> orders = ordersMapper.getByStatusAndOrdertimeLT(Orders.PENDING_PAYMENT, time);

        if (orders != null && orders.size() >0){
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("支付超时，订单自动取消");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.updateById(order);
            }
        }
    }

    /**
     * 处理“派送中”状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中订单：{}", new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = ordersMapper.getByStatusAndOrdertimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (orders!= null && orders.size()>0){
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                ordersMapper.updateById(order);
            }
        }

    }

}
