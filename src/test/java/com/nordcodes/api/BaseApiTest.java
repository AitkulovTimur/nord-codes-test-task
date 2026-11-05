package com.nordcodes.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.nordcodes.api.client.ApiClientNordCodes;
import com.nordcodes.api.mock.ExternalServiceMock;
import com.nordcodes.api.steps.NordCodesAppResponseSteps;
import com.nordcodes.api.support.NordCodesAppRunner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.nordcodes.api.config.ConfigReader.CONFIG;

/**
 * Базовый класс API-тестов с общей настройкой и очисткой тестового окружения. Также мы тут управляем ЖЦ
 * WireMock и приложения NordCodes.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseApiTest {
    protected WireMockServer wireMockServer;
    protected ExternalServiceMock externalServiceMock;
    protected ApiClientNordCodes apiClientNordCodes;
    protected NordCodesAppResponseSteps nordCodesAppResponseSteps;
    private NordCodesAppRunner nordCodesAppRunner;

    /**
     * Подготовить окружение перед запуском тестов.
     */
    @BeforeAll
    protected void globalSetUp() {
        try {
            startWireMock();
            configureTestHelpers();
            startNordCodesApp();
            configureRestAssured();
        } catch (RuntimeException e) {
            globalTearDown();
            throw e;
        }
    }

    private void configureTestHelpers() {
        externalServiceMock = new ExternalServiceMock(wireMockServer);
        apiClientNordCodes = new ApiClientNordCodes();
        nordCodesAppResponseSteps = new NordCodesAppResponseSteps();
    }

    @BeforeEach
    protected void resetWireMock() {
        wireMockServer.resetAll();
    }

    /**
     * Очистить тестовое окружение после завершения всех тестов.
     */
    @AfterAll
    protected void globalTearDown() {
        stopNordCodesApp();
        stopWireMock();
    }

    private void startWireMock() {
        wireMockServer = new WireMockServer(
                options().port(CONFIG.mockPort())
        );
        wireMockServer.start();
    }

    private void startNordCodesApp() {
        nordCodesAppRunner = new NordCodesAppRunner(getClass().getSimpleName());
        nordCodesAppRunner.start();
    }

    private void configureRestAssured() {
        RestAssured.baseURI = CONFIG.nordcodesAppUrl();

        if (CONFIG.isLogIfValidationFails()) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
    }

    private void stopNordCodesApp() {
        if (nordCodesAppRunner != null) {
            nordCodesAppRunner.stop();
        }
    }

    private void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
