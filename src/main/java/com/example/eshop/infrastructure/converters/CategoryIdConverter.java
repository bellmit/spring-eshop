package com.example.eshop.infrastructure.converters;

import com.example.eshop.core.catalog.domain.category.Category.CategoryId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryIdConverter implements Converter<String, CategoryId> {
    @Override
    @Nullable
    public CategoryId convert(String source) {
        return new CategoryId(Long.valueOf(source));
    }
}
