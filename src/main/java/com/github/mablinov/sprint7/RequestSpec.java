package com.github.mablinov.sprint7;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

public class RequestSpec {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static final String BASE_PATH = "/api/v1";

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .build();
    }
}
