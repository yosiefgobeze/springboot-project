package com.samsun.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.samsun.bookstore.catalog.TestcontainersConfiguration;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {
            //                "spring.flyway.enabled=false",
            "spring.test.database.replace=none",
            //                "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
        })
@Sql("/test-data.sql")
// I added below because the properties was not working
@Import(TestcontainersConfiguration.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertThat(products).hasSize(20); // match your SQL data
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P105").orElseThrow();
        assertThat(product.getCode()).isEqualTo("P105");
        assertThat(product.getName()).isEqualTo("The Hobbit");
        assertThat(product.getDescription()).isEqualTo("A fantasy adventure about Bilbo Baggins.");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("13.75"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExist() {
        // Since the productRepository.findByCode(String code) returns Optional, verify Optional.isEmpty()
        assertThat(productRepository.findByCode("invalid_product_code")).isEmpty();
    }
}
