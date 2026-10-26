package archieve;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockingApiTesting {
    Page page;
    @BeforeMethod
    public void setUp() {
        final String URL = "https://eventhub.rahulshettyacademy.com/login";
        final String username = "test.pl@abc.com";
        final String password = "Playwright@Test#13";
        // Creating playwright obj from Playwright interface using Playwright.create() method
        Playwright playwright = Playwright.create();
        // Creating Browser instace from browser interface type of chrome using previously created Playwright object -> chromium.launch()
//        HeadMode --> Browser UI will be visible
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        HeadlessMode --> Browser UI will not be visible
/*
        Browser browser = playwright.chromium().launch();
*/
//        "Can I test on my actual physical Chrome browser?"
  /*
  Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome"));
  */
        // Now we will open a window or tab in that for we have to create a Page instance from browser object instance
        page = browser.newPage();
        // now we are going to navigate to url specified using page
        page.navigate(URL);
        // User .title() with page instance for
        System.out.println(page.title()); //EventHub — Discover & Book Events
//        assertThat(Page Context will go there).hasTitle(Expected value will go here.)
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
//        Now we are going to fill log in of which we need to enter email , passworda and click on sign in button
//        For enter email and password we are gonna get the lable using page.getBAraLable() --> method follow by .fill() -> which will help us to fill the data.
        page.getByLabel("Email").fill(username);
        page.getByLabel("Password").fill(password);
//        For finding button using role we are going to use page.getByRole() --> which uses AraiRole.BUTTON of element to find out but we need to filter futher using page interface so we can get the right return new Page.GetByRoleOptions().setName("<Button text>")
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        //        For validate any anchor Link exist on page or not we mostly utilize page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("<Link Name>")) it will help us to find.
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    }

    @Test(description = "Intercepting Response from server using route.fulfill")
    // Not working
    public void testRoute(){
        page.route("**/api/events**", route ->
           route.fulfill(
                   new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events.json"))
           ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        System.out.println("Debug Pointer");
    }

    @Test(description = "Intercepting Request from server using route.resume")
    // Not working
    public void testRouteResume(){
        page.route("**/api/bookings**", route ->
                route.resume(
                        new Route.ResumeOptions().setUrl("https://eventhub.rahulshettyacademy.com/bookings/12457")
                )
                );
        page.navigate("https://eventhub.rahulshettyacademy.com/bookings/37684");
        System.out.println("Debug Pointer");
    }

}