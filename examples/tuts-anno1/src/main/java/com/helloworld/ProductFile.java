package com.helloworld;

public class ProductFile {
    private String name;
    private String filePath;
    private long size;

    public ProductFile(String name, String filePath) {
        this.name = name;
        this.filePath=filePath;
    }

    public String getName() {
        return name;
    }

    public String getPath(){
        return filePath;
    }
}
