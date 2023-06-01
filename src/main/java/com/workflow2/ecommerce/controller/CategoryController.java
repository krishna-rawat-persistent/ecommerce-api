package com.workflow2.ecommerce.controller;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;
import com.workflow2.ecommerce.services.CategoryService;
import com.workflow2.ecommerce.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/")
	@PreAuthorize("hasAnyRole('User','Admin')")
	public ResponseEntity<List<CategoryDTO>> getAllCategory() {
		return ResponseEntity.ok().body(categoryService.getAllCategories());
	}
	
	@GetMapping("/{name}")
	@PreAuthorize("hasAnyRole('User','Admin')")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String name) {
		return  ResponseEntity.ok().body(categoryService.getCategoryById(name));
	}
	
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
	
	@PutMapping("/{name}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable String name,@RequestPart("category") Category category,
															 @RequestPart("image") MultipartFile file) {
		CategoryDTO cat = null;
		try{
			if(category.getImage()==null) {
				Category category1 = Category.builder()
						.name(category.getName())
						.image(ImageUtility.compressImage(file.getBytes()))
						.build();
				return new ResponseEntity<>(categoryService.updateCategoryById(category1, name),HttpStatus.OK);
			} else {
				Category category1 = Category.builder()
						.name(category.getName())
						.image(ImageUtility.compressImage(category.getImage()))
						.build();
				return new ResponseEntity<>(categoryService.updateCategoryById(category1, name),HttpStatus.OK);
			}
		}catch (IOException ioe){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@DeleteMapping("/{name}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteById(@PathVariable String name) {
		return ResponseEntity.ok().body(categoryService.deleteCategoryById(name));
	}
	
	@DeleteMapping("/")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteCategories() {
		return ResponseEntity.ok().body(categoryService.deleteAllCategories());
	}
}
