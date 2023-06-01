package com.workflow2.ecommerce.services.impl;


import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;
import com.workflow2.ecommerce.repository.CategoryRepo;
import com.workflow2.ecommerce.services.CategoryService;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryRepo categoryRepo;
	@Override
	public CategoryDTO saveCategory(Category category) {
		Category category1 = categoryRepo.save(category);
		return CategoryDTO.builder().name(category1.getName()).image(ImageUtility.decompressImage(category1.getImage())).build();
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		try {
			List<Category> categories = categoryRepo.findAll();
			if (categories == null) {
				return null;
			}
			List<CategoryDTO> decompressCats = new ArrayList<>();
			for (Category cat : categories) {
				cat.setImage(ImageUtility.decompressImage(cat.getImage()));
				CategoryDTO prod = CategoryDTO.builder()
						.name(cat.getName())
						.image(cat.getImage())
						.build();
				decompressCats.add(prod);
			}
			return decompressCats;
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public String deleteCategoryById(String name) {
		
		categoryRepo.deleteById(name);
		return "Successfully deleted the category";
	}

	@Override
	public CategoryDTO updateCategoryById(Category category, String name) {
			final Category cat = categoryRepo.getReferenceById(name);
			categoryRepo.save(category);
			category.setImage(ImageUtility.decompressImage(cat.getImage()));
			return CategoryDTO.builder().name(category.getName()).image(category.getImage()).build();

	}

	@Override
	public CategoryDTO getCategoryById(String name) {
			final Category cat = categoryRepo.getReferenceById(name);
			return CategoryDTO.builder().name(cat.getName()).image(ImageUtility.decompressImage(cat.getImage())).build();

	}

	@Override
	public  String deleteAllCategories() {
		categoryRepo.deleteAll();
		return "Deleted Successfully all Categories";
	}

	
}

