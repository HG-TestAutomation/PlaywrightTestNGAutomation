package utils;

import factory.BaseTest;
import com.microsoft.playwright.BrowserContext;
import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.*;
import java.nio.file.*;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            BrowserContext context = ((BaseTest) testInstance).context;
            if (context != null) {
                try {
                    String testName = result.getName();
                    String traceFile = "target/allure-results/" + testName + "-trace.zip";

                    // Stop tracing and save trace file
                    System.out.println("🛑 Stopping tracing for failure; saving trace to: " + traceFile);
                    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(Paths.get(traceFile)));
                    File trace = new File(traceFile);
                    if (trace.exists()) {
                        try (InputStream is = new FileInputStream(trace)) {
                            Allure.addAttachment("Playwright Trace", "application/zip", is, "zip");
                            System.out.println("✅ Trace attached to Allure.");
                        }
                    }

                    // Get the latest video file from the video folder
                    File videoFolder = new File("target/playwright-videos/");
                    String[] videos = videoFolder.list((dir, name) -> name.endsWith(".webm"));
                    if (videos != null && videos.length > 0) {
                        // Here, we assume the latest video is the one with the highest sort order;
                        // you can add more robust logic if needed.
                        String videoFile = "target/playwright-videos/" + videos[videos.length - 1];
                        System.out.println("✅ Video file selected: " + videoFile);

                        File video = new File(videoFile);
                        if (video.exists()) {
                            File videoDest = new File("target/allure-results/" + video.getName());
                            Files.copy(video.toPath(), videoDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("✅ Video copied to: " + videoDest.getAbsolutePath());

                            try (InputStream is = new FileInputStream(videoDest)) {
                                Allure.addAttachment("Playwright Video", "video/webm", is, "webm");
                                System.out.println("✅ Video attached to Allure.");
                            }
                        } else {
                            System.out.println("❌ Video file not found: " + videoFile);
                        }
                    } else {
                        System.out.println("❌ No video files found in target/playwright-videos/");
                    }
                } catch (Exception e) {
                    System.out.println("Error in onTestFailure: " + e.getMessage());
                }
            }
        }
    }

    // (Other listener methods can remain empty if not needed)
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
