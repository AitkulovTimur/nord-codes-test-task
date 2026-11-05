package com.nordcodes.api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.nordcodes.api.config.ConfigReader.CONFIG;
import static com.nordcodes.api.utils.GeneralConstants.*;

/**
 * Фабрика общих спецификаций запросов и ответов RestAssured.
 */
public final class Specifications {

    private Specifications() {
    }

    /**
     * Создаёт базовую спецификацию запроса без заголовка API-ключа.
     *
     * @return спецификация запросов к API NordCodes без API-ключа
     */
    public static RequestSpecification nordCodesRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(CONTENT_TYPE_FORM)
                .setAccept(CONTENT_TYPE_JSON);

        if (CONFIG.isDetailedLog()) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Создаёт спецификацию запроса с настроенным API-ключом.
     *
     * @return спецификация запросов к API NordCodes с API-ключом
     */
    public static RequestSpecification nordCodesRequestSpecWithApiKey() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setContentType(CONTENT_TYPE_FORM)
                .setAccept(CONTENT_TYPE_JSON)
                .addHeader(API_KEY_HEADER_NAME, CONFIG.apiKey());

        if (CONFIG.isDetailedLog()) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }

    /**
     * Создаёт общую спецификацию ожидаемого ответа API NordCodes.
     *
     * @return спецификация ответа с проверкой типа содержимого JSON
     */
    public static ResponseSpecification nordCodesResponseSpec() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON);

        if (CONFIG.isDetailedLog()) {
            builder.log(LogDetail.ALL);
        }

        return builder.build();
    }
}
