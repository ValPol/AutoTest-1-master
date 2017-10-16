package Controls;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class Grid implements SelenideElement {


    public int GetRowCount(WebDriver driver, String locator) {
        WebElement baseTable = driver.findElement(By.xpath(locator));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        return tableRows.size();
    }
}
