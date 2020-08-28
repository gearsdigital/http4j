package de.svenkubiak.http4j.enums;

/**
 * 
 * @author sven.kubiak
 *
 */
public enum HttpMethod {
    DELETE("DELETE"),
    GET("GET"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PATCH("PATCH"),
    POST("POST"),
    PUT("PUT");

    private final String value;

    HttpMethod (String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}