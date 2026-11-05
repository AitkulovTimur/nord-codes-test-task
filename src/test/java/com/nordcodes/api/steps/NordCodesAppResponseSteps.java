package com.nordcodes.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matcher;

import static com.nordcodes.api.specs.Specifications.nordCodesResponseSpec;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static com.nordcodes.api.utils.GeneralConstants.*;

/**
 * Шаги проверки ответов приложения NordCodes.
 */
public class NordCodesAppResponseSteps {

    private static final Matcher<String> ERROR_FIELD_MATCHER = equalTo("ERROR");
    private static final Matcher<String> OK_FIELD_MATCHER = equalTo("OK");

    /**
     * Проверить успешный ответ API NordCodes.
     *
     * @param response ответ приложения NordCodes
     */
    @Step("Проверяем, что приложение вернуло успешный результат")
    public void verifySuccessResponse(Response response) {
        response.then()
                .spec(nordCodesResponseSpec())
                .statusCode(SC_OK)
                .body(RESULT_FIELD_NAME, OK_FIELD_MATCHER);
    }

    /**
     * Проверить ответ с ошибкой и HTTP-кодом 400.
     *
     * @param response ответ приложения NordCodes
     */
    public void verifyError400Response(Response response) {
        verifyErrorResponse(response, SC_BAD_REQUEST);
    }

    /**
     * Проверить ответ с ошибкой, HTTP-кодом 401 и ожидаемой частью сообщения.
     *
     * @param response            ответ приложения NordCodes
     * @param expectedMessagePart ожидаемая часть текста ошибки
     */
    public void verifyError401Response(Response response, String expectedMessagePart) {
        verifyErrorResponse(response, SC_UNAUTHORIZED, expectedMessagePart);
    }

    /**
     * Проверить ответ с ошибкой и HTTP-кодом 403.
     *
     * @param response ответ приложения NordCodes
     */
    public void verifyError403Response(Response response) {
        verifyErrorResponse(response, SC_FORBIDDEN);
    }

    /**
     * Проверить ответ с ошибкой, HTTP-кодом 403 и ожидаемой частью сообщения.
     *
     * @param response            ответ приложения NordCodes
     * @param expectedMessagePart ожидаемая часть текста ошибки
     */
    public void verifyError403Response(Response response, String expectedMessagePart) {
        verifyErrorResponse(response, SC_FORBIDDEN, expectedMessagePart);
    }

    /**
     * Проверить ответ с ошибкой и HTTP-кодом 409.
     *
     * @param response ответ приложения NordCodes
     */
    public void verifyError409Response(Response response) {
        verifyErrorResponse(response, SC_CONFLICT);
    }

    /**
     * Проверить ответ с ошибкой и HTTP-кодом 500.
     *
     * @param response ответ приложения NordCodes
     */
    public void verifyError500Response(Response response) {
        verifyErrorResponse(response, SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Проверить формат ответа с ошибкой и ожидаемый HTTP-код.
     *
     * @param response           ответ приложения NordCodes
     * @param expectedStatusCode ожидаемый HTTP-код ответа
     */
    @Step("Проверяем, что приложение вернуло ошибку с HTTP-кодом {expectedStatusCode}")
    public void verifyErrorResponse(Response response, int expectedStatusCode) {
        response.then()
                .spec(nordCodesResponseSpec())
                .statusCode(expectedStatusCode)
                .body(RESULT_FIELD_NAME, ERROR_FIELD_MATCHER)
                .body(MESSAGE_FIELD_NAME, not(emptyOrNullString()));
    }

    /**
     * Проверить формат ответа с ошибкой и наличие ожидаемой части сообщения.
     *
     * @param response            ответ приложения NordCodes
     * @param expectedStatusCode  ожидаемый HTTP-код ответа
     * @param expectedMessagePart ожидаемая часть текста ошибки
     */
    @Step("Проверяем, что приложение вернуло ошибку с HTTP-кодом {expectedStatusCode} и сообщением, содержащим {expectedMessagePart}")
    public void verifyErrorResponse(Response response, int expectedStatusCode, String expectedMessagePart) {
        response.then()
                .spec(nordCodesResponseSpec())
                .statusCode(expectedStatusCode)
                .body(RESULT_FIELD_NAME, ERROR_FIELD_MATCHER)
                .body(MESSAGE_FIELD_NAME, not(emptyOrNullString()))
                .body(MESSAGE_FIELD_NAME, containsString(expectedMessagePart));
    }

}
