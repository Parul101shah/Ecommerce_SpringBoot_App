package com.ecommerce.project.service;

import com.ecommerce.project.exeptions.APIExceptions;
import com.ecommerce.project.exeptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    private Long catId=1L;
    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories=categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIExceptions("No Category Created till now");
        //Model mapping
        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class)).toList();

        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;  //returns all the categories that exist in db
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category= modelMapper.map(categoryDTO,Category.class);
        Category savedFromDb=categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedFromDb != null)
            throw new APIExceptions("Category with the name " + category.getCategoryName()+" already exists !!!");
        category.setCategoryId(catId++);

        Category savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    //stream() converts the list into a stream pipeline so you can process elements in a functional way.
    //List → Stream → Operations → Result
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException ("Category","categoryId", String.valueOf(categoryId)));

        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException ("Category","categoryId", String.valueOf(categoryId)));
        Category category =modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }
}


