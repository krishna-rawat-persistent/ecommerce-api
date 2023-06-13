package com.workflow2.ecommerce.services;

import com.workflow2.ecommerce.dto.CategoryDTO;
import com.workflow2.ecommerce.entity.Category;
import com.workflow2.ecommerce.repository.CategoryDao;
import com.workflow2.ecommerce.util.ImageUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class CategoryServiceImplTest {

    @Mock
    CategoryDao categoryDao;

    @InjectMocks
    CategoryServiceImpl service;

    Category category;

    @BeforeAll
    static void setUpAll() throws Exception {
        mockStatic(ImageUtility.class);
        when(ImageUtility.decompressImage(any())).thenReturn(null);
    }

    @BeforeEach
    void setUp() {
        category = Category.builder().name("Category Name").image(null).build();
    }

    @AfterEach
    void tearDown() {
        category =null;
    }

    @Test
    void saveCategory() throws Exception {

        when(categoryDao.save(any())).thenReturn(category);
        CategoryDTO res = service.saveCategory(category);

        Mockito.verify(categoryDao,times(1)).save(any());
        assertThat(res.getImage()).isEqualTo(category.getImage());
    }

    @Test
    void getAllCategories() {
        Category category1 = Category.builder().name("Category Name 1").image(null).build();
        when(categoryDao.findAll()).thenReturn(Arrays.asList(category,category1)).thenReturn(null);

        List<CategoryDTO> res = service.getAllCategories();

        verify(categoryDao,times(1)).findAll();
        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getName()).isEqualTo(category.getName());
        assertThat(res.get(1).getName()).isEqualTo(category1.getName());

        List<CategoryDTO> res1 = service.getAllCategories();
        verify(categoryDao,times(2)).findAll();
        assertThat(res1).isNull();
    }

    @Test
    void deleteCategoryById() {
        doNothing().when(categoryDao).deleteById(category.getName());

        String res = service.deleteCategoryById(category.getName());

        verify(categoryDao,times(1)).deleteById(any());
        assertThat(res).isEqualTo("Successfully deleted the category");
    }

    @Test
    void updateCategoryById() throws Exception {
        when(categoryDao.getReferenceById(category.getName())).thenReturn(category);
        when(categoryDao.save(category)).thenReturn(category);

        CategoryDTO res = service.updateCategoryById(category, category.getName());

        verify(categoryDao,times(1)).getReferenceById(any());
        verify(categoryDao,times(1)).save(any());
        assertThat(res.getName()).isEqualTo(category.getName());
    }

    @Test
    void getCategoryById() {
        when(categoryDao.getReferenceById(category.getName())).thenReturn(category).thenReturn(null);

        CategoryDTO res = service.getCategoryById(category.getName());

        verify(categoryDao,times(1)).getReferenceById(any());
        assertThat(res.getName()).isEqualTo(category.getName());

        CategoryDTO res1 = service.getCategoryById(category.getName());
        verify(categoryDao,times(2)).getReferenceById(any());
        assertThat(res1).isNull();
    }

    @Test
    void deleteAllCategories() {
        doNothing().when(categoryDao).deleteAll();

        String res = service.deleteAllCategories();

        verify(categoryDao,times(1)).deleteAll();
        assertThat(res).isEqualTo("Deleted Successfully all Categories");
    }
}