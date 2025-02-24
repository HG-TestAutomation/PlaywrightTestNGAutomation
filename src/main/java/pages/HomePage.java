package pages;
import com.microsoft.playwright.Page;

public class HomePage {
    private final Page page;

    private final String usernameField = "input[name='username']";
    private final String passwordField = "input[name='password']";
    private final String loginButton = "button#login";

    public HomePage(Page page) {
        this.page = page;
    }

    public void login(String username, String password) {
        page.locator(usernameField).fill(username);
        page.locator(passwordField).fill(password);
        page.locator(loginButton).click();
    }
}
