package StepDefinitions;

import DTO.FilterDTO;
import PageObjects.PurchasePage;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static com.codeborne.selenide.Selenide.open;

/**
 * Created by User2 on 13.10.2017.
 */
public class MyStepdefs {

    PurchasePage page;

    @Before
    public void BeforeAll () throws Exception {
        Common.Configuration config = new Common.Configuration();
        config.SetDrivers();
    }

    @Given("^I have opened purchases page$")
    public void iHaveOpenedPurchasesPage() throws Throwable {
        page = open("https://223.rts-tender.ru/supplier/auction/Trade/Search.aspx", PurchasePage.class);
    }
    @When("^I filled filter by values$")
    public void iFilledFilterByValues(DataTable table) throws Throwable {
        FilterDTO dto = table.asList(FilterDTO.class).get(0);
        page.setFilter(dto);
    }
    @When("^I click Search button and get new results$")
    public void iClickSearchButton() throws Throwable {
        page.findRecords();
    }

    @Then("^I can evaluate summ of all purchases$")
    public void iCanEvaluateSummOfAllPurchases() throws Throwable {
        page.evaluateStartPriceSum();
    }

    @After
    public void AfterTest(){

    }
}
