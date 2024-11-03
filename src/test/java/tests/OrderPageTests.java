package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.HomePageScooter;
import pageobjects.OrderPageScooter;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderPageTests {
    private WebDriver driver;
    private HomePageScooter homePage;
    private OrderPageScooter orderPage;
    private String name;
    private String surname;
    private String address;
    private String metroStation;
    private String phone;
    private String browser;
    private By finalScreen = By.className("Order_Modal__YZ-d3");

    public OrderPageTests(String name, String surname, String address, String metroStation, String phone) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
    }

    @Before
    public void setUp() {
        browser = System.getProperty("browser", "chrome");
        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty("webdriver.firefox.bin", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
            driver = new FirefoxDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        homePage = new HomePageScooter(driver);
        orderPage = new OrderPageScooter(driver);

        // Инициализация страницы
        initPage();
    }

    private void initPage() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        orderPage.closeCookieBanner();
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
        orderPage.clickOrderButtonFromHeader();

        // Заполнение формы
        orderPage.fillOrderForm(name, surname, address, metroStation, phone);
        orderPage.clickNextButton();

        // Выбор срока аренды
        orderPage.selectNextDay();
        orderPage.selectRentalPeriod();

        // Завершение оформления заказа
        orderPage.submitOrder();
        orderPage.clickYesButton();

        WebElement orderModal = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(finalScreen));
        assertTrue("Окно заказа не отображается.", orderModal.isDisplayed());
    }

    @Test
    public void testOrderFromBottomButton() {
        // Прокрутка до кнопки заказа снизу
        WebElement thirdPartElement = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Home_ThirdPart__LSTEE")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", thirdPartElement);

        orderPage.clickOrderButtonFromBottom();

        // Заполнение формы
        orderPage.fillOrderForm(name, surname, address, metroStation, phone);
        orderPage.clickNextButton();

        // Выбор срока аренды
        orderPage.selectNextDay();
        orderPage.selectRentalPeriod();

        // Завершение оформления заказа
        orderPage.submitOrder();
        orderPage.clickYesButton();

        //Перепробовала все возможные варианты проверки, не могу разобраться с тем, почему в хроме тесты успешные
        WebElement orderModal = new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(finalScreen));
        assertTrue("Окно заказа не отображается.", orderModal.isDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}