package com.ecomm.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ecomm.productservice.model.Product;
import com.ecomm.productservice.service.ProductService;
import com.ecomm.productservice.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private TokenService tokenService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product("1", "Monitor", "Full HD Monitor", new BigDecimal("199.99"));
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Monitor"))
               .andExpect(jsonPath("$[0].description").value("Full HD Monitor"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        Product product = new Product("2", "Mouse", "Wireless Mouse", new BigDecimal("25.00"));
        when(productService.getProduct("2")).thenReturn(product);

        mockMvc.perform(get("/products/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Mouse"))
               .andExpect(jsonPath("$.price").value(25.00));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProduct("999")).thenReturn(null);

        mockMvc.perform(get("/products/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct_ValidToken() throws Exception {
        Product input = new Product(null, "Keyboard", "Gaming Keyboard", new BigDecimal("89.99"));
        Product saved = new Product("3", "Keyboard", "Gaming Keyboard", new BigDecimal("89.99"));

        when(tokenService.validateToken("Bearer valid-token", "STAFF")).thenReturn("user-phone");
        when(productService.saveProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer valid-token")
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    @Test
    void testCreateProduct_InvalidToken() throws Exception {
        Product input = new Product(null, "Speaker", "Bluetooth Speaker", new BigDecimal("59.99"));

        when(tokenService.validateToken("Bearer bad-token", "STAFF"))
            .thenThrow(new WebClientResponseException(401, "Unauthorized", null, null, null));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer bad-token")
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isUnauthorized());
    }
}
