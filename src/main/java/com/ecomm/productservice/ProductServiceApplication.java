package com.ecomm.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ProductServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
