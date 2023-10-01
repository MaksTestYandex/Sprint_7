package com.github.mablinov.sprint7.orders;

import com.github.mablinov.sprint7.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderIdReq {
    @Step
    public ValidatableResponse getOrderId(Integer orderTrack) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get(OrderSpec.ORDERS_PATH + "/track?t=" + orderTrack)
                .then()
                .log().all();
    }
}
