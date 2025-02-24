package tests;

import factory.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

@Epic("Homepage Tests")
@Feature("Page Title Verification")
@Listeners({io.qameta.allure.testng.AllureTestNg.class, utils.TestListener.class})
public class HomepageTests extends BaseTest {

    @Test
    @Story("Verify Homepage Title")
    @Description("Navigates to the homepage and verifies the title")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Navigate to homepage and verify title")
    public void shouldShowThePageTitle() throws InterruptedException {
        // Navigate to the URL and wait for load
        page.navigate("https://practicesoftwaretesting.com",
                new com.microsoft.playwright.Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.LOAD));
        // Let the page run for a bit to record a longer video
        Thread.sleep(5000);

        String title = page.title();
        System.out.println("🔎 Page Title: " + title);
        Allure.addAttachment("Page Title", title);
        Thread.sleep(5000);
        // Change expected text to match the actual title on your page
        Assert.assertTrue(title.contains("Practice Software BUTT"), "Title does not match expected value");
    }
}
