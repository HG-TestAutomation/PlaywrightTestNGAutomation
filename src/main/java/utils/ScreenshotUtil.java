package utils;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;

public class ScreenshotUtil {

    public static void captureScreenshot(Page page, String filePath) {
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
