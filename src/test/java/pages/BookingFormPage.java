package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object class representing the Booking Form page.
 * Handles ticket quantity selection, user details input, confirmation,
 * booking reference extraction, and navigation back to the events page.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class BookingFormPage extends CommonSelectors {
    private final Page page;

    private final String NUMBER_OF_TICKETS_LOCATOR = "//button[@type='button']";
    private final String ADD_NUMBER_OF_TICKETS_LOCATOR = "+";
    private final String CONFIRM_BUTTON_TEXT = "Confirm Booking";
    private final String BOOKING_CONFIRMATION_MESSAGE = "Your tickets are reserved.";

    /**
     * Constructs a BookingFormPage instance with the specified Playwright page.
     *
     * @param page the thread-safe Playwright Page instance
     */
    public BookingFormPage(Page page) {
        this.page = page;
    }

    /**
     * Books tickets for an event by selecting the dynamic seat count from the data map,
     * filling out user personal details, confirming the booking, extracting the booking reference,
     * verifying the success message, and navigating back to the events page.
     *
     * @param bookingEvent a HashMap containing key-value pairs of booking details (e.g., fullName, email, phone, seatToBook)
     * @return a new instance of {@link EventsPage} representing the destination page
     */
    public EventsPage bookEvent(HashMap<String, String> bookingEvent) {
        // Dynamically parse the number of seats from the data map instead of hardcoding
        int targetTickets = Integer.parseInt(bookingEvent.get("seatToBook"));

        while (targetTickets > 1) {
            page.locator(NUMBER_OF_TICKETS_LOCATOR)
                    .filter(new Locator.FilterOptions().setHasText(ADD_NUMBER_OF_TICKETS_LOCATOR))
                    .click();
            targetTickets--;
        }

        page.getByLabel("Full Name").fill(bookingEvent.get("fullName"));
        page.getByLabel("Email").fill(bookingEvent.get("email"));
        page.getByLabel("Phone Number").fill(bookingEvent.get("phone"));

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONFIRM_BUTTON_TEXT)).click();

        String bookRef = page.locator(EVENT_BOOKING_REF_LOCATOR).innerText();
        System.setProperty("BOOKING_REF", bookRef);

        assertThat(page.getByText(BOOKING_CONFIRMATION_MESSAGE)).isVisible();
        page.locator(NAVBAR_EVENTS).click();

        return new EventsPage(page);
    }
}