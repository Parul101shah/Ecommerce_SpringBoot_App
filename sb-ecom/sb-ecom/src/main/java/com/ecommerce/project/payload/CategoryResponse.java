package com.ecommerce.project.payload;
import java.util.List;

//Resopnse obj
public class CategoryResponse {
    private List<CategoryDTO> content;

    public CategoryResponse() {
    }

    public CategoryResponse(List<CategoryDTO> content) {
        this.content = content;
    }

    public List<CategoryDTO> getContent() {
        return content;
    }

    public void setContent(List<CategoryDTO> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "content=" + content +
                '}';
    }
}
