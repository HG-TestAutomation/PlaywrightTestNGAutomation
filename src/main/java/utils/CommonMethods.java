package utils;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.*;
import java.nio.file.Paths;

public class CommonMethods {
    private final Page page;  // ✅ Properly assigned in the constructor

    public CommonMethods(Page page) {
        if (page == null) {
            throw new IllegalArgumentException("Page instance cannot be null");
        }
        this.page = page;
    }

    public void clickElement(String selector) {
        page.locator(selector).click();
    }

    public void enterText(String selector, String text) {
        page.locator(selector).fill(text);
    }

    public void takeScreenshot(String testName) {
        try {
            // Define the screenshot path
            String screenshotPath = "target/allure-results/" + testName + ".png";  // ✅ Save to target/allure-results

            // Capture the screenshot and save it
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true)); // Ensure full page is captured

            // Attach screenshot to Allure report
            Allure.addAttachment("Screenshot - " + testName, "image/png", new ByteArrayInputStream(screenshotBytes), ".png");

            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attachTraceToAllure(String tracePath) {
        File traceFile = new File(tracePath);
        if (traceFile.exists()) {
            try (FileInputStream fis = new FileInputStream(traceFile)) {
                Allure.addAttachment("Playwright Trace", "application/zip", fis, "zip");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

