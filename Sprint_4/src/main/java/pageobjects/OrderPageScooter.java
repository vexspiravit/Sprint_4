package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPageScooter {
    private WebDriver driver;

    private By orderButtonTop = By.className("Button_Button__ra12g");
    private By orderButtonBottom = By.className("Button_Button__ra12g");
    private By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroStationInput = By.xpath("//input[@placeholder='* Станция метро']");
    private By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private By nextButton = By.xpath("//*[@id='root']/div/div[2]/div[3]/button");
    private By successMessage = By.className("Order_Notice__3k1yt");

    public OrderPageScooter(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickOrderButtonBottom() {
        driver.findElement(orderButtonBottom).click();
    }

    public void fillOrderForm(String name, String surname, String address, String metroStation, String phone) {
        WebElement nameElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameElement.sendKeys(name);

        WebElement surnameElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(surnameInput));
        surnameElement.sendKeys(surname);

        WebElement addressElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(addressInput));
        addressElement.sendKeys(address);

        WebElement metroStationElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(metroStationInput));
        metroStationElement.click();
        metroStationElement.sendKeys(metroStation);

        // Явное ожидание появления выпадающего списка с выбором станции
        WebElement selectedStation = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='select-search__select']//div[contains(text(), '" + metroStation + "')]")));

        // Попробуй выполнить клик с помощью JavaScript, если обычный клик не работает
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", selectedStation);

        WebElement phoneElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        phoneElement.sendKeys(phone);
    }

    public void clickSubmitButton() {
        WebElement submitButtonElement = new WebDriverWait(driver, 20)
                .until(ExpectedConditions.visibilityOfElementLocated(nextButton));

        assertButtonClass(submitButtonElement, "Button_Button__ra12g Button_Middle__1CSJM");
        submitButtonElement.click();
    }

    private void assertButtonClass(WebElement button, String expectedClass) {
        String actualClass = button.getAttribute("class");
        if (!actualClass.equals(expectedClass)) {
            throw new AssertionError("Expected class: " + expectedClass + ", but found: " + actualClass);
        }
    }

    public String getSuccessMessage() {
        return driver.findElement(successMessage).getText();
    }
}