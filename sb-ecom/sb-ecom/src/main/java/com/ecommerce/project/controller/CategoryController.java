package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//tells Spring that This class will handle HTTP API requests
//This class becomes an API controller.
/*What Spring does internally
Spring Boot automatically:
1.Creates an object of this class.
2.Registers it to handle HTTP requests.
3.Converts returned objects into JSON response.
*/
@RestController
public class CategoryController {
    //need object of CategoryService
    @Autowired //(Field Injection)
    private CategoryService categoryService;


//    This is constructor injection
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    //This maps an HTTP GET request to a method.
    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
    // endpoints
    @PostMapping("/api/public/categories")
    //In most modern REST APIs, clients send JSON, so @RequestBody is almost always required for POST and PUT requests.
    //Spring must convert this JSON → Java object --->  That is what @RequestBody does.
    public String createCategory(@RequestBody Category category)
    {
        categoryService.createCategory(category);
        return "Added Successfully";
    }

}
