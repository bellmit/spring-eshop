package com.example.eshop.core.catalog.application.exceptions;

import com.example.eshop.core.catalog.domain.product.Product.ProductId;

public class ProductNotFoundException extends RuntimeException {
    private final ProductId productId;

    public ProductNotFoundException(ProductId productId, String message) {
        super(message);
        this.productId = productId;
    }

    public ProductId getProductId() {
        return productId;
    }
}