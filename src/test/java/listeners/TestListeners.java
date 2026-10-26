package listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.TestBase;
import util.LoggerUtility;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * TestNG listener for handling test execution events and Allure reporting.
 *
 * <p>Logs test execution details and integrates with Allure for reporting and screenshot attachments.</p>
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class TestListeners implements ITestListener {
    Logger logger = LoggerUtility.getLogger(this.getClass());

    /**
     * Called when a test starts. Logs method name, description, and assigned groups.
     *
     * @param result the result of the test method that is starting
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test Started: " + result.getMethod().getMethodName());
        logger.info("Description: " + result.getMethod().getDescription());
        logger.info("Groups: " + Arrays.toString(result.getMethod().getGroups()));
    }

    /**
     * Called when a test passes successfully. Logs the success status.
     *
     * @param result the result of the test method that passed
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info(result.getMethod().getMethodName() + " PASSED");
    }

    /**
     * Called when a test fails. Captures the failure reason, retrieves the thread-isolated
     * Playwright page, takes a full-page screenshot, and attaches it directly to the Allure report.
     *
     * @param result the result of the test method that failed
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error(result.getMethod().getMethodName() + " FAILED");
        if (result.getThrowable() != null) {
            logger.error(result.getThrowable().getMessage());
        }

        try {
            Object testClass = result.getInstance();
            if (testClass instanceof TestBase) {
                // Retrieve the thread-safe Playwright Page instance for parallel execution
                Page page = ((TestBase) testClass).getPage();

                if (page != null) {
                    logger.info("Capturing Playwright Screenshot for the failed test.");
                    byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));

                    // Attach screenshot using Allure's standard attachment method
                    Allure.attachment(
                            result.getMethod().getMethodName() + " - Failed Screenshot",
                            new ByteArrayInputStream(screenshotBytes)
                    );
                    logger.info("Successfully attached Screenshot to Allure report.");
                } else {
                    logger.warn("Playwright Page instance was null; could not capture screenshot.");
                }
            }
        } catch (Exception e) {
            logger.error("Error while handling test failure and attaching screenshot: ", e);
        }
    }

    /**
     * Called when a test is skipped. Logs the skip warning.
     *
     * @param result the result of the test method that was skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn(result.getMethod().getMethodName() + " SKIPPED");
    }

    /**
     * Called before any tests in the suite run. Initializes suite-level logging.
     *
     * @param context the test context
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started");
    }

    /**
     * Called after all tests in the suite have completed. Finalizes suite execution logs.
     *
     * @param context the test context
     */
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Completed");
    }
}