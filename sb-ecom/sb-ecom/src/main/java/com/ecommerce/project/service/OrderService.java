package com.ecommerce.project.service;

import com.ecommerce.project.model.OrderStatus;
import com.ecommerce.project.payload.OrderDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) ;
    List<OrderDTO> getOrdersByEmail(String emailId);
    List<OrderDTO> getAllOrders();
    OrderDTO updateOrderStatus(Long orderId, OrderStatus orderStatus);
}
