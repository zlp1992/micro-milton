package com.helloworld;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String name;
    private List<ProductFile> productFiles = new ArrayList<ProductFile>();

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<ProductFile> getProductFiles() {
        return productFiles;
    }
}
