package com.ecommerce.project.controller;

import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/public/categories")
    //Use case of request mapping in method level
    //GetMapping and RequestMapping are both ways to do same thing
    public ResponseEntity<CategoryResponse> getAllCategories() {
        CategoryResponse categoryResponse = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }
    // endpoints
    @PostMapping("/public/categories")
    //In most modern REST APIs, clients send JSON, so @RequestBody is almost always required for POST and PUT requests.
    //Spring must convert this JSON → Java object --->  That is what @RequestBody does.
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO)
    {
        CategoryDTO savedCategoryDTO= categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
    }

    //Response Entity---control the entire HTTP response.
    @DeleteMapping("/admin/categories/{categoryId}")
                                       //PathVariable -- Read values form URL
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId)
    {
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
    }
    //update
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId)
    {
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO,categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}
