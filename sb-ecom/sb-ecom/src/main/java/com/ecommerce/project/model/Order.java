package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private List<OrderItem> orderItems=new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDate orderDate;
    private Double totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    //multiple orders in same address
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
