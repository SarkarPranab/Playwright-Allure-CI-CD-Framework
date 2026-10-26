package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object class representing the Events listing and management page.
 * Handles event card validation, initiating the booking flow,
 * and validating available seats and booking references.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class EventsPage extends CommonSelectors {
    private final Page page;
    private static final String BOOK_NOW_BUTTON_LOCATOR = "#book-now-btn";

    /**
     * Constructs an EventsPage instance with the specified Playwright page.
     *
     * @param page the thread-safe Playwright Page instance
     */
    public EventsPage(Page page) {
        this.page = page;
    }

    /**
     * Retrieves a specific event card locator filtered by the provided event name.
     *
     * @param eventName the name/title of the event to filter by
     * @return a Playwright {@link Locator} representing the filtered event card
     */
    public Locator getEnvCardLocatorByEventName(String eventName) {
        return page.locator(EVENTS_CARD_LOCATOR).filter(new Locator.FilterOptions().setHasText(eventName));
    }

    /**
     * Validates that the event card is visible, clicks the "Book Now" button,
     * and transitions to the booking form page.
     *
     * @param eventName the name/title of the event to book
     * @return a new instance of {@link BookingFormPage} representing the destination page
     */
    public BookingFormPage validateEventCardAndProceedToForBooking(String eventName) {
        assertThat(getEnvCardLocatorByEventName(eventName)).isVisible();
        this.getEnvCardLocatorByEventName(eventName)
                .filter(new Locator.FilterOptions().setHasText(eventName))
                .locator(BOOK_NOW_BUTTON_LOCATOR)
                .click();
        return new BookingFormPage(page);
    }

    /**
     * Validates that after booking, the remaining available seats are updated correctly
     * and that the booking reference appears in the bookings tab.
     *
     * @param eventName     the name/title of the event
     * @param seatAvailable the initial total seats before booking
     * @param seatTarget    the number of seats that were booked
     * @return true if the booking reference is visible in the bookings section
     */
    public boolean validateBookEvent(String eventName, int seatAvailable, int seatTarget) {
        // Fixed syntax/operator: subtracting booked seats from total seats to find remaining availability
        String availableSeat = String.valueOf(seatAvailable - seatTarget);

        this.getEnvCardLocatorByEventName(eventName)
                .filter(new Locator.FilterOptions().setHasText(availableSeat + " seats available"));

        page.locator(NAVBAR_BOOKINGS).click();

        Locator getBookingId = page.locator(EVENT_BOOKING_REF_LOCATOR)
                .filter(new Locator.FilterOptions().setHasText(System.getProperty("BOOKING_REF")));

        assertThat(getBookingId).isVisible();
        return getBookingId.isVisible();
    }
}