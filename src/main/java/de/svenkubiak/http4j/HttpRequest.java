package de.svenkubiak.http4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author svenkubiak
 *
 */
public class HttpRequest {
    private java.net.http.HttpClient.Builder httpClientBuilder = java.net.http.HttpClient.newBuilder();
    private java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
    private Map<String, String> formAttributes = new HashMap<>();
    private String method;
    
    public HttpRequest(String method, BodyPublisher bodyPublisher) {
        this.method = method;
        
        httpRequestBuilder
            .method(method, bodyPublisher)
            .version(Version.HTTP_2)
            .timeout(Duration.ofSeconds(5));
        
        httpClientBuilder.followRedirects(java.net.http.HttpClient.Redirect.ALWAYS)
            .version(Version.HTTP_2);
    }
    
    public static HttpRequest GET() {
        return new HttpRequest("GET", BodyPublishers.noBody());
    }
    
    public static HttpRequest POST() {
        return new HttpRequest("POST", BodyPublishers.noBody());
    }
    
    public static HttpRequest PUT() {
        return new HttpRequest("PUT", BodyPublishers.noBody());
    }
    
    public static HttpRequest DELETE() {
        return new HttpRequest("DELETE", BodyPublishers.noBody());
    }
    
    public static HttpRequest HEAD() {
        return new HttpRequest("HEAD", BodyPublishers.noBody());
    }
    
    public static HttpRequest PATCH() {
        return new HttpRequest("PATCH", BodyPublishers.noBody());
    }

    public HttpRequest to(String url) {
        Objects.requireNonNull(url, "url can not be null");
        httpRequestBuilder.uri(URI.create(url));
        
        return this;
    }

    public HttpRequest disableRedirects() {
        httpClientBuilder.followRedirects(java.net.http.HttpClient.Redirect.NEVER);
        
        return this;
    }

    public HttpRequest withFormAttribute(String key, String value) {
        Objects.requireNonNull(key, "key can not be null");
        this.formAttributes.put(key, value);
        
        httpRequestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        
        return this;
    }

    public HttpRequest withBody(String body) {
        httpRequestBuilder.method(method, BodyPublishers.ofString(body));        
        
        return this;
    }

    public HttpRequest withHeader(String name, String value) {
        Objects.requireNonNull(name, "name can not be null");
        httpRequestBuilder.header(name, value);
        
        return null;
    }

    public HttpRequest withBasicAuthentication(String username, String password) {
        Objects.requireNonNull(username, "username can not be null");
        Objects.requireNonNull(password, "password can not be null");
        
        String encodedAuth = Base64.getEncoder()
                .encodeToString((username + ":" + password)
                .getBytes(StandardCharsets.UTF_8));
        
        httpRequestBuilder.header("Authorization", "Basic " + encodedAuth);
        
        return this;
    }

    public HttpRequest withTimeout(Duration duration) {
        Objects.requireNonNull(duration, "duration can not be null");
        httpRequestBuilder.timeout(duration);
        
        return this;
    }

    public HttpRequest withHttpVersion1_1() {
        httpRequestBuilder.version(Version.HTTP_1_1);
        
        return this;
    }

    public HttpRequest withProxy(String host, int port) {
        Objects.requireNonNull(host, "host can not be null");
        httpClientBuilder.proxy(ProxySelector.of(new InetSocketAddress(host, port)));
        
        return this;
    }

    public HttpRequest withJsonBody(Object object) throws HttpRequestException {
        Objects.requireNonNull(object, "object can not be null");
        
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            httpRequestBuilder.method(method, BodyPublishers.ofString(objectMapper.writeValueAsString(object)));
        } catch (JsonProcessingException e) {
            throw new HttpRequestException("Failed to convert object to JSON string", e);
        }      
        
        return this;
    }

    public HttpRequest withBarerToken(String token) {
        Objects.requireNonNull(token, "token can not be null");
        httpRequestBuilder.header("Authorization", "Bearer " + token);
        
        return this;
    }

    public HttpResponse execute() throws HttpRequestException {
        java.net.http.HttpResponse<String> response = null;
        
        if (!formAttributes.isEmpty()) {
            if (("POST").equals(method)) {
                httpRequestBuilder.POST(ofFormData(formAttributes));                
            } else if (("PUT").equals(method)) {
                httpRequestBuilder.PUT(ofFormData(formAttributes));
            }
            httpRequestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        }
        
        try {
            response = httpClientBuilder.build().send(httpRequestBuilder.build(), java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Failed to execute HTTP request", e);
        }
        
        return new HttpResponse(response);
    }

    private java.net.http.HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : formAttributes.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return java.net.http.HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}