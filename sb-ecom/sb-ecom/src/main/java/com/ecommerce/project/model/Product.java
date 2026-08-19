package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    private String productName;
    private String image;

    @NotBlank
    @Size(min = 6, message = "Product description must contain atleast 6 characters")
    private String description;
    private Integer quantity;
    private double price;//100
    private double discount;//25
    private double specialPrice;//75

    // 100 - (25 *100)*100 --> discount

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product",cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<CartItem> products=new ArrayList<>();

   //-----------------------------------------------------------
    public Product() {
    }

    public Product(String description, double price,String image, double discount,Long productId, String productName, Integer quantity, double specialPrice) {
        this.description = description;
        this.price = price;
        this.image=image;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.discount=discount;
        this.specialPrice = specialPrice;
    }


}
