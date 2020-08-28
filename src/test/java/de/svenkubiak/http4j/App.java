package de.svenkubiak.http4j;

import java.time.Duration;
import java.util.Map;

public class App {
    public static void main( String[] args ) throws HttpRequestException {
        Object object = null;
            HttpResponse httpResponse = HttpRequest.GET()
                .to("foo")
                .disableRedirects()
                .withTimeout(Duration.ofSeconds(5))
                .withHttpVersion1_1()
                .withBarerToken("foo")
                .withJsonBody(object)
                .withFormAttribute("name", "value")
                .withProxy("host", 8888)
                .withBody("")
                .withHeader("foo", "bar")
                .withBasicAuthentication("foo", "bar")
                .execute();
            
        httpResponse.isSuccessful();
        httpResponse.isOk();
        httpResponse.getHttpStatusCode();
        httpResponse.getHeader("foo");
        httpResponse.getBody();
        httpResponse.getJsonBodyAs(Map.class);
    }
}