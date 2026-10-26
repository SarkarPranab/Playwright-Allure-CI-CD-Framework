package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Base test class managing the test lifecycle, environment configuration,
 * and thread-safe initialization and cleanup of Playwright instances for parallel execution.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class TestBase {

    /** ThreadLocal instance ensuring isolated Playwright execution per thread. */
    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();

    /** ThreadLocal instance ensuring isolated Browser execution per thread. */
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();

    /** ThreadLocal instance ensuring isolated Page execution per thread. */
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    /** Application configuration properties loaded from the configuration file. */
    protected Properties properties;

    /** Current test execution environment (e.g., qa, dev, prod). */
    protected String env;

    /**
     * Sets up the test environment before each test method. Initializes thread-safe
     * Playwright, launches the configured browser instance, creates a new page,
     * and navigates to the base URL.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browserName = System.getProperty("browser") != null
                ? System.getProperty("browser")
                : getProperties().getProperty("browser");

        this.env = System.getProperty("env") != null
                ? System.getProperty("env")
                : getProperties().getProperty("env");
        String headless = System.getProperty("headless") != null
                ? System.getProperty("headless")
                : getProperties().getProperty("headless");

        // Initialize Playwright for the current thread
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        // Launch Browser based on configuration
        Browser browser;
        if ("firefox".equals(browserName)) {
            browser = playwright.firefox().launch();
        } else if ("safari".equals(browserName)) {
            browser = playwright.webkit().launch();
        } else {
            if(Boolean.parseBoolean(headless)){
                browser = playwright.chromium().launch();
            }else{
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            }

        }
        browserThreadLocal.set(browser);

        // Create a new isolated page for the thread and navigate
        Page page = browser.newPage();
        pageThreadLocal.set(page);

        page.navigate(getProperties().getProperty(env + ".base.url"));
    }

    /**
     * Loads and returns application configuration properties from the properties file.
     *
     * @return a {@link Properties} object containing configuration settings
     * @throws RuntimeException if the properties file cannot be found or loaded
     */
    public Properties getProperties() {
        try {
            this.properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
            properties.load(fileInputStream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the thread-safe Page instance for the current execution thread.
     *
     * @return the active thread-isolated {@link Page} instance
     */
    public Page getPage() {
        return pageThreadLocal.get();
    }

    /**
     * Cleans up and closes thread-local Playwright resources after each test method
     * to prevent memory leaks and thread collisions during parallel execution.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // Clean up thread-local resources to prevent memory leaks and thread collisions
        if (pageThreadLocal.get() != null) {
            pageThreadLocal.get().close();
            pageThreadLocal.remove();
        }
        if (browserThreadLocal.get() != null) {
            browserThreadLocal.get().close();
            browserThreadLocal.remove();
        }
        if (playwrightThreadLocal.get() != null) {
            playwrightThreadLocal.get().close();
            playwrightThreadLocal.remove();
        }
    }
}