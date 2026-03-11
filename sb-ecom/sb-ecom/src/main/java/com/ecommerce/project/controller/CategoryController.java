package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    //Response Entity---control the entire HTTP response.
    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<@Nullable String> deleteCategory(@PathVariable Long categoryId)
    {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

}
