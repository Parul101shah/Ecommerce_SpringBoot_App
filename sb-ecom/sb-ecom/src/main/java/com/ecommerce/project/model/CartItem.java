package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity//directly maps to relational database
@Table(name="cart_items")
@Data// automatically generates getters, setters, constructors, and key object methods at compile-time
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    private  Cart cart;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double discount;
    private Double ProductPrice;
}
