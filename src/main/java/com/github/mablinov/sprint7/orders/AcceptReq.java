package com.github.mablinov.sprint7.orders;

import com.github.mablinov.sprint7.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class AcceptReq {

    @Step
    public ValidatableResponse acceptOrder(Integer courierId, Integer orderTrack) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .put(OrderSpec.ORDERS_PATH + "/accept/" + orderTrack + "?courierId=" + courierId)
                .then()
                .log().all();
    }
}
