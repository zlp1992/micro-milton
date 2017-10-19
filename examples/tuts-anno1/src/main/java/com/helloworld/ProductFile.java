package com.helloworld;

public class ProductFile {
    private String name;
    private byte[] bytes;

    public ProductFile(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }
}
