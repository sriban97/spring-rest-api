package com.green.springrestapi.controller;

import com.green.springrestapi.entity.Category;
import com.green.springrestapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/save")
    public Category save(@RequestBody Category category){
        return categoryRepository.save(category);
    }

    @GetMapping("/findAll")
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

}
