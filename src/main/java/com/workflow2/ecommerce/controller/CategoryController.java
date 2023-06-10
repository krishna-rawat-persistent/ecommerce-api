package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;
import com.workflow2.ecommerce.services.CategoryServiceImpl;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * This class have all the endpoints for category class
 * @author mayur_jadhav & tejas_badjate
 * @version v0.0.1
 */
@RestController
@RequestMapping("api/category")
public class CategoryController {
	@Autowired
	CategoryServiceImpl categoryService;

	/**
	 * This is the endpoint which returns all the categories from the database
	 * @return It returns all the categories form the database
	 */
	@GetMapping("/")
	public ResponseEntity<List<CategoryDTO>> getAllCategory() {
		return ResponseEntity.ok().body(categoryService.getAllCategories());
	}

	/**
	 * This endpoint returns the particular category by its name
	 * @param name It is the name of category we wanted to find
	 * @return This return a category whose name is given a path variable
	 */
	@GetMapping("/{name}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String name) {
		return  ResponseEntity.ok().body(categoryService.getCategoryById(name));
	}

	/**
	 * This is the endpoint for creating new category
	 * @param category This contains Category name inside category class object
	 * @param file This is the image of the category we wanted to create
	 * @return This returns the category we have created
	 */
	@PostMapping("/")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<CategoryDTO> save(@RequestPart("category") Category category,
									   @RequestPart("image") MultipartFile file) {
		try{
		Category category1 = Category.builder()
				.name(category.getName())
				.image(ImageUtility.compressImage(file.getBytes()))
				.build();

			return new ResponseEntity<CategoryDTO>(categoryService.saveCategory(category1), HttpStatus.CREATED);
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * This endpoint is for deleting category by its name
	 * @param name It contains the name of the category we wanted to delete
	 * @return It returns the success message in side response entity class body
	 */
	@DeleteMapping("/{name}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteById(@PathVariable String name) {
		return ResponseEntity.ok().body(categoryService.deleteCategoryById(name));
	}

	/**
	 * This endpoint delete all the category present in the database
	 * @return It returns the success message in side response entity class body
	 */
	@DeleteMapping("/")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteCategories() {
		return ResponseEntity.ok().body(categoryService.deleteAllCategories());
	}
}
