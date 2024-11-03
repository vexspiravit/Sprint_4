package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrderPageScooter {
    private WebDriver driver;

    // Локаторы
    private By orderButtonTop = By.className("Button_Button__ra12g");
    private By orderButtonBottom = By.className("Button_Button__ra12g");
    private By nameInput = By.xpath("//*[@id='root']/div/div[2]/div[2]/div[1]/input");
    private By surnameInput = By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]/input");
    private By addressInput = By.xpath("//*[@id='root']/div/div[2]/div[2]/div[3]/input");
    private By metroStationInput = By.xpath("//*[@id='root']/div/div[2]/div[2]/div[4]/div/div"); // обновленный локатор для станции метро
    private By phoneInput = By.xpath("//*[@id='root']/div/div[2]/div[2]/div[5]/input"); // обновленный локатор для телефона
    private By nextButton = By.cssSelector("#root > div > div.Order_Content__bmtHS > div.Order_NextButton__1_rCA > button");
    private By submitButton = By.xpath("//*[@id='root']/div/div[2]/div[3]/button[2]");
    private By successMessage = By.className("Order_Notice__3k1yt");
    private By cookieCloseButton = By.className("App_CookieButton__3cvqF");
    private By yesButton = By.xpath("//*[@id='root']/div/div[2]/div[5]/div[2]/button[2]");

    public OrderPageScooter(WebDriver driver) {
        this.driver = driver;
    }

    public void closeCookieBanner() {
        try {
            WebElement cookieElement = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.elementToBeClickable(cookieCloseButton));
            cookieElement.click();
        } catch (Exception e) {
            // Игнорируем, если баннер не появился или кнопка не кликабельна
        }
    }

    public void clickOrderButtonFromHeader() {
        WebElement orderButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(orderButtonTop));
        orderButton.click();
    }

    public void clickOrderButtonFromBottom() {
        WebElement orderButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(orderButtonBottom));
        orderButton.click();
    }

    public void fillOrderForm(String name, String surname, String address, String metroStation, String phone) {
        fillName(name);
        fillSurname(surname);
        fillAddress(address);
        selectMetroStation(metroStation);
        fillPhone(phone);
    }

    private void fillName(String name) {
        WebElement nameElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameElement.sendKeys(name);
    }

    private void fillSurname(String surname) {
        WebElement surnameElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(surnameInput));
        surnameElement.sendKeys(surname);
    }

    private void fillAddress(String address) {
        WebElement addressElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(addressInput));
        addressElement.sendKeys(address);
    }

    public void selectMetroStation(String metroStationName) {
        WebElement metroStationInputElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(metroStationInput));
        metroStationInputElement.click();

        WebElement stationList = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[4]/div/div[2]")));

        WebElement metroStation = stationList.findElement(By.xpath(".//div[contains(text(), '" + metroStationName + "')]"));
        try {
            metroStation.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", metroStation);
        }
    }

    public void selectNextDay() {
        LocalDate nextDay = LocalDate.now().plusDays(1);
        String dayOfWeek = nextDay.format(DateTimeFormatter.ofPattern("EEEE", new Locale("ru")));
        String monthYear = nextDay.format(DateTimeFormatter.ofPattern("d'-е' MMMM yyyy г.", new Locale("ru")));
        String ariaLabelDate = "Choose " + dayOfWeek + ", " + monthYear;

        WebElement datePicker = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker-wrapper")));
        datePicker.click();

        WebElement dateElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='" + ariaLabelDate + "']")));
        dateElement.click();
    }

    private void fillPhone(String phone) {
        WebElement phoneElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        phoneElement.sendKeys(phone);
    }

    public void clickNextButton() {
        WebElement nextButtonElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(nextButton));
        nextButtonElement.click();
    }

    public void clickYesButton() {
        WebElement yesButtonElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(yesButton));
        yesButtonElement.click();
    }

    public void selectRentalPeriod() {
        WebElement rentalPeriodDropdown = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]")));
        rentalPeriodDropdown.click();

        WebElement specificRentalPeriodOption = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]/div[2]/div[2]")));
        specificRentalPeriodOption.click();
    }

    public void submitOrder() {
        WebElement submitButtonElement = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButtonElement.click();
    }

    public String getSuccessMessage() {
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }
}