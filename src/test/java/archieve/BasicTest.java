package archieve;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PageAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {
    Page page;

    @BeforeMethod
    public void setUp() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        this.page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
//        For Default assertions (not UI Action) timeout throughout while execution cycle
/*
PlaywrightAssertions.setDefaultAssertionTimeout(10_000);
*/
/*
//        For UI action (Locator Action) default time out is 10 seconds but we can customize below
        page.setDefaultTimeout(8000);
        */
    }

    @Test
    public void DemoTest() {
//        https://eventhub.rahulshettyacademy.com/login
//        test@playwright.it Playwright@Test#13
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
        Page page = browser.newPage();
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

//        Now we are going to add new event in that eventhub website
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.locator("//div/a[contains(text(),'Manage Events')]").click();
//        Now we will be arriving in Manage Event Page where we will be adding new event details
        page.getByLabel("Title").fill("Test Event one");
        /*
//      In case any specific Locator in age can't accept UI action execution because of readiness with in default 10 Second we can use below default configuration

        page.getByLabel("Title").fill("Test Event one", new Locator.FillOptions().setTimeout(10000));
        */
        page.getByPlaceholder("Describe the event…").fill("Test Event Description");
/*
        page.locator("#category").selectOption(new SelectOption().setLabel("Festival")); // Using visible text option
*/
        int seatAval = 300;
        page.locator("#category").selectOption("Festival");
        page.getByLabel("City").fill("Kolkata");
        page.getByLabel("venue").fill("Eco Space");
        page.getByLabel("Event Date & Time").fill("2026-10-29T15:10");
        page.getByLabel("Price ($)").fill("20");
        page.getByLabel("Total Seats").fill(String.valueOf(seatAval));
        page.getByLabel("Image URL (optional)").fill("https://www.awardspace.com/wp-content/uploads/2024/05/how-to-update-drupal-core-featured-image.jpeg");
        page.locator("#add-event-btn").click();
//        We are waiting for ropoup to arrive with in default 5-second playwright window
        assertThat(page.getByText("Event created!")).isVisible();
//        If in case we need more time tha 5 second we can configure it in step level
/*
assertThat(page.getByText("Event created!")).isVisible(
                new LocatorAssertions.IsVisibleOptions().setTimeout(10_000)); // its gonna wait 10 second polling based wait
                */
//        Till this point we've successfully added one event now we are going to book a ticket for that event
        page.locator("#nav-events").click();
        Locator evnCardLocator = page.locator("#event-card");
        assertThat(evnCardLocator.filter(new Locator.FilterOptions().setHasText("Test Event one"))).isVisible();
        evnCardLocator.filter(new Locator.FilterOptions().setHasText("Test Event one")).locator("#book-now-btn").click();
//        Now we are in event page we will be filling this
        int targetTicket = 4;
        while (targetTicket > 1) {
            page.locator("//button[@type='button']").filter(new Locator.FilterOptions().setHasText("+")).click();
            targetTicket--;
        }
        page.getByLabel("Full Name").fill("First Name and Last Name");
        page.getByLabel("Email").fill("abc@kkj.com");
        page.getByLabel("Phone Number").fill("7488154123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        String book_ref = page.locator(".booking-ref").innerText();
//        Assert That Booking confirm
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        page.locator("#nav-events").click();
        String availableSeat = String.valueOf(seatAval - targetTicket);
        evnCardLocator.filter(new Locator.FilterOptions().setHasText(availableSeat + " seats available"));
//        My Booking page validation
        page.locator("#nav-bookings").click();
        assertThat(page.locator(".booking-ref").filter(new Locator.FilterOptions().setHasText(book_ref))).isVisible();
    }

    @AfterMethod
    public void tearDown() {
        this.page.close();
    }
}