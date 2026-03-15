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
//Use case of request mapping in class level
@RequestMapping("/api")
public class CategoryController {
    //need object of CategoryService
    @Autowired //(Field Injection)
    private CategoryService categoryService;


//    This is constructor injection
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    //This maps an HTTP GET request to a method.
    //@GetMapping("/public/categories")
    @RequestMapping(value = "/public/categories",method = RequestMethod.GET)
    //Use case of request mapping in method level
    //GetMapping and RequestMapping are both ways to do same thing
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }
    // endpoints
    @PostMapping("/public/categories")
    //In most modern REST APIs, clients send JSON, so @RequestBody is almost always required for POST and PUT requests.
    //Spring must convert this JSON → Java object --->  That is what @RequestBody does.
    public ResponseEntity<String> createCategory(@RequestBody Category category)
    {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Added Successfully",HttpStatus.CREATED);
    }

    //Response Entity---control the entire HTTP response.
    @DeleteMapping("/admin/categories/{categoryId}")
                                       //PathVariable -- Read values form URL
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
    //update
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId)
    {
        try{
            Category savedCategory = categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>("Category with Category Id :"+ categoryId, HttpStatus.OK);
        }catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }
}
