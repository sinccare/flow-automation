package configuration;

import static com.codeborne.selenide.Configuration.*;

public class SelenideConfiguration {

    private SelenideConfiguration() {
        throw new IllegalStateException();
    }

    public static void configurationSelenide() {
        headless = false;
        browser = "chrome";
        timeout = 20000;
        pageLoadTimeout = 60000;
    }
}
