package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import util.DataProviderUtil;

import java.util.HashMap;

/**
 * Test class containing end-to-end test scenarios for event management,
 * administrative event creation, and ticket booking workflows.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class FrameworkTest extends TestBase {

    /**
     * Provides test data from a JSON file mapped to key-value pairs for data-driven testing.
     *
     * @return a two-dimensional Object array containing event test data maps
     */
    @DataProvider(name = "EventDataProvider")
    public Object[][] getEventData() {
        return DataProviderUtil.getJsonDataToMap("//src//test//resources//events.json");
    }

    /**
     * Executes the end-to-end test scenario verifying admin login, event creation,
     * ticket booking, and availability validations.
     *
     * @param eventCreationData a HashMap containing test parameters for event creation and booking
     */
    @Test(dataProvider = "EventDataProvider", description = "Validation for end to end Event addition and ticket booking", groups = {"smoke"})
    @Description("This test performs an end-to-end validation by logging in as an admin, creating a new event from JSON data, verifying the event card, booking tickets, and validating available seats and booking references.")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Pranab Sarkar")
    @Link(name = "Portfolio", url = "https://pranabsarkar.vercel.app/")
    @Issue("EVT-101")
    @TmsLink("TMS-456")
    public void DemoTest(HashMap<String, String> eventCreationData) {
        Assert.assertTrue(
                new LoginPage(getPage())
                        .loginToApplication(
                                (String) properties.get(env + ".user_name"),
                                (String) properties.get(env + ".password")
                        )
                        .goToAdminEventPage()
                        .createNewEvent(eventCreationData)
                        .validateEventCardAndProceedToForBooking(eventCreationData.get("titlePrefix"))
                        .bookEvent(eventCreationData)
                        .validateBookEvent(
                                eventCreationData.get("titlePrefix"),
                                Integer.parseInt(eventCreationData.get("totalSeats")),
                                Integer.parseInt(eventCreationData.get("seatToBook"))
                        ),
                "User able to create and validate booking event successfully"
        );
    }
}