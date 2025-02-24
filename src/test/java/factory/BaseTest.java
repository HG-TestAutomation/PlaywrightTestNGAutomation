package factory;

import com.microsoft.playwright.*;
import org.testng.annotations.*;
import java.nio.file.Paths;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    public BrowserContext context;
    protected Page page;

    @Parameters("browserType")
    @BeforeMethod
    public void setup(@Optional("chromium") String browserType) {
        System.out.println("🚀 Initializing Playwright for browser: " + browserType);
        playwright = Playwright.create();
        boolean headless = false; // change as needed
        int timeout = 10000;

        // Launch the browser based on the parameter value
        switch (browserType.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
        }

        // Create a new browser context with video recording enabled
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1280, 800)
                .setRecordVideoDir(Paths.get("target/playwright-videos"))
                .setRecordVideoSize(1280, 720)
        );

        // Start tracing (for screenshots, snapshots, and sources)
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );

        // Open a new page and set default timeout
        page = context.newPage();
        page.setDefaultTimeout(timeout);
        System.out.println("✅ Playwright setup complete.");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        System.out.println("🛑 Tearing down Playwright...");
        try {
            if (context != null) {
                // Stop tracing (attach trace in the listener, if needed)
                context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/allure-results/trace.zip")));
                context.close();
                System.out.println("✅ Browser context closed.");
            }
            if (browser != null) {
                browser.close();
                System.out.println("✅ Browser closed.");
            }
            if (playwright != null) {
                playwright.close();
                System.out.println("✅ Playwright closed.");
            }
        } catch (Exception e) {
            System.out.println("Error during teardown: " + e.getMessage());
        }
    }
}
