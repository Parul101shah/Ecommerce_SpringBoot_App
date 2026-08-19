package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity//directly maps to relational database
@Table(name="carts")
@Data// automatically generates getters, setters, constructors, and key object methods at compile-time
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment feature
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //one cart can have many cart items
    @OneToMany(mappedBy = "cart",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<CartItem> cartItems=new ArrayList<>();

    private double totalprice=0.0;
}
