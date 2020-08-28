package de.svenkubiak.http4j;

/**
 * 
 * @author svenkubiak
 *
 */
public class HttpRequestException extends Exception {
    private static final long serialVersionUID = 6859267843322140140L;

    public HttpRequestException(String message, Exception e) {
        super(message, e);
    }
}