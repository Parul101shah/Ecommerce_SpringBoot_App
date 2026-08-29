package com.ecommerce.project.controller;

import com.ecommerce.project.model.OrderStatus;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthUtil authUtil;
    //place order
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO){
        String emailId =authUtil.loggedInEmail();
        OrderDTO order=orderService.placeOrder(emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage());

        return new ResponseEntity<>(order,HttpStatus.CREATED);
    }

    //Get my orders
    @GetMapping("/order/users/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        String emailId =authUtil.loggedInEmail();
        List<OrderDTO> orders=orderService.getOrdersByEmail(emailId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllAdminOrders(){
        List<OrderDTO> orders=orderService.getAllOrders();
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    @PutMapping("admin/orders/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus){
        OrderDTO dto=orderService.updateOrderStatus(orderId,orderStatus);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

}
