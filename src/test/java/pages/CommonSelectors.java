package pages;

import archieve.BasicTest;
import tests.TestBase;

/**
 * Utility class holding common UI selector constants and locators
 * shared across multiple page object classes within the application.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class CommonSelectors extends TestBase {

    /** Locator for the navigation bar events button/link. */
    protected static final String NAVBAR_EVENTS = "#nav-events";

    /** Locator for individual event cards displayed on the UI. */
    protected static final String EVENTS_CARD_LOCATOR = "#event-card";

    /** Locator for the navigation bar bookings button/link. */
    protected static final String NAVBAR_BOOKINGS = "#nav-bookings";

    /** Locator for extracting the generated booking reference string. */
    protected static final String EVENT_BOOKING_REF_LOCATOR = ".booking-ref";
}