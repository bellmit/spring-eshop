package com.example.eshop.core.catalog.application;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DBRider
class CategoryCrudServiceIntegrationTest {
    private static final UUID PARENT_CATEGORY_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID NOT_EXISTING_CATEGORY_ID = UUID.fromString("12345678-1234-1234-1234-123456789012");

    @Autowired
    CategoryCrudService categoryCrudService;

    @Test
    @DataSet("categories.yml")
    void shouldFindCategoryById() {
        var category = categoryCrudService.getCategory(PARENT_CATEGORY_ID);

        assertAll(
                () -> assertThat(category.id()).isEqualTo(PARENT_CATEGORY_ID),
                () -> assertThat(category.getName()).isEqualTo("parent")
        );
    }

    @Test
    @DataSet("categories.yml")
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        assertThatThrownBy(() -> categoryCrudService.getCategory(NOT_EXISTING_CATEGORY_ID))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DataSet("categories.yml")
    void shouldFindAllCategories() {
        var categories = categoryCrudService.getAll();

        assertThat(categories).hasSize(4);
    }
}
