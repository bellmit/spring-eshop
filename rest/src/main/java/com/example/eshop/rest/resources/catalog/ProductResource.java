package com.example.eshop.rest.resources.catalog;

import com.example.eshop.catalog.domain.product.Product;
import java.util.List;

public class ProductResource {
    public String id;

    public String name;

    public List<SkuResource> sku;

    public ProductResource(Product product) {
        this.id = product.getId().toString();
        this.name = product.getName();
        this.sku = product.getSku().stream().map(SkuResource::new).toList();
    }
}
