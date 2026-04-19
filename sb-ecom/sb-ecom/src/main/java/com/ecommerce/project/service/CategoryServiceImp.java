package com.ecommerce.project.service;

import com.ecommerce.project.exeptions.APIExceptions;
import com.ecommerce.project.exeptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    private Long catId=1L;
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories=categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIExceptions("No Category Created till now");
        return categories;  //returns all the categories that exist in db
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIExceptions("Category with the name " + category.getCategoryName()+" already exists !!!");
        category.setCategoryId(catId++);
        categoryRepository.save(category);
    }

    //stream() converts the list into a stream pipeline so you can process elements in a functional way.
    //List → Stream → Operations → Result
    @Override
    public String deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException ("Category","categoryId", String.valueOf(categoryId)));

        categoryRepository.delete(category);
        return "Category with categoryId "+categoryId+" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIExceptions("Category with the name " + category.getCategoryName()+" already exists !!!");
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        return savedCategory;
    }
}


