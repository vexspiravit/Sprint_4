package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePageScooter {
    private WebDriver driver;
    private WebDriverWait wait;

    // Локаторы
    private By faqSection = By.className("Home_FourPart__1uthg");
    private By accordionButtons = By.className("accordion__button");
    private String accordionPanelXpath = "//*[@id='accordion__panel-%d']/p";

    public HomePageScooter(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10); // Увеличиваем время ожидания до 10 секунд
    }

    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void expandFAQSectionByIndex(int index) {
        scrollToElement(faqSection); // Используем вынесенный локатор faqSection
        List<WebElement> buttons = driver.findElements(accordionButtons); // Используем вынесенный локатор accordionButtons

        if (index < 0 || index >= buttons.size()) {
            throw new IllegalArgumentException("Некорректный индекс вопроса: " + index);
        }

        WebElement button = buttons.get(index);
        button.click();

        try {
            String xpath = String.format(accordionPanelXpath, index);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось найти панель с текстом после нажатия кнопки FAQ: " + button.getText(), e);
        }
    }

    public String getFAQAnswerTextByIndex(int index) {
        String xpath = String.format(accordionPanelXpath, index);
        WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return answer.getText();
    }
}



