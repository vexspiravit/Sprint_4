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
    private WebDriverWait wait;

    // Поля для данных пользователя
    private String name;
    private String surname;
    private String address;
    private String metroStation;
    private String phone;

    // Кнопка заказа сверху и снизу одинаковая, поэтому один локатор
    private By orderButtonHeader = By.className("Button_Button__ra12g");
    private By orderButtonBottom = By.cssSelector(".Home_ThirdPart__LSTEE .Home_FinishButton__1_cWm button");

    // Поля ввода формы
    private By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroStationInput = By.xpath("//input[@placeholder='* Станция метро']");
    private By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка "Далее"
    private By nextButton = By.cssSelector(".Order_NextButton__1_rCA button");

    // Кнопка подтверждения заказа
    private By submitButton = By.cssSelector(".Order_Buttons__1xGrp > button:nth-child(2)");

    // Кнопка для закрытия уведомления о cookie
    private By cookieCloseButton = By.className("App_CookieButton__3cvqF");

    // Кнопка "Да" в модальном окне подтверждения заказа
    private By yesButton = By.cssSelector(".Order_Modal__YZ-d3 .Order_Buttons__1xGrp > button:nth-child(2)");

    // Модальное окно финального экрана заказа
    private By finalScreen = By.xpath("//div[text()='Заказ оформлен']");

    private By scrollBrick = By.className("Home_FinishButton__1_cWm");

    public By getFinalScreen() {
        return finalScreen;
    }

    // Обновленный конструктор для инициализации данных пользователя
    public OrderPageScooter(WebDriver driver, String name, String surname, String address, String metroStation, String phone) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);  // Установить ожидание для элементов
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
    }

    public void closeCookieBanner() {
        try {
            WebElement cookieElement = wait.until(ExpectedConditions.elementToBeClickable(cookieCloseButton));
            cookieElement.click();
        } catch (Exception e) {
            // Игнорируем, если баннер не появился или кнопка не кликабельна
        }
    }

    // Прокрутка до кнопки заказа снизу
    public void scrollToOrderButton() {
        WebElement thirdPartElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(scrollBrick));
        ((JavascriptExecutor) driver).

                executeScript("arguments[0].scrollIntoView(true);", thirdPartElement);
    }

    public void clickOrderButtonHeader() {
        WebElement orderButtons = wait.until(ExpectedConditions.elementToBeClickable(orderButtonHeader));
        orderButtons.click();
    }

    public void clickOrderButtonBottom() {
        WebElement orderButtons = wait.until(ExpectedConditions.elementToBeClickable(orderButtonBottom));
        orderButtons.click();
    }

    public void fillName() {
        WebElement nameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameElement.sendKeys(this.name);
    }

    public void fillSurname() {
        WebElement surnameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(surnameInput));
        surnameElement.sendKeys(this.surname);
    }

    public void fillAddress() {
        WebElement addressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput));
        addressElement.sendKeys(this.address);
    }

    public void selectMetroStation() {
        WebElement metroStationInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(metroStationInput));
        metroStationInputElement.click();

        WebElement stationList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[4]/div/div[2]")));
        WebElement metroStationElement = stationList.findElement(By.xpath(".//div[contains(text(), '" + this.metroStation + "')]"));

        try {
            metroStationElement.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", metroStationElement);
        }
    }

    public void selectNextDay() {
        LocalDate nextDay = LocalDate.now().plusDays(1);
        String dayOfWeek = nextDay.format(DateTimeFormatter.ofPattern("EEEE", new Locale("ru")));
        String monthYear = nextDay.format(DateTimeFormatter.ofPattern("d'-е' MMMM yyyy г.", new Locale("ru")));
        String ariaLabelDate = "Choose " + dayOfWeek + ", " + monthYear;

        WebElement datePicker = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker-wrapper")));
        datePicker.click();

        WebElement dateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='" + ariaLabelDate + "']")));
        dateElement.click();
    }

    public void fillPhone() {
        WebElement phoneElement = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        phoneElement.sendKeys(this.phone);
    }

    public void clickNextButton() {
        WebElement nextButtonElement = wait.until(ExpectedConditions.visibilityOfElementLocated(nextButton));
        nextButtonElement.click();
    }

    public void clickYesButton() {
        WebElement yesButtonElement = wait.until(ExpectedConditions.visibilityOfElementLocated(yesButton));
        yesButtonElement.click();
    }

    public void selectRentalPeriod() {
        WebElement rentalPeriodDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]")));
        rentalPeriodDropdown.click();

        WebElement specificRentalPeriodOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]/div[2]/div[2]")));
        specificRentalPeriodOption.click();
    }

    public void submitOrder() {
        WebElement submitButtonElement = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButtonElement.click();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(finalScreen)).getText();
    }
}