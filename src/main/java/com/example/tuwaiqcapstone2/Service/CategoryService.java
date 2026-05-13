package com.example.tuwaiqcapstone2.Service;

import com.example.tuwaiqcapstone2.Api.ApiException;
import com.example.tuwaiqcapstone2.Model.Category;
import com.example.tuwaiqcapstone2.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //BASIC CRUD
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void updateCategory(Integer id, Category category){
        Category oldCategory = categoryRepository.findCategoryById(id);
        if(oldCategory == null) throw new ApiException("Category not found"); //check category

        oldCategory.setName(category.getName());
        categoryRepository.save(oldCategory);
    }

    public void deleteCategory(Integer id){
        Category category = categoryRepository.findCategoryById(id);
        if(category == null) throw new ApiException("Category not found"); //check category

        categoryRepository.delete(category);
    }
}
