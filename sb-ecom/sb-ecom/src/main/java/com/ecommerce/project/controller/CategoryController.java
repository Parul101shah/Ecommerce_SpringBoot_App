package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
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
    private List<Category> categories=new ArrayList<>();

    //This maps an HTTP GET request to a method.
    @GetMapping("/api/public/categories")
    public List<Category> getAllCategories() {
        return categories;
    }
}
