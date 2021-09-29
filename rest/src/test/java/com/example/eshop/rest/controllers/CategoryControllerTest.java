package com.example.eshop.rest.controllers;

import com.example.eshop.catalog.application.category.CategoryCrudService;
import com.example.eshop.catalog.application.category.CategoryNotFoundException;
import com.example.eshop.catalog.application.product.ProductCrudService;
import com.example.eshop.catalog.domain.category.Category;
import com.example.eshop.catalog.domain.category.Category.CategoryId;
import com.example.eshop.rest.config.MappersConfig;
import com.example.eshop.rest.mappers.CategoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
@Import(MappersConfig.class)
class CategoryControllerTest {
    private Category parent;
    private Category child1;
    private Category child2;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryCrudService categoryCrudService;
    @MockBean
    private ProductCrudService productCrudService;

    @BeforeEach
    void setUp() {
        parent = Category.builder().id(new CategoryId("1")).name("parent").build();
        child1 = Category.builder().id(new CategoryId("2")).name("child1").parent(parent).build();
        child2 = Category.builder().id(new CategoryId("3")).name("child2").parent(parent).build();

        parent.addChild(child1);
        parent.addChild(child2);
    }

    @Nested
    class GetById {
        @Test
        void givenGetByIdRequest_whenCategoryExists_thenReturnOk() throws Exception {
            var category = parent;
            when(categoryCrudService.getCategory(category.getId())).thenReturn(category);

            var expectedJson = objectMapper.writeValueAsString(categoryMapper.toCategoryDto(category));

            mockMvc.perform(get("/api/categories/" + category.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));

            verify(categoryCrudService).getCategory(category.getId());
        }

        @Test
        void givenGetByIdRequest_whenCategoryDoesNotExist_thenReturn404() throws Exception {
            var id = new CategoryId("1");
            when(categoryCrudService.getCategory(id)).thenThrow(new CategoryNotFoundException(id, ""));

            assert404("/api/categories/" + id, id);

            verify(categoryCrudService).getCategory(id);
        }
    }

    @Nested
    class GetList {
        @Test
        void whenGetListRequest_thenReturnAllCategories() throws Exception {
            var categories = List.of(parent, child1, child2);
            when(categoryCrudService.getAll()).thenReturn(categories);

            var expectedJson = objectMapper.writeValueAsString(categoryMapper.toCategoryDtoList(categories));

            mockMvc.perform(get("/api/categories"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }
    }

    @Nested
    class GetTree {
        @Test
        void whenGetTreeRequest_thenReturnOk() throws Exception {
            var tree = List.of(parent);
            when(categoryCrudService.getTree()).thenReturn(tree);

            var expectedJson = objectMapper.writeValueAsString(categoryMapper.toTree(tree));

            mockMvc.perform(get("/api/categories/tree"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }
    }

    @Nested
    class GetProducts {
        @Test
        void givenGetProductsRequest_whenCategoryDoesNotExist_thenReturn404() throws Exception {
            var id = new CategoryId("1");
            var perPage = 5;
            var pageable = PageRequest.of(0, perPage);
            when(productCrudService.getForCategory(id, pageable)).thenThrow(new CategoryNotFoundException(id, ""));

            assert404("/api/categories/" + id + "/products/?per_page=" + perPage, id);

            verify(productCrudService).getForCategory(id, pageable);
        }
    }

    private void assert404(String url, CategoryId id) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                            {
                                status: 404,
                                detail: "Category %s not found"
                            }
                            """.formatted(id)
                ));
    }
}