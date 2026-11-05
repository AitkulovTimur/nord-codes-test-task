package com.nordcodes.api.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * Утильный класс для чтения конфига.
 */
public final class ConfigReader {
    public static final GeneralConfig CONFIG = ConfigFactory.create(GeneralConfig.class);

    private ConfigReader() {
    }
}
