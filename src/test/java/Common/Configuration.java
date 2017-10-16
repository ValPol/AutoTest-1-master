package Common;

public class Configuration {

    public void SetDrivers(){
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\drivers\\IEDriverServer.exe");
        com.codeborne.selenide.Configuration.browser = "chrome";
        com.codeborne.selenide.Configuration.holdBrowserOpen = true;
    }
}