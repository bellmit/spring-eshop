package com.example.eshop.core.catalog.application;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import com.example.eshop.core.catalog.domain.Category;
import com.example.eshop.core.catalog.domain.Category.CategoryId;
import com.example.eshop.core.catalog.domain.CategoryRepository;
import com.example.eshop.core.catalog.domain.Product;
import com.example.eshop.core.catalog.domain.Product.ProductId;
import com.example.eshop.core.catalog.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ProductCrudServiceImpl implements ProductCrudService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductCrudServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Product getProduct(ProductId productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with ID %s not found", productId)));
    }

    @Override
    @Transactional
    public Page<Product> getList(Pageable pageable) {
        return productRepository.findAll(pageable, EntityGraphs.named("Product.sku"));
    }

    @Override
    @Transactional
    public Page<Product> getForCategory(CategoryId categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId, "Category with ID " + categoryId + " not found"));

        return productRepository.findByCategory(category, pageable, EntityGraphs.named("Product.sku"));
    }
}
