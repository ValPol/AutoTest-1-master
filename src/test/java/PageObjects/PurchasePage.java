package PageObjects;
import DTO.FilterDTO;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.csvreader.CsvWriter;
import com.google.common.base.Function;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class PurchasePage {

    @FindBy(id = "BaseMainContent_MainContent_chkPurchaseType_0")
    public  SelenideElement cbIs223Purchase;

    @FindBy(id = "BaseMainContent_MainContent_chkPurchaseType_1")
    public  SelenideElement cbIsCommercialPurchase;

    @FindBy(id = "BaseMainContent_MainContent_btnSearch")
    public SelenideElement btnSearch;

    @FindBy(xpath ="//*[@aria-describedby='BaseMainContent_MainContent_jqgTrade_OosNumber']")
    public List<SelenideElement> colEISNumber;

    @FindBy(xpath ="//*[@aria-describedby='BaseMainContent_MainContent_jqgTrade_LotStateString']")
    public List<SelenideElement> colLotState;

    @FindBy(xpath ="//*[@aria-describedby='BaseMainContent_MainContent_jqgTrade_StartPrice']")
    public List<SelenideElement> colSumm;

    @FindBy(xpath ="//*[@name='ctl00$ctl00$BaseMainContent$MainContent$txtStartPrice$txtRangeFrom']")
    public SelenideElement startPriceFrom;

    @FindBy(xpath ="//*[@id='BaseMainContent_MainContent_txtPublicationDate_txtDateFrom']")
    public SelenideElement publicationDateFrom;

    @FindBy(xpath ="//*[@id='BaseMainContent_MainContent_txtPublicationDate_txtDateTo']")
    public SelenideElement publicationDateTo;

    @FindBy(xpath ="//*[@id='sp_1_BaseMainContent_MainContent_jqgTrade_toppager']")
    public SelenideElement lblTotalRecordsCountInPager;

    @FindBy(xpath ="//*[@id='next_t_BaseMainContent_MainContent_jqgTrade_toppager']")
    public SelenideElement btnNextRecordInPager;

    @FindBy(xpath ="//*[@class='ui-pg-input']")
    public SelenideElement edtPageNumberInPager;

    @FindBy(xpath ="//*[@class='ui-pg-selbox']")
    public SelenideElement selectRecordsCountInPager;

    @FindBy(xpath ="//*[@id='gview_BaseMainContent_MainContent_jqgTrade']")
    public SelenideElement gridPurchases;

    @FindBy(xpath ="//*[@class='filter']")
    public SelenideElement pnlPurchasesGridFilter;

    @FindBy(xpath ="//*[@class='ui-paging-info']")
    public SelenideElement lblPagingInfo;

    public void setFilter (FilterDTO dto){
        pnlPurchasesGridFilter.shouldBe(enabled);
        cbIs223Purchase.click();
        cbIsCommercialPurchase.click();
        startPriceFrom.setValue(dto.startPriceFrom).pressEnter();
        publicationDateFrom.setValue(dto.publishDateFrom).pressEnter();
        publicationDateTo.setValue(dto.publishDateTo).pressEnter();
    }

    public void findRecords() throws Exception {
        btnSearch.shouldBe(enabled);
        btnSearch.click();
        WaitForGridContentIsChanged();
        setGridDisplayedRecordsCount("100");
    }

    public void evaluateStartPriceSum() throws InterruptedException, StaleElementReferenceException, IOException {
        gridPurchases.shouldBe(enabled);
        BigDecimal currencySumm = new BigDecimal(0);
        try {
            CsvWriter csvOutput = new CsvWriter(new FileWriter("out.csv", true), ',');
            do {
                gridPurchases.shouldBe(enabled);
                Thread.sleep(1000); //заменить относительной задержкой
                System.out.println("ALL pages" + lblTotalRecordsCountInPager.getText());
                System.out.println("current page" + edtPageNumberInPager.val());
                for (int i = 0; i < colEISNumber.size(); i++) {
                    String eisNumberColumnText = colEISNumber.get(i).getText();
                    if ((eisNumberColumnText.toCharArray().length != 1) && (colLotState.get(i).getText().compareTo("Отменена") != 0)) {
                        System.out.println(i + " line " + colEISNumber.get(i).getText());
                        csvOutput.write(colEISNumber.get(i).getText());
                        System.out.println(colLotState.get(i).getText());
                        csvOutput.write(colLotState.get(i).getText());
                        String currencyCurrentText = clearCurrencyText(colSumm.get(i).getText());
                        currencySumm = currencySumm.add(new BigDecimal(currencyCurrentText));
                        System.out.println(currencySumm);
                        csvOutput.write(currencySumm.toString());
                        csvOutput.endRecord();
                    }
                }
                btnNextRecordInPager.click();

            }
            while (Integer.parseInt(lblTotalRecordsCountInPager.getText()) > Integer.parseInt(edtPageNumberInPager.val()));
            csvOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String clearCurrencyText(String str){
        String money = str.replaceAll(" руб.", "");
        money = money.replaceAll(" ", "");
        money = money.replaceAll(",", ".");
        return money;
    }

    private void setGridDisplayedRecordsCount(String count) throws InterruptedException {
        selectRecordsCountInPager.shouldBe(enabled);
        selectRecordsCountInPager.selectOptionContainingText(count);
        Thread.sleep(1000);
    }

    private void WaitForGridContentIsChanged() throws Exception {
        WebDriver driver = getWebDriver();
        (new WebDriverWait(driver, Configuration.timeout)).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver webDriver) {
               return colEISNumber.size() == 0;
            }
        });
        (new WebDriverWait(driver, Configuration.timeout)).until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                return colEISNumber.size() != 0;
            }
        });
    }
}


