package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryService {
	
	public CategoryDTO saveCategory(Category category);
	public List<CategoryDTO> getAllCategories();
	public String deleteCategoryById(String id);
	public CategoryDTO updateCategoryById(Category category, String name);
	public CategoryDTO getCategoryById(String id);
	public String deleteAllCategories(); 
		
}
