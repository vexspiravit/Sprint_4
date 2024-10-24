package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class OrderPageTestsChrome {

    private WebDriver driver;
    private String name;
    private String surname;
    private String address;
    private String metroStation;
    private String phone;

    public OrderPageTestsChrome(String name, String surname, String address, String metroStation, String phone) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"Иван", "Иванов", "Москва, ул. Пушкина, д. 1", "Лубянка", "+79995410488"},
                {"Мария", "Сидорова", "Москва, ул. Ленина, д. 2", "Красносельская", "+79995220101"}
        });
    }

    @Test
    public void testOrderFromHeader() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        closeCookieBanner();

        WebElement orderButtonTop = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.className("Button_Button__ra12g")));
        orderButtonTop.click();

        fillOrderForm(name, surname, address, metroStation, phone);

        WebElement nextButton = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div[3]/button")));
        nextButton.click();

        selectNextDay();

        WebElement dropdownRoot = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("#root > div > div.Order_Content__bmtHS > div.Order_Form__17u6u > div.Dropdown-root")));
        dropdownRoot.click();

        WebElement specifiedElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]/div[2]/div[1]")));
        specifiedElement.click();

        WebElement additionalElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]")));
        additionalElement.click();

        // Клик по кнопке //*[@id="root"]/div/div[2]/div[3]/button[2]
        WebElement orderButton = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[3]/button[2]")));
        orderButton.click();

        WebElement orderModal = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));
        if (!orderModal.isDisplayed()) throw new AssertionError();
    }

    //Эти тесты получились нестабильными, иногда ломаются на этапе скролла, но я так и не смогла понять, в чем проблема
    @Test
    public void testOrderFromBottomButton() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        closeCookieBanner();

        WebElement thirdPartElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Home_ThirdPart__LSTEE")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", thirdPartElement);

        WebElement orderButtonBottom = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.className("Button_Button__ra12g")));
        orderButtonBottom.click();

        fillOrderForm(name, surname, address, metroStation, phone);

        WebElement nextButton = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div[2]/div[3]/button")));
        nextButton.click();

        selectNextDay();

        WebElement dropdownRoot = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("#root > div > div.Order_Content__bmtHS > div.Order_Form__17u6u > div.Dropdown-root")));
        dropdownRoot.click();

        WebElement specifiedElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]/div[2]/div[1]")));
        specifiedElement.click();

        WebElement additionalElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[2]/div[2]")));
        additionalElement.click();

        // Клик по кнопке //*[@id="root"]/div/div[2]/div[3]/button[2]
        WebElement orderButton = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[3]/button[2]")));
        orderButton.click();

        // Клик по кнопке //*[@id="root"]/div/div[2]/div[5]/div[2]/button[2]
        WebElement finalButton = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div/div[2]/div[5]/div[2]/button[2]")));
        finalButton.click();


        WebElement orderModal = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));
        assertTrue("Окно заказа не отображается.", orderModal.isDisplayed());


    }

    private void fillOrderForm(String name, String surname, String address, String metroStation, String phone) {
        WebElement nameInput = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Имя']")));
        nameInput.sendKeys(name);

        WebElement surnameInput = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Фамилия']")));
        surnameInput.sendKeys(surname);

        WebElement addressInput = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']")));
        addressInput.sendKeys(address);

        WebElement metroStationInput = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Станция метро']")));
        metroStationInput.click();

        selectMetroStation(metroStation);

        WebElement phoneInput = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']")));
        phoneInput.sendKeys(phone);
    }

    private void selectMetroStation(String metroStationName) {
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

    private void selectNextDay() {
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

    private void closeCookieBanner() {
        try {
            WebElement cookieCloseButton = new WebDriverWait(driver, 5)
                    .until(ExpectedConditions.elementToBeClickable(By.className("App_CookieButton__3cvqF")));
            cookieCloseButton.click();
        } catch (Exception e) {
            // Игнорируем, если баннер не появился
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}