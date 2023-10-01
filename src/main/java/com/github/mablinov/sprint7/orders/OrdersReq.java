package com.github.mablinov.sprint7.orders;

import com.github.mablinov.sprint7.RequestSpec;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class OrdersReq {
    @Step
    public ValidatableResponse getOrderList(Integer courierId) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get(OrderSpec.ORDERS_PATH + "?courierId=" + courierId)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse getOrderList(Integer courierId, List<String> metroStations) {
        String stationsList = metroStations.stream().map(value -> "\"" + value + "\"").collect(Collectors.joining(","));
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get(OrderSpec.ORDERS_PATH + "?courierId=" + courierId + "&nearestStation=[" + stationsList + "]")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse getTenAvailableOrders() {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get(OrderSpec.ORDERS_PATH + "?limit=10&page=0")
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse getTenAvailableOrders(String metroStation) {
        return given()
                .spec(RequestSpec.requestSpecification())
                .when()
                .log().all()
                .get(OrderSpec.ORDERS_PATH + "?limit=10&page=0&nearestStation=[\"" + metroStation + "\"]")
                .then()
                .log().all();
    }

}
