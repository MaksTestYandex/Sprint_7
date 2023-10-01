package com.github.mablinov.sprint7;


import com.github.mablinov.sprint7.courier.CourierReq;
import com.github.mablinov.sprint7.courier.RequestCourierBody;
import com.github.mablinov.sprint7.courier.RequestCourierLoginBody;
import com.github.mablinov.sprint7.orders.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListOrdersTest {
    private CourierReq courierReq;
    private OrderReq orderReq;
    private AcceptReq acceptReq;
    private OrdersReq ordersReq;
    private OrderIdReq orderIdReq;

    private boolean needDeleteCourier = false;

    @Before
    public void setUp() {
        needDeleteCourier = false;

        courierReq = new CourierReq();
        orderReq = new OrderReq();
        acceptReq = new AcceptReq();
        ordersReq = new OrdersReq();
        orderIdReq = new OrderIdReq();
    }

    private final RequestCourierBody courierBody = new RequestCourierBody("alexandrSix19", "1234", "Saske");
    private final RequestCourierLoginBody courierLoginBody = RequestCourierLoginBody.from(courierBody);

    @Test
    @DisplayName("Check get list of orders ")
    @Description("Create, login courier. Create and accept new order. Get list of couriers orders | assert: status code")
    public void shouldGetListOfOrders() {
        courierReq.createNewCourier(courierBody);
        needDeleteCourier = true;
        ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
        RequestOrderBody orderBody = new RequestOrderBody("Naruto", "Uchiha", "Konoha, 143 apt.", "2", "+7 800 355 35 35", 5, "2023-09-26", "Saske, come back to Konoha", List.of("BLACK", "GREY"));
        ValidatableResponse createNewOrder = orderReq.createNewOrder(orderBody);
        ValidatableResponse getOrderId = orderIdReq.getOrderId(createNewOrder.extract().path("track"));
        acceptReq.acceptOrder(loginCourier.extract().path("id"), getOrderId.extract().path("order.id"));
        ValidatableResponse getOrdersList = ordersReq.getOrderList(loginCourier.extract().path("id"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, getOrdersList.extract().statusCode());
    }

    @Test
    @DisplayName("Check get list of orders created at metrostation")
    @Description("Create, login courier. Create and accept new orders. Get list of couriers orders | assert: status code, metrostation value in response")
    public void shouldGetListOfOrdersWithMetroStation() {
        courierReq.createNewCourier(courierBody);
        needDeleteCourier = true;
        ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
        for (int i = 1; i < 14; i++) {
            String metroStation = "27";
            if (i % 2 == 0) {
                metroStation = "39";
            }
            RequestOrderBody orderBody = new RequestOrderBody("Naruto", "Uchiha", "Konoha, 143 apt.", metroStation, "+7 800 355 35 35", 5, "2023-09-26", "Saske, come back to Konoha", List.of("BLACK", "GREY"));
            ValidatableResponse createNewOrder = orderReq.createNewOrder(orderBody);
            ValidatableResponse getOrderId = orderIdReq.getOrderId(createNewOrder.extract().path("track"));
            acceptReq.acceptOrder(loginCourier.extract().path("id"), getOrderId.extract().path("order.id"));
        }
        ValidatableResponse getOrdersList = ordersReq.getOrderList(loginCourier.extract().path("id"), List.of("17", "27"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, getOrdersList.extract().statusCode());
        assertEquals("Expect value failure!", "[27]", getOrdersList.extract().path("availableStations.number").toString());
    }

    @Test
    @DisplayName("Check get list of ten orders available to accept ")
    @Description("Create, login courier. Create new orders. Get list of ten couriers orders | assert: status code,size of list orders")
    public void shouldGetListOfTenAvailableOrders() {
        for (int i = 1; i < 14; i++) {
            String metroStation = "27";
            if (i % 2 == 0) {
                metroStation = "39";
            }
            RequestOrderBody orderBody = new RequestOrderBody("Naruto", "Uchiha", "Konoha, 143 apt.", metroStation, "+7 800 355 35 35", 5, "2023-09-26", "Saske, come back to Konoha", List.of("BLACK", "GREY"));
            orderReq.createNewOrder(orderBody);
        }
        ValidatableResponse getOrdersList = ordersReq.getTenAvailableOrders();
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, getOrdersList.extract().statusCode());
        assertEquals("Incorrect list size!", 10, getOrdersList.extract().jsonPath().getList("orders.id").size());
    }

    @Test
    @DisplayName("Check get list of ten orders available to accept at metrostation ")
    @Description("Create, login courier. Create new orders. Get list of ten couriers orders | assert: status code,size of list orders, value of metrostation in response")
    public void shouldGetListOfTenAvailableOrdersWithMetroStation() {
        for (int i = 1; i < 14; i++) {
            String metroStation = "110";
            if (i % 2 == 0) {
                metroStation = "39";
            }
            RequestOrderBody orderBody = new RequestOrderBody("Naruto", "Uchiha", "Konoha, 143 apt.", metroStation, "+7 800 355 35 35", 5, "2023-09-26", "Saske, come back to Konoha", List.of("BLACK", "GREY"));
            orderReq.createNewOrder(orderBody);
        }
        ValidatableResponse getOrdersList = ordersReq.getTenAvailableOrders("110");
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, getOrdersList.extract().statusCode());
        assertEquals("Incorrect list size!", 10, getOrdersList.extract().jsonPath().getList("orders.id").size());
        assertEquals("Expect value failure!", "[110]", getOrdersList.extract().path("availableStations.number").toString());
    }

    @After
    public void deleteCreatedCourier() {
        if (needDeleteCourier) {
            ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
            ValidatableResponse deleteCourier = courierReq.deleteCourier(loginCourier.extract().path("id"));
            assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, deleteCourier.extract().statusCode());
        }
    }

}
