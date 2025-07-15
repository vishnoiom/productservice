package com.ecomm.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.ecomm.productservice.model.Product;
import com.ecomm.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(
            new Product("1", "Monitor", "Full HD Monitor", new BigDecimal("199.99"))
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Monitor", result.get(0).getName());
    }

    @Test
    void testGetProductById() {
        Product product = new Product("2", "Mouse", "Wireless Mouse", new BigDecimal("25.00"));
        when(productRepository.findById("2")).thenReturn(Optional.of(product));

        Product result = productService.getProduct("2");

        assertNotNull(result);
        assertEquals("Mouse", result.getName());
    }

    @Test
    void testSaveProduct() throws JsonProcessingException {
        Product input = new Product(null, "Keyboard", "Mechanical Keyboard", new BigDecimal("89.99"));
        Product saved = new Product("3", "Keyboard", "Mechanical Keyboard", new BigDecimal("89.99"));

        when(productRepository.save(input)).thenReturn(saved);
        when(objectMapper.writeValueAsString(saved)).thenReturn("{\"id\":\"3\",\"name\":\"Keyboard\"}");

        Product result = productService.saveProduct(input);

        assertNotNull(result);
        assertEquals("Keyboard", result.getName());
        verify(kafkaTemplate).send(eq("product-events"), anyString());
    }
}
