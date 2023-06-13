package com.workflow2.ecommerce.repository;

import com.workflow2.ecommerce.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CategoryDaoTest {

    Category category;

    @Autowired
    CategoryDao categoryDao;

    @BeforeEach
    public void setUpForEach(){
        category = Category.builder().name("Category Name").image(null).build();
    }

    @AfterEach
    public void tearDownForEach(){
        categoryDao.deleteAll();
    }

    @Test
    public void testSave(){
        Category res = categoryDao.save(category);
        assertThat(res.getName()).isEqualTo(category.getName());
    }

    @Test
    public void testGetReferenceById(){
        categoryDao.save(category);
        Category res = categoryDao.getReferenceById(category.getName());
        assertThat(res.getName()).isEqualTo(category.getName());
    }
}