package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService{
    private List<Category> categories=new ArrayList<>();
    private Long catId=1L;
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(catId++);
        categories.add(category);
    }

    //stream() converts the list into a stream pipeline so you can process elements in a functional way.
    //List → Stream → Operations → Result
    @Override
    public String deleteCategory(Long categoryId) {
        Category category=categories.stream().filter(c->c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
        if(category==null)
            return "Category Not found";
        categories.remove(category);
        return "Category with categoryId "+categoryId+" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory=categories.stream().filter(c->c.getCategoryId().equals(categoryId))
                .findFirst();
        if(optionalCategory.isPresent()) {
            Category existingCtegory=optionalCategory.get();
            existingCtegory.setCategoryName(category.getCategoryName());
            return existingCtegory;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }
}


