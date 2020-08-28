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
    
    /**
     * @return The Http status of the request
     */
    public int getHttpStatusCode() {
        return httpResponse.statusCode();
    }

    /**
     * @return The body of the Http request
     */
    public String getBody() {
        return httpResponse.body();        
    }

    /**
     * @return True if the Http status code is any of 2xx Success
     */
    public boolean isSuccessful() {
        return String.valueOf(httpResponse.statusCode()).startsWith("2");
    }
    
    /**
     * @return True if the Http status code is 200
     */
    public boolean isOk() {
        return httpResponse.statusCode() == 200;
    }

    /**
     * The value of a given header or null if value not found
     * 
     * @param name The header to retrieve
     * @return The value of the Header or null
     */
    public String getHeader(String name) {
        Objects.requireNonNull(name, "name can not be null");
        
        return httpResponse.headers().firstValue(name).orElse("");
    }

    /**
     * Converts the JSON String body into a given Object class
     * 
     * @param <T> ignored
     * @param clazz The class to convert the JSON body to
     * @return The converted object class
     * 
     * @throws HttpRequestException when the conversion fails
     */
    public <T> T getJsonBodyAs(Class<T> clazz) throws HttpRequestException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        T value = null;
        try {
            value = objectMapper.readValue(httpResponse.body(), clazz);
        } catch (IOException e) {
            throw new HttpRequestException("Failed to convert body to JSON object", e);
        }
        
        return value;
    }
}