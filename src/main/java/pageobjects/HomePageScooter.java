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

    public HomePageScooter(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10); // Увеличиваем время ожидания до 10 секунд
    }

    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Метод для открытия FAQ вопроса по индексу
    public void expandFAQSectionByIndex(int index) {
        scrollToElement(By.className("Home_FourPart__1uthg"));
        List<WebElement> buttons = driver.findElements(By.className("accordion__button"));

        if (index < 0 || index >= buttons.size()) {
            throw new IllegalArgumentException("Некорректный индекс вопроса: " + index);
        }

        WebElement button = buttons.get(index);
        button.click();

        try {
            // Ожидаем появления текста ответа по индексу, а не только панели
            String xpath = String.format("//*[@id='accordion__panel-%d']/p", index);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось найти панель с текстом после нажатия кнопки FAQ: " + button.getText(), e);
        }
    }

    // Метод для получения текста ответа по индексу
    public String getFAQAnswerTextByIndex(int index) {
        String xpath = String.format("//*[@id='accordion__panel-%d']/p", index);
        WebElement answer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return answer.getText();
    }
}


