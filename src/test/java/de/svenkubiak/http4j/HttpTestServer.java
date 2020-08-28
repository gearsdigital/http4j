package de.svenkubiak.http4j;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.util.HttpString;

public class HttpTestServer {
    
    public void start() {
        Undertow.builder()
            .addHttpListener(5555, "127.0.0.1")
            .setHandler(Handlers.routing()
                .setFallbackHandler(exchange -> {exchange.setStatusCode(404).endExchange();})
                .add("GET", "/test/get", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("GET", "/test/get/statuscode", exchange -> {exchange.setStatusCode(203).endExchange();})
                .add("GET", "/test/get/header", exchange -> {exchange.setStatusCode(200).getResponseHeaders().add(new HttpString("x-custom-header"), "foo");})
                .add("POST", "/test/post", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("PUT", "/test/put", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("HEAD", "/test/head", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("PATCH", "/test/patch", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("DELETE", "/test/delete", exchange -> {exchange.setStatusCode(200).endExchange();})
                .add("OPTIONS", "/test/options", exchange -> {exchange.setStatusCode(200).endExchange();}))
            .build()
            .start();
    }
}