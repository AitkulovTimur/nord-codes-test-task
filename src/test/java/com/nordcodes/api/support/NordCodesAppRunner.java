package com.nordcodes.api.support;

import com.nordcodes.api.exception.NordCodesAppStartException;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.nordcodes.api.config.ConfigReader.CONFIG;
import static org.awaitility.Awaitility.await;

/**
 * Класс для управления жизненным циклом процесса приложения NordCodes. (запуск, остановка и другие настройки).
 */
public class NordCodesAppRunner {
    private static final String JAVA_HOME_PROPERTY_NAME = "java.home";
    private static final String JAVA_UNIX_CMD = "java";
    private static final String JAVA_WIN_CMD = "java.exe";
    private static final String OS_NAME_PROPERTY_NAME = "os.name";
    private static final String JAR_OPTION = "-jar";
    private static final DateTimeFormatter LOG_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final String testClassName;
    private Process nordCodesAppProcess;

    /**
     * Создать раннер апы NordCodes.
     *
     * @param testClassName имя тестового класса
     */
    public NordCodesAppRunner(String testClassName) {
        this.testClassName = testClassName;
    }

    /**
     * Запустить процесс апы NordCodes.
     *
     * @throws NordCodesAppStartException если не удалось запустить приложение
     */
    public void start() {
        if (nordCodesAppProcess != null && nordCodesAppProcess.isAlive()) {
            return;
        }

        try {
            ProcessBuilder processBuilder = configureProcessBuilder();
            nordCodesAppProcess = processBuilder.start();
            waitUntilApplicationStarted();
        } catch (Exception e) {
            stop();
            throw new NordCodesAppStartException(e);
        }
    }

    private void waitUntilApplicationStarted() {
        await()
                .atMost(Duration.ofSeconds(CONFIG.nordcodesAppStartSeconds()))
                .pollInterval(Duration.ofMillis(CONFIG.pollMilliseconds()))
                .until(this::isNordCodesAppStarted);
    }

    /**
     * Проверить, запустилось ли приложение NordCodes + что порт открыт и принимает конекшены.
     *
     * @return true, если апа работает и порт открыт. Иначе false
     */
    public boolean isNordCodesAppStarted() {
        return nordCodesAppProcess != null
                && nordCodesAppProcess.isAlive()
                && isNordCodesAppPortOpen();
    }

    private boolean isNordCodesAppPortOpen() {
        URI uri = URI.create(CONFIG.nordcodesAppUrl());

        try (Socket socket = new Socket()) {
            socket.connect(
                    new InetSocketAddress(uri.getHost(), uri.getPort()),
                    CONFIG.socketConnectMilliseconds()
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ProcessBuilder configureProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder(
                getJavaExecutablePath(),
                "-Dsecret=" + CONFIG.secret(),
                String.format("-Dmock=%s/", CONFIG.mockUrl()),
                JAR_OPTION,
                CONFIG.jarPath()
        );

        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.to(getApplicationLogFile()));

        return processBuilder;
    }

    private String getJavaExecutablePath() {
        String javaHome = System.getProperty(JAVA_HOME_PROPERTY_NAME);
        String executableName = isWindows() ? JAVA_WIN_CMD : JAVA_UNIX_CMD;

        return Path.of(javaHome, "bin", executableName).toString();
    }

    private boolean isWindows() {
        return System.getProperty(OS_NAME_PROPERTY_NAME)
                .toLowerCase(Locale.ROOT)
                .contains("win");
    }

    private File getApplicationLogFile() {
        String timestamp = LocalDateTime.now().format(LOG_TIMESTAMP_FORMATTER);

        Path logFilePath = Path.of(
                CONFIG.logDirectory(),
                "%s_%s.log".formatted(testClassName, timestamp)
        );

        try {
            Files.createDirectories(logFilePath.getParent());
        } catch (IOException e) {
            throw new NordCodesAppStartException(
                    "Failed to create directory for application logs: " + logFilePath.getParent(),
                    e
            );
        }

        return logFilePath.toFile();
    }

    /**
     * Остановить процесс апы NordCodes.
     */
    public void stop() {
        if (nordCodesAppProcess == null || !nordCodesAppProcess.isAlive()) {
            return;
        }

        nordCodesAppProcess.destroy();

        try {
            boolean stopped = nordCodesAppProcess.waitFor(
                    CONFIG.nordcodesAppStopSeconds(),
                    TimeUnit.SECONDS
            );

            if (!stopped) {
                nordCodesAppProcess.destroyForcibly();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            nordCodesAppProcess.destroyForcibly();
        }
    }
}
