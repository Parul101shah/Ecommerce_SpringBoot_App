package com.ecommerce.project.payload;
//Request object
public class CategoryDTO {
    private Long CategoryId;
    private String CategoryName;


    public CategoryDTO(){}
    @Override
    public String toString() {
        return "CategoryDTO{" +
                "CategoryId=" + CategoryId +
                ", CategoryName='" + CategoryName + '\'' +
                '}';
    }
    public Long getCategoryId() {
        return CategoryId;
    }

    public CategoryDTO(Long categoryId, String categoryName) {
        CategoryId = categoryId;
        CategoryName = categoryName;
    }

    public void setCategoryId(Long categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
