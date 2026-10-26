package archieve;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MoreValidation {
    //    https://rahulshettyacademy.com/loginpagePractise/
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    @BeforeMethod
    public void setUp() {
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
// This option typically opens a new incognito-like browser context in Playwright, providing an isolated test execution environment with separate cookies, cache, and session storage.
        this.context = browser.newContext();
        this.context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
//        https://trace.playwright.dev/ == Visit this site and upload trace viewer extract for which code written in after method
        this.page = context.newPage();
//      To handle multiple browser sessions, we can create multiple contexts and pages, allowing each test execution session to run separately in an isolated environment.
        this.page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @Test(description = "Handle Child Windows")
    public void handleChildWindowTests(){
//        Below code going to handle a situation where new tab or window opened from click action
        Page newPage = context.waitForPage(() -> page.locator(".blinkingText").first().click());
//        Above code does not guarantee page already loaded properly so it's a good practice to use below validation
        newPage.waitForLoadState();
        System.out.println(newPage.title());
//        We are getting email id using .innerText()
        String emailAddress = newPage.locator("p.red > strong > a").innerText();
//        Now we are going to return to old page and put email id in input box
//        If you want to validate your input reflecting in int the input Box you can use .inputValue() method
        Locator username = page.getByLabel("Username:");
        username.fill(emailAddress);
        System.out.println(username.inputValue());

    }

    @Test(description = "Handle Radio Button , Popups and drop downs")
    public void uiControlTests(){
        page.getByLabel("Username:").fill("rahulshettyacademy");
        page.getByLabel("Password:").fill("Learning@830$3mK2");
        Locator userTypeRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("user"));
        userTypeRadio.click();
        page.getByText("You will be limited to only fewer functionalities of the app. Proceed?").isVisible(new Locator.IsVisibleOptions().setTimeout(10000));
        page.locator("#okayBtn").click();
        assertThat(userTypeRadio).isChecked();
        page.getByRole(AriaRole.COMBOBOX).selectOption("teach");
        Locator checkboxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        checkboxTerms.check();
        assertThat(checkboxTerms).isChecked();
    }
    @AfterMethod
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("Trace.zip")));
    }

}
