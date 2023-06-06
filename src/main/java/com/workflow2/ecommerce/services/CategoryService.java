package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;

import java.util.List;

/**
 * This Interface contains all the methods to perform operations category class
 * @author Mayur_Jadhav
 * @version v0.0.1
 */
public interface CategoryService {
	
	 CategoryDTO saveCategory(Category category) throws Exception;
	 List<CategoryDTO> getAllCategories();
	 String deleteCategoryById(String id);
	 CategoryDTO updateCategoryById(Category category, String name) throws Exception;
	 CategoryDTO getCategoryById(String id);
	 String deleteAllCategories();
		
}
