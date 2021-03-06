package de.svenkubiak.http4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.svenkubiak.http4j.enums.HttpMethod;

/**
 * 
 * @author svenkubiak
 *
 */
public class HttpRequest {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private java.net.http.HttpClient.Builder httpClientBuilder = java.net.http.HttpClient.newBuilder();
    private java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
    private Map<String, String> formAttributes = new HashMap<>();
    private HttpMethod httpMethod;
    
    public HttpRequest(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        
        httpRequestBuilder
            .method(this.httpMethod.toString(), BodyPublishers.noBody())
            .version(Version.HTTP_2)
            .timeout(Duration.ofSeconds(5));
        
        httpClientBuilder.followRedirects(java.net.http.HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(5))
            .version(Version.HTTP_2);
    }
    
    /**
     * Creates a new Http GET request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest get() {
        return new HttpRequest(HttpMethod.GET);
    }
    
    /**
     * Creates a new Http POST request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest post() {
        return new HttpRequest(HttpMethod.POST);
    }
    
    /**
     * Creates a new Http PUT request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest put() {
        return new HttpRequest(HttpMethod.PUT);
    }
    
    /**
     * Creates a new Http DELETE request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest delete() {
        return new HttpRequest(HttpMethod.DELETE);
    }
    
    /**
     * Creates a new Http HEAD request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest head() {
        return new HttpRequest(HttpMethod.HEAD);
    }
    
    /**
     * Creates a new Http PATCH request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest patch() {
        return new HttpRequest(HttpMethod.PATCH);
    }
    
    /**
     * Creates a new Http OPTIONS request
     * 
     * @return HttpRequest instance
     */
    public static HttpRequest options() {
        return new HttpRequest(HttpMethod.OPTIONS);
    }

    /**
     * Sets the URL the Http request is calling
     * 
     * @param url The url to use
     * 
     * @return HttpRequest instance
     */
    public HttpRequest to(String url) {
        Objects.requireNonNull(url, "url can not be null");
        httpRequestBuilder.uri(URI.create(url));
        
        return this;
    }

    /**
     * Disables redirects for the Http request
     * 
     * Redirects are enabled by default
     * 
     * @return HttpRequest instance
     */
    public HttpRequest disableRedirects() {
        httpClientBuilder.followRedirects(java.net.http.HttpClient.Redirect.NEVER);
        
        return this;
    }

    /**
     * Sets a form attribute to the Http request
     * 
     * @param name The name of the attribute
     * @param value The value of the attribute
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withFormAttribute(String name, String value) {
        Objects.requireNonNull(name, "name can not be null");
        formAttributes.put(name, value);
        
        httpRequestBuilder.header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED);
        
        return this;
    }

    /**
     * Sets a body to the Http request
     * 
     * @param body The body to set
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withBody(String body) {
        Objects.requireNonNull(body, "body can not be null");
        httpRequestBuilder.method(httpMethod.toString(), BodyPublishers.ofString(body));        
        
        return this;
    }

    /**
     * Sets an additional header to the request
     * 
     * @param name The name of the header
     * @param value The value of the header
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withHeader(String name, String value) {
        Objects.requireNonNull(name, "name can not be null");
        httpRequestBuilder.setHeader(name, value);
        
        return this;
    }

    /**
     * Sets basic authentication to the Http request
     * 
     * Basic authentication uses preemptive Authentication
     * 
     * @param username The username for basic authentication
     * @param password The password for basic authentication
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withBasicAuthentication(String username, String password) {
        Objects.requireNonNull(username, "username can not be null");
        Objects.requireNonNull(password, "password can not be null");
        
        var encodedAuth = Base64.getEncoder()
                .encodeToString((username + ":" + password)
                .getBytes(StandardCharsets.UTF_8));
        
        httpRequestBuilder.header("Authorization", "Basic " + encodedAuth);
        
        return this;
    }

    /**
     * Sets a timeout to the Http request
     * Default timeout is 5 seconds
     * 
     * @param duration The duration of the timeout
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withTimeout(Duration duration) {
        Objects.requireNonNull(duration, "duration can not be null");
        httpRequestBuilder.timeout(duration);
        httpClientBuilder.connectTimeout(duration);
        
        return this;
    }

    /**
     * Sets the Http version of the request to Version 1.1
     * Default Http version is 2.0
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withHttpVersion1_1() {
        httpRequestBuilder.version(Version.HTTP_1_1);
        
        return this;
    }

    /**
     * Sets a proxy server to the Http request
     * 
     * @param host The proxy host
     * @param port The proxy port
     * 
     * @return HttpRequest instance
     */
    public HttpRequest withProxy(String host, int port) {
        Objects.requireNonNull(host, "host can not be null");
        httpClientBuilder.proxy(ProxySelector.of(new InetSocketAddress(host, port)));
        
        return this;
    }

    /**
     * Sets the body of the request by converting a given object
     * to a JSON string
     * 
     * Additionally setting the content type to 'application/json'
     * 
     * @param object The object to convert into a JSON string
     * 
     * @return HttpRequest instance
     * @throws HttpRequestException if conversion to JSON string fails
     */
    public HttpRequest withJsonBody(Object object) throws HttpRequestException {
        Objects.requireNonNull(object, "object can not be null");
        
        var objectMapper = new ObjectMapper();
        try {
            httpRequestBuilder.method(httpMethod.toString(), BodyPublishers.ofString(objectMapper.writeValueAsString(object)));
        } catch (JsonProcessingException e) {
            throw new HttpRequestException("Failed to convert object to JSON string", e);
        }      
        
        httpRequestBuilder.header(CONTENT_TYPE, APPLICATION_JSON);
        
        return this;
    }

    /**
     * Sets an Authorization header using the following format:
     * 
     * Authorization: Bearer Token
     * 
     * @param token The token to use
     * @return HttpRequest instance
     */
    public HttpRequest withBarerToken(String token) {
        Objects.requireNonNull(token, "token can not be null");
        httpRequestBuilder.header("Authorization", "Bearer " + token);
        
        return this;
    }

    /**
     * Executes the configure Http request
     * 
     * @return HttpResponse The HttpResponse
     * @throws HttpRequestException if execution of the Http request fails
     */
    public HttpResponse execute() throws HttpRequestException {
        java.net.http.HttpResponse<String> response = null;
        
        if (!formAttributes.isEmpty()) {
            if (("POST").equals(httpMethod.toString())) {
                httpRequestBuilder.POST(getFormData());                
            } else if (("PUT").equals(httpMethod.toString())) {
                httpRequestBuilder.PUT(getFormData());
            }
            httpRequestBuilder.header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED);
        }
        
        try {
            response = httpClientBuilder.build().send(httpRequestBuilder.build(), java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpRequestException("Failed to execute HTTP request", e);
        }
        
        return new HttpResponse(response);
    }

    private java.net.http.HttpRequest.BodyPublisher getFormData() {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : formAttributes.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append('=');
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        
        return java.net.http.HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}