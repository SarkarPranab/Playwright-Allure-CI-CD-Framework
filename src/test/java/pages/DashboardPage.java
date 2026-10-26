package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page Object class representing the application Dashboard page.
 * Handles navigation from the main dashboard to administrative event management features.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class DashboardPage {
    private final Page page;

    private static final String NAV_BAR_ADMIN_BUTTON_TEXT = "Admin";
    private static final String MANAGE_EVENTS_LOCATOR = "//div/a[contains(text(),'Manage Events')]";

    /**
     * Constructs a DashboardPage instance with the specified Playwright page.
     *
     * @param page the thread-safe Playwright Page instance
     */
    public DashboardPage(Page page) {
        this.page = page;
    }

    /**
     * Navigates from the dashboard to the administrative event management page
     * by clicking the Admin button and selecting the Manage Events option.
     *
     * @return a new instance of {@link AdminEventsPage} representing the destination page
     */
    public AdminEventsPage goToAdminEventPage() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(NAV_BAR_ADMIN_BUTTON_TEXT)).click();
        page.locator(MANAGE_EVENTS_LOCATOR).click();
        return new AdminEventsPage(page);
    }
}