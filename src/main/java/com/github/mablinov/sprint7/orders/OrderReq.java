package com.github.mablinov.sprint7.orders;

import com.github.mablinov.sprint7.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderReq {
    @Step
    public ValidatableResponse createNewOrder(RequestOrderBody orderBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(orderBody)
                .when()
                .log().all()
                .post(OrderSpec.ORDERS_PATH)
                .then()
                .log().all();
    }
}
