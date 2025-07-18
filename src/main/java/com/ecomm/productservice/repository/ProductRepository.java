package com.ecomm.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecomm.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
