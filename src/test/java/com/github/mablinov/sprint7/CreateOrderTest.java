package com.github.mablinov.sprint7;

import com.github.mablinov.sprint7.orders.OrderReq;
import com.github.mablinov.sprint7.orders.RequestOrderBody;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> colourList;
    private final int expectedStatusCode;

    public CreateOrderTest(List<String> colourList, int expectedStatusCode) {
        this.colourList = colourList;
        this.expectedStatusCode = expectedStatusCode;
    }

    private OrderReq orderReq;

    @Before
    public void setUp() {
        orderReq = new OrderReq();
    }

    @Parameterized.Parameters
    public static Object[][] getStatusCode() {
        return new Object[][]{
                {List.of("BLACK"), HttpURLConnection.HTTP_CREATED},
                {List.of("GREY"), HttpURLConnection.HTTP_CREATED},
                {List.of("BLACK", "GREY"), HttpURLConnection.HTTP_CREATED},
                {List.of(), HttpURLConnection.HTTP_CREATED},
        };
    }

    @Test
    @DisplayName("Check that order is created ")
    @Description("Send request various value of colour  | assert: status code, key value is not null")
    public void shouldCreateNewOrder() {
        RequestOrderBody orderBody = new RequestOrderBody("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2023-09-26", "Saske, come back to Konoha", colourList);
        ValidatableResponse createNewOrder = orderReq.createNewOrder(orderBody);
        assertEquals("Status code failure!", expectedStatusCode, createNewOrder.extract().statusCode());
        assertNotNull("Key value is null", createNewOrder.extract().path("track"));
    }
}
