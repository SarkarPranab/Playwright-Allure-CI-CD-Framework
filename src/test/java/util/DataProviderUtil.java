package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for reading and parsing JSON test data files into TestNG-compatible
 * data provider formats using Gson.
 *
 * <p>Author: Pranab Sarkar</p>
 * <p>LinkedIn: <a href="https://www.linkedin.com/in/pranabsarkar/">https://www.linkedin.com/in/pranabsarkar/</a></p>
 * <p>Portfolio: <a href="https://pranabsarkar.vercel.app/">https://pranabsarkar.vercel.app/</a></p>
 *
 * @since 2026-10-26
 */
public class DataProviderUtil {

    /**
     * Reads a JSON file from the specified relative file path and converts its content
     * into a two-dimensional Object array of HashMaps suitable for TestNG DataProviders.
     *
     * @param filePath the relative path to the JSON data file (e.g., "\\src\\test\\resources\\events.json")
     * @return a two-dimensional {@link Object} array where each row contains a data {@link HashMap}
     * @throws RuntimeException if the file cannot be read, found, or parsed successfully
     */
    public static Object[][] getJsonDataToMap(String filePath) {
        try {
            String jsonContent = new String(Files.readAllBytes(
                    Paths.get(
                            System.getProperty("user.dir") + filePath
                    )
            ));

            Type type = new TypeToken<List<HashMap<String, String>>>() {
            }.getType();

            List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);

            Object[][] table = new Object[list.size()][1];
            for (int i = 0; i < list.size(); i++) {
                table[i][0] = list.get(i);
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load or parse JSON data from path: " + filePath, e);
        }
    }
}