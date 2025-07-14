package com.ecomm.productservice.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.productservice.model.Product;
import com.ecomm.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.core.KafkaTemplate;

@Service
public class ProductService {
	@Autowired
    private ProductRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOPIC = "product-events";

    //@HystrixCommand(fallbackMethod = "defaultProducts")
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) throws JsonProcessingException {
        Product saved = repository.save(product);
        String json = objectMapper.writeValueAsString(saved);
        kafkaTemplate.send(TOPIC, json);
        return saved;
    }

    public List<Product> defaultProducts() {
        return Collections.emptyList();
    }


}
