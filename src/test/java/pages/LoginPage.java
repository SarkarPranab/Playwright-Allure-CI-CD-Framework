package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Page Object class representing the Login page of the application.
 * Handles user authentication, credential input, title verification,
 * and successful login redirection.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class LoginPage {
    private final Page page;

    private static final String EMAIL_FIELD_LABEL = "Email";
    private static final String PASSWORD_FIELD_LABEL = "Password";
    private static final String SIGN_IN_BUTTON_TEXT = "Sign In";
    private static final String CONFIRM_PAGE_TITLE = "EventHub — Discover & Book Events";
    private static final String CONFIRM_DASHBOARD_PAGE_TEXT = "Browse Events →";

    /**
     * Constructs a LoginPage instance with the specified Playwright page.
     *
     * @param page the thread-safe Playwright Page instance
     */
    public LoginPage(Page page) {
        this.page = page;
    }

    /**
     * Authenticates the user into the application by validating the page title,
     * filling in credentials, clicking sign-in, verifying the dashboard redirection text,
     * and returning the dashboard page object.
     *
     * @param username the user email or username for login
     * @param password the password for authentication
     * @return a new instance of {@link DashboardPage} representing the destination page
     */
    public DashboardPage loginToApplication(String username, String password) {
        assertThat(page).hasTitle(CONFIRM_PAGE_TITLE);
        page.getByLabel(EMAIL_FIELD_LABEL).fill(username);
        page.getByLabel(PASSWORD_FIELD_LABEL).fill(password);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(SIGN_IN_BUTTON_TEXT)).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(CONFIRM_DASHBOARD_PAGE_TEXT))).isVisible();

        return new DashboardPage(page);
    }
}