package com.helloworld;



import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTest {
    public static void uploadTest() throws Exception{
        Product product=new Product("hello");
        String sUrl="http://localhost:8080/hello";
        String user="user";
        String password="password";


        URI url = URI.create( sUrl );
        HttpPut p = new HttpPut( url );

        List<NameValuePair> values=new ArrayList();
        values.add(new BasicNameValuePair("product", JSON.toJSONString(product)));
        values.add(new BasicNameValuePair("newName","newName"));
        values.add(new BasicNameValuePair("bytes","new content"));
        UrlEncodedFormEntity encodedFormEntity=new UrlEncodedFormEntity(values);
        p.setEntity(encodedFormEntity);

        HttpHost httpHost=new HttpHost("localhost",8080,"http");
        HttpContext context=createBasicAuthContext(httpHost,user,password);
        CloseableHttpClient httpClient=HttpClients.createDefault();

        HttpResponse response=httpClient.execute(httpHost,p,context);
       // HttpResponse response=httpClient.execute(p);

        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
    public static void main(String[] args) throws Exception{
        uploadTest();
    }


    private static HttpClientContext createBasicAuthContext(HttpHost host,String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials defaultCreds = new UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(AuthScope.ANY, defaultCreds);

        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        return context;
    }
}
