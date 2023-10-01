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
import static org.junit.Assert.assertNotNull;

public class CourierLoginTest {
    private CourierReq courierReq;
    boolean needDeleteCourier = false;

    @Before
    public void setUp() {
        needDeleteCourier = false;
        courierReq = new CourierReq();
    }

    private final RequestCourierBody courierBody = new RequestCourierBody("alexandrFour112", "1234", "Saske");
    private final RequestCourierLoginBody courierLoginBody = RequestCourierLoginBody.from(courierBody);
    private final RequestCourierLoginBody courierBodyWithoutLogin = new RequestCourierLoginBody(null, "1234");
    private final RequestCourierLoginBody courierBodyWithLoginMistake = new RequestCourierLoginBody("alexand", "1234");

    @Test
    @DisplayName("Check courier authorize")
    @Description("Create  courier account, login it | assert: status code")
    public void shouldCourierAuthorize() {
        courierReq.createNewCourier(courierBody);
        ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_OK, loginCourier.extract().statusCode());
        needDeleteCourier = true;
    }

    @Test
    @DisplayName("Check necessary fields for login")
    @Description("Send request without login value | assert: status code, message")
    public void shouldVerifyNecessaryFieldsInRequestLoginCourier() {
        ValidatableResponse loginCourier = courierReq.loginCourier(courierBodyWithoutLogin);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_BAD_REQUEST, loginCourier.extract().statusCode());
        assertEquals("Key value failure!", "Недостаточно данных для входа", loginCourier.extract().path("message").toString());
    }

    @Test
    @DisplayName("Check orthography fields for login")
    @Description("Send request with login mistake value | assert: status code, message")
    public void shouldVerifyRequestLoginCourierWithLoginMistake() {
        ValidatableResponse loginCourier = courierReq.loginCourier(courierBodyWithLoginMistake);
        assertEquals("Status code failure!", HttpURLConnection.HTTP_NOT_FOUND, loginCourier.extract().statusCode());
        assertEquals("Key value failure!", "Учетная запись не найдена", loginCourier.extract().path("message").toString());
    }

    @Test
    @DisplayName("Get courier id")
    @Description("extract id value from  login response | assert: id value not null")
    public void shouldGetIdWithSuccessRequest() {
        courierReq.createNewCourier(courierBody);
        ValidatableResponse loginCourier = courierReq.loginCourier(courierLoginBody);
        assertNotNull("Key value is null", loginCourier.extract().path("id"));
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