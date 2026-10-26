package archieve;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationContinuedTest {
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
        this.page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(description = "Validate text box is visible and hidden based on ui action")
    public void validateTextBoxIsVisibleAndHidden(){
        Locator textBox = page.locator("input[name='show-hide']");
        assertThat(textBox).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(textBox).isHidden();
    }

    @Test(description = "Validate alert can be handle using playwright")
    public void validateAlert(){
        page.onDialog(dialog -> dialog.accept());
        page.locator("#alertbtn").click();
    }

    @Test(description = "Validate mouse hover action using playwright")
    public void validateMouseHover(){
        page.locator("#mousehover").hover();
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Top")).click();
    }

    @Test(description = "Validate playwright can access frames")
    public void validateFrameAttached(){
        FrameLocator frameLocator = page.frameLocator("#courses-iframe");
        frameLocator.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        assertThat(frameLocator.locator(".page-title h1")).hasText("LEARNING PATHS");
    }

    @Test(description = "Take screenshot on page and locator level")
    public void takeScreenShot(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("fullscreen.png")));
//        Taking specific element as screen shot
        page.locator("table[name='courses']").screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("TableLvlScreenshot.png")));
    }
}
