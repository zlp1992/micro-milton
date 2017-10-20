/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.helloworld;

import io.milton.annotations.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@ResourceController
public class HelloWorldController  {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HelloWorldController.class);

    private List<Product> products = new ArrayList<Product>();

    public HelloWorldController() {
        products.add(new Product("hello"));
        products.add(new Product("world"));
    }
            
    @Root
    public HelloWorldController getRoot() {
        return this;
    }
    @ChildrenOf
    public List<Product> getProducts(HelloWorldController root) {
        return products;
    }

    @ChildrenOf
    public List<ProductFile> getProductFiles(Product product) {
        return product.getProductFiles();
    }

    @PutChild
    public ProductFile upload(Product product,String name, byte[] bytes) throws Exception{
        String fileName=name;
        File newFile=new File(fileName);
        if(!newFile.exists()){
            newFile.setWritable(true,false);
            newFile.createNewFile();
        }
        ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
        FileUtils.copyInputStreamToFile(inputStream,newFile);
        String path=newFile.getAbsolutePath();
        System.out.println("file create: name="+fileName+", path="+path);
        ProductFile productFile=new ProductFile(fileName,path);
        product.getProductFiles().add(productFile);
        return productFile;
    }
//    @PutChild
//    public ProductFile upload(Product product, String newName, InputStream inputStream) {
//        System.out.println("upload inputStream called");
//        ProductFile pf = new ProductFile("stream", "inputStream".getBytes());
//        product.getProductFiles().add(pf);
//        return pf;
//    }
    @Get
    public void download(ProductFile file, OutputStream outputStream){
        System.out.println("download called");
        if(outputStream!=null){
            try{
                File exist=new File(file.getPath());
                if(exist.exists()){
                    FileUtils.copyFile(exist,outputStream);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    outputStream.close();
                }catch (Exception e){}
            }
        }
    }

}
