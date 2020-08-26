package de.svenkubiak.http4j;

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
        HttpResponse httpResponse = HttpRequest.GET()
            .to(URL + "/test/get")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
    
    @Test
    public void testGetStatusCode() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.GET()
            .to(URL + "/test/get/statuscode")
            .execute();
        
        assertTrue(httpResponse.getHttpStatusCode() == 203);
    }
    
    @Test
    public void testGetHeader() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.GET()
            .to(URL + "/test/get/header")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
        assertTrue(httpResponse.getHeader("x-custom-header").equals("foo"));
    }
    
    @Test
    public void testPost() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.POST()
            .to(URL + "/test/post")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
    
    @Test
    public void testPut() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.PUT()
            .to(URL + "/test/put")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
    
    @Test
    public void testPatch() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.PATCH()
            .to(URL + "/test/patch")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
    
    @Test
    public void testDelete() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.DELETE()
            .to(URL + "/test/delete")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
    
    @Test
    public void testHead() throws HttpRequestException {
        HttpResponse httpResponse = HttpRequest.HEAD()
            .to(URL + "/test/head")
            .execute();
        
        assertTrue(httpResponse.isSuccessful());
    }
}