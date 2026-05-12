package com.ecommerce.project.payload;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private Integer quantity;
    private String description;
    private double price;
    private double discount;
    private double specialPrice;

    public ProductDTO() {
    }

    public ProductDTO(double discount, String image, String description,double price, Long productId, String productName, Integer quantity, double specialPrice) {
        this.discount = discount;
        this.image = image;
        this.price = price;
        this.productId = productId;
        this.description=description;
        this.productName = productName;
        this.quantity = quantity;
        this.specialPrice = specialPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "description='" + description + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", specialPrice=" + specialPrice +
                '}';
    }
}
