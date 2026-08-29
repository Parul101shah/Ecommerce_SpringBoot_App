package com.ecommerce.project.service;

import com.ecommerce.project.exeptions.APIExceptions;
import com.ecommerce.project.exeptions.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        //Getting User Cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart","email",emailId);
        }
        //delivery add
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));


        //Get items from the cart into the order items
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems == null || cartItems.isEmpty()){
            throw new APIExceptions("Cart is empty");
        }
        //Validate Stock availability before placing order
        for( CartItem cartItem : cartItems){
            Product product = cartItem.getProduct();
            if(product.getQuantity()==null || product.getQuantity()< cartItem.getQuantity()){
                throw new APIExceptions("Product quantity less than or equal to quantity"+product.getProductName());
            }
        }

        //Create a new order with payment info

        Order order = new Order();
        order.setEmail(emailId);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalprice());
        order.setOrderStatus(OrderStatus.ACCEPTED);

        // create and save payment
        Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment=paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        //create order items from cart items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);

        //Collect productID Before modifying cart.
        List<Long> productIds =cartItems.stream().map(item -> item.getProduct().getProductId()).toList();
        //Reducing stock with optimistic locking.
        for(CartItem item : cartItems){
            try {
                Product product=item.getProduct();
                product.setQuantity(product.getQuantity()-item.getQuantity());
                productRepository.saveAndFlush(product);
            } catch (ObjectOptimisticLockingFailureException e)
            {
                throw new APIExceptions("Product" +item.getProduct().getProductName()+ " is updated by another transaction , Please try again later ");
            }
        }

         //send back the order summary
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        //order_item dto to  order dto
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByEmail(String emailId) {
        List<Order>orders=orderRepository.findByEmail(emailId);
        return orders.stream()
                .map(order->modelMapper.map(order, OrderDTO.class)).toList();
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order>orders=orderRepository.findAll();
        return orders.stream()
                .map(order->modelMapper.map(order, OrderDTO.class)).toList();
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order=orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order","orderId", orderId));
        order.setOrderStatus(orderStatus);
        Order saved=orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }
}
