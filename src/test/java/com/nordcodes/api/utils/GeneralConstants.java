package com.nordcodes.api.utils;

/**
 * Константы для набора API-тестов NordCodes.
 */
public final class GeneralConstants {

    private GeneralConstants() {
    }

    public static final String EXTERNAL_AUTH_PATH = "/auth";
    public static final String EXTERNAL_DO_ACTION_PATH = "/doAction";
    public static final String ENDPOINT_PATH = "/endpoint";


    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    public static final String API_KEY_HEADER_NAME = "X-Api-Key";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    public static final String TOKEN_FIELD_NAME = "token";
    public static final String ACTION_FIELD_NAME = "action";
    public static final String RESULT_FIELD_NAME = "result";
    public static final String MESSAGE_FIELD_NAME = "message";

    /**
     * Виды действий, поддерживаемые апой NordCodes.
     */
    public enum ActionType {
        LOGIN,
        ACTION,
        LOGOUT
    }
}
