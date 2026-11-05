package com.nordcodes.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.nordcodes.api.specs.Specifications.nordCodesRequestSpec;
import static com.nordcodes.api.specs.Specifications.nordCodesRequestSpecWithApiKey;
import static com.nordcodes.api.utils.GeneralConstants.ACTION_FIELD_NAME;
import static com.nordcodes.api.utils.GeneralConstants.API_KEY_HEADER_NAME;
import static com.nordcodes.api.utils.GeneralConstants.ActionType;
import static com.nordcodes.api.utils.GeneralConstants.ENDPOINT_PATH;
import static com.nordcodes.api.utils.GeneralConstants.TOKEN_FIELD_NAME;
import static io.restassured.RestAssured.given;

/**
 * Клиент для взаимодействия с API NordCode.
 */
public class ApiClientNordCodes {
    /**
     * Отправить действие в апу NordCodes с API-ключом
     *
     * @param token      токен аутентификации
     * @param actionType тип действия {@link ActionType}
     * @return ответ приложения NordCodes
     */
    @Step("Отправляем в приложение действие {actionType}")
    public Response sendAction(String token, ActionType actionType) {
        return sendAction(token, actionType.name());
    }

    @Step("Отправляем в приложение действие {actionType}")
    public Response sendAction(String token, String actionType) {
        return given()
                .spec(nordCodesRequestSpecWithApiKey())
                .formParam(TOKEN_FIELD_NAME, token)
                .formParam(ACTION_FIELD_NAME, actionType)
                .when()
                .post(ENDPOINT_PATH);
    }

    /**
     * Отправить действие в апу NordCodes без токена
     *
     * @param actionType тип действия {@link ActionType}
     * @return ответ приложения NordCodes
     */
    @Step("Отправляем в приложение действие {actionType} без token")
    public Response sendActionWithoutToken(ActionType actionType) {
        return given()
                .spec(nordCodesRequestSpecWithApiKey())
                .formParam(ACTION_FIELD_NAME, actionType.name())
                .when()
                .post(ENDPOINT_PATH);
    }

    /**
     * Отправить действие в апу NordCodes без действия
     *
     * @param token токен аутентификации
     * @return ответ приложения NordCodes
     */
    @Step("Отправляем в приложение запрос без action")
    public Response sendRequestWithoutAction(String token) {
        return given()
                .spec(nordCodesRequestSpecWithApiKey())
                .formParam(TOKEN_FIELD_NAME, token)
                .when()
                .post(ENDPOINT_PATH);
    }

    /**
     * Отправить действие в апу NordCodes без API-ключа.
     *
     * @param token      токен аутентификации
     * @param actionType тип действия {@link ActionType}
     * @return ответ приложения NordCodes
     */
    @Step("Отправляем в приложение действие {actionType}, не используя API-ключ")
    public Response sendActionWithoutApiKey(String token, ActionType actionType) {
        return given()
                .spec(nordCodesRequestSpec())
                .formParam(TOKEN_FIELD_NAME, token)
                .formParam(ACTION_FIELD_NAME, actionType.name())
                .when()
                .post(ENDPOINT_PATH);
    }

    /**
     * Отправить действие в апу NordCodes с указанным API-ключом.
     *
     * @param apiKey     передаваемый API-ключ
     * @param token      токен аутентификации
     * @param actionType тип действия {@link ActionType}
     * @return ответ приложения NordCodes
     */
    @Step("Отправляем в приложение действие {actionType}, используя переданный ключ доступа")
    public Response sendActionWithApiKey(String apiKey, String token, ActionType actionType) {
        return given()
                .spec(nordCodesRequestSpec())
                .header(API_KEY_HEADER_NAME, apiKey)
                .formParam(TOKEN_FIELD_NAME, token)
                .formParam(ACTION_FIELD_NAME, actionType.name())
                .when()
                .post(ENDPOINT_PATH);
    }

}
