package com.ecomm.productservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ecomm.productservice.model.Product;
import com.ecomm.productservice.service.ProductService;
import com.ecomm.productservice.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
    private ProductService productService;
	
	@Autowired
    TokenService tokenService;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	final String userType="STAFF";

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        Product product = productService.getProduct(id);
        if (product == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody	 Product product, @RequestHeader("Authorization") String tokenValue) throws JsonProcessingException {
    	String phone = null;
    	try
        {
            phone =  tokenService.validateToken(tokenValue, userType);
        }
        catch (WebClientResponseException e)
        {
            logger.info("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body(e.getResponseBodyAsString());
        }

    	Product savedProduct= productService.saveProduct(product);
		return ResponseEntity.status(201).body(savedProduct);
    	
    }


}
