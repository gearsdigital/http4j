package de.svenkubiak.http4j;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author svenkubiak
 *
 */
public class HttpResponse {
    private java.net.http.HttpResponse<String> httpResponse;

    public HttpResponse(java.net.http.HttpResponse<String> httpRepsonse) {
        this.httpResponse = Objects.requireNonNull(httpRepsonse, "httpRepsonse can not be null");
    }
    
    public int getHttpStatusCode() {
        return httpResponse.statusCode();
    }

    public String getBody() {
        return httpResponse.body().toString();        
    }

    public boolean isSuccessful() {
        return httpResponse.statusCode() == 200;
    }

    public String getHeader(String name) {
        Objects.requireNonNull(name, "name can not be null");
        
        return httpResponse.headers().firstValue(name).get();
    }

    public <T> T getJsonBodyAs(Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        T value = null;
        try {
            value = objectMapper.readValue(httpResponse.body().toString(), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return value;
    }
}