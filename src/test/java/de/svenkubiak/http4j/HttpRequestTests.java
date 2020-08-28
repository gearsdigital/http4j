package de.svenkubiak.http4j;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HttpRequestTests {
    private static final String URL = "http://127.0.0.1:5555";
    
    @BeforeAll
    public static void init() {
        new HttpTestServer().start();
    }
    
    @Test
    public void testGet() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/get")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
    
    @Test
    public void testOptions() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/options")
            .execute();
        
        assertTrue(httpResponse.getHttpStatusCode() == 405);
    }
    
    @Test
    public void testGetStatusCode() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/get/statuscode")
            .execute();
        
        assertTrue(httpResponse.getHttpStatusCode() == 203);
    }
    
    @Test
    public void testIsOk() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/get")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
        assertTrue(httpResponse.isOk());
    }
    
    @Test
    public void testIsSuccess() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/get/statuscode")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
        assertFalse(httpResponse.isOk());
    }
    
    @Test
    public void testGetHeader() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.get()
            .to(URL + "/test/get/header")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
        assertTrue(httpResponse.getHeader("x-custom-header").equals("foo"));
    }
    
    @Test
    public void testPost() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.post()
            .to(URL + "/test/post")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
    
    @Test
    public void testPut() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.put()
            .to(URL + "/test/put")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
    
    @Test
    public void testPatch() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.patch()
            .to(URL + "/test/patch")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
    
    @Test
    public void testDelete() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.delete()
            .to(URL + "/test/delete")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
    
    @Test
    public void testHead() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.head()
            .to(URL + "/test/head")
            .execute();
        
        assertTrue(httpResponse.isSuccess());
    }
}