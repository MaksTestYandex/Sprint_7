package com.github.mablinov.sprint7;

import com.github.mablinov.sprint7.courier.CourierReq;
import com.github.mablinov.sprint7.courier.RequestCourierBody;
import com.github.mablinov.sprint7.courier.RequestCourierLoginBody;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;

public class CreateCourierTest {
    private CourierReq courierReq;
    private boolean needDeleteCourier = false;

    private final RequestCourierBody courierBody = new RequestCourierBody("alexandrThreett", "1234", "Saske");
    private final RequestCourierLoginBody courierLoginBody = RequestCourierLoginBody.from(courierBody);
    private final RequestCourierBody courierBodyWithoutPassword = new RequestCourierBody("alexandrTwo7", "", "Saske");

    @Before
    public void setUp() {
        needDeleteCourier = false;
        courierReq = new CourierReq();
    }

    @Test
    @DisplayName("Check that courier account is created ")
    @Description("Create  courier account | assert: status code,message")
    public void shouldCreateNewCourier() {
        ValidatableResponse createNewCourier = courierReq.createNewCourier(courierBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_CREATED, createNewCourier.extract().statusCode());
        assertEquals("Key value failure!", true, createNewCourier.extract().path("ok"));
        needDeleteCourier = true;
    }

    @Test
    @DisplayName("Check that courier account may delete ")
    @Description("Create, login and delete courier account | assert: status code")
    public void shouldDeleteCourier() {
        courierReq.createNewCourier(courierBody);
        ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
        ValidatableResponse deleteCourier = courierReq.deleteCourier(loginCourier.extract().path("id"));
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, deleteCourier.extract().statusCode());
    }

    @Test
    @DisplayName("Check that courier account is unique ")
    @Description("Try to create duplicate courier account | assert: status code")
    public void shouldNotCreateDuplicateCourier() {
        courierReq.createNewCourier(courierBody);
        ValidatableResponse createNewCourier = courierReq.createNewCourier(courierBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_CONFLICT, createNewCourier.extract().statusCode());
        needDeleteCourier = true;
    }

    @Test
    @DisplayName("Check necessary fields for create courier account ")
    @Description("Send request without password value  | assert: status code")
    public void shouldVerifyNecessaryFieldsInRequestCreateNewCourier() {
        ValidatableResponse createNewCourier = courierReq.createNewCourier(courierBodyWithoutPassword);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_BAD_REQUEST, createNewCourier.extract().statusCode());
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