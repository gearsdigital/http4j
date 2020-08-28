[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.svenkubiak/http4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.svenkubiak/http4j)

Http4j
================

Http4j is minimalistic and fluent library for making Http calls in Java.

Wait! Why in the world do we need another Http library for Java, when there is OkHttp, HttpComponents, Google Http, and gazillion other Http Clients for Java? 

Well ... while there are a lot of Http libraries and the core Http client of Java is probably the best way to go (so Http4j is build on-top of it), there a still some complexities coming with all those libraries (looking at you BodyPublishers and BodyParsers). So the idea
for Http4j was to make Http communication in Java as simple and self-explaining as possible, while also incorporating a most
common use-case: sending and receiving JSON.

Examples:

```
HttpResponse httpResponse = HttpRequest.GET()
    .to("https://www.google.de")
    .execute();
    
if (httpResponse.isSuccesful()) {
	String body = httpResponse.getBody();
}
```

```
HttpResponse httpResponse = HttpRequest.GET()
    .to("https://www.myurl.com")
    .withBarerToken("myToken")
    .withJsonBody(myObject)
    .execute();

if (httpResponse.isSuccessful()) {
    MyResponseObject object = httpResponse.getJsonBodyAs(MyResponseObject.class);
}
```

Full API:

```
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
    
httpResponse.isSuccess();
httpResponse.isOk();
httpResponse.getHttpStatusCode();
httpResponse.getHeader("foo");
httpResponse.getBody();
httpResponse.getJsonBodyAs(Map.class);
```

Requires Java 11.

Usage
------------------

1) Add the Http4j dependency to your pom.xml:

```
<dependency>
    <groupId>de.svenkubiak</groupId>
    <artifactId>http4j</artifactId>
    <version>x.x.x</version>
</dependency>
```

Roadmap
------------------
- JavaDoc
- More unit tests
- Check basic authentication via Proxy
- Support async Http connections
- Support form attachments