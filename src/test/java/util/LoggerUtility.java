package util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class responsible for providing centralized Log4j Logger instances
 * across the automation framework.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class LoggerUtility {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LoggerUtility() {
    }

    /**
     * Retrieves a configured Log4j Logger instance for the specified class.
     *
     * @param clazz The Class object requesting the logger.
     * @return A Log4j Logger instance bound to the given class.
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = null;
        if (logger == null) {
            logger = LogManager.getLogger(clazz);
        }
        return logger;
    }
}