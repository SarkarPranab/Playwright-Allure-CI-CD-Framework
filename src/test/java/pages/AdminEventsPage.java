package pages;

import com.microsoft.playwright.Page;

import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object class representing the Admin Events management page.
 * Handles form filling for new event creation, assertion of success states,
 * and navigation across admin modules.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class AdminEventsPage extends CommonSelectors {
    private final Page page;

    private static final String TITLE_LOCATOR = "Title";
    private static final String EVENT_DESCRIPTION_PLACEHOLDER = "Describe the event…";
    private static final String EVENT_CATEGORY_LOCATOR = "#category";
    private static final String EVENT_CITY = "City";
    private static final String EVENT_VENUE = "venue";
    private static final String EVENT_DATE_TIME = "Event Date & Time";
    private static final String EVENT_PRICE = "Price ($)";
    private static final String EVENT_TOTAL_SEATS = "Total Seats";
    private static final String EVENT_IMAGE_URL = "Image URL (optional)";
    private static final String ADD_EVENT_BUTTON = "#add-event-btn";
    private static final String EVENT_CREATION_STATE = "Event created!";

    /**
     * Constructs an AdminEventsPage instance with the specified Playwright page.
     *
     * @param page the thread-safe Playwright Page instance
     */
    public AdminEventsPage(Page page) {
        this.page = page;
    }

    /**
     * Creates a new event by filling in the event details form from the provided data map,
     * verifying the creation success message, and navigating to the events list page.
     *
     * @param eventData a HashMap containing key-value pairs of event attributes (e.g., title, description, category)
     * @return a new instance of {@link EventsPage} representing the destination page
     */
    public EventsPage createNewEvent(HashMap<String, String> eventData) {
        page.getByLabel(TITLE_LOCATOR).fill(eventData.get("titlePrefix"));
        page.getByPlaceholder(EVENT_DESCRIPTION_PLACEHOLDER).fill(eventData.get("description"));
        page.locator(EVENT_CATEGORY_LOCATOR).selectOption(eventData.get("category"));
        page.getByLabel(EVENT_CITY).fill(eventData.get("city"));
        page.getByLabel(EVENT_VENUE).fill(eventData.get("venue"));
        page.getByLabel(EVENT_DATE_TIME).fill(eventData.get("dateTime"));
        page.getByLabel(EVENT_PRICE).fill(eventData.get("price"));
        page.getByLabel(EVENT_TOTAL_SEATS).fill(eventData.get("totalSeats"));
        page.getByLabel(EVENT_IMAGE_URL).fill(eventData.get("imgUrl"));

        page.locator(ADD_EVENT_BUTTON).click();

        assertThat(page.getByText(EVENT_CREATION_STATE)).isVisible();
        page.locator(NAVBAR_EVENTS).click();

        return new EventsPage(page);
    }
}