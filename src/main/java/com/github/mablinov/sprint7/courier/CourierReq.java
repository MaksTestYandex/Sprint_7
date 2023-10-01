package com.github.mablinov.sprint7.courier;

import com.github.mablinov.sprint7.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierReq {

    public static final String COURIER_PATH = "/courier/";

    @Step
    public ValidatableResponse createNewCourier(RequestCourierBody courierBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(courierBody)
                .when()
                .log().all()
                .post(COURIER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse loginCourier(RequestCourierLoginBody courierBody) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .body(courierBody)
                .when()
                .log().all()
                .post(COURIER_PATH + "login")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse deleteCourier(Integer courierId) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .delete(COURIER_PATH + courierId)
                .then()
                .log().all();
    }
}
