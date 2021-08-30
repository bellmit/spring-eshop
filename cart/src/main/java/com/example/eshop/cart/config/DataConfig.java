package com.example.eshop.cart.config;

import com.example.eshop.sharedkernel.infrastructure.dal.SimpleNaturalIdRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration("cartDataConfig")
@EnableJpaRepositories(
        basePackages = "com.example.eshop.cart",
        repositoryBaseClass = SimpleNaturalIdRepositoryImpl.class
)
@EntityScan(basePackages = "com.example.eshop.cart")
public class DataConfig {
}
