package com.ecommerce.project.payload;

import java.util.List;

public class ProductResponse {
    private List<ProductDTO> content;

    public ProductResponse() {
    }

    public ProductResponse(List<ProductDTO> content) {
        this.content = content;
    }

    public List<ProductDTO> getContent() {
        return content;
    }

    public void setContent(List<ProductDTO> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "content=" + content +
                '}';
    }
}
