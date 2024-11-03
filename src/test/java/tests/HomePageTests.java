package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import pageobjects.HomePageScooter;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HomePageTests {

    private WebDriver driver;
    private HomePageScooter homePage;
    private int index;
    private String browser;

    private static final String ANSWER_0 = "Сутки — 400 рублей. Оплата курьеру — наличными или картой.";
    private static final String ANSWER_1 = "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.";
    private static final String ANSWER_2 = "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.";
    private static final String ANSWER_3 = "Только начиная с завтрашнего дня. Но скоро станем расторопнее.";
    private static final String ANSWER_4 = "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.";
    private static final String ANSWER_5 = "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.";
    private static final String ANSWER_6 = "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.";
    private static final String ANSWER_7 = "Да, обязательно. Всем самокатов! И Москве, и Московской области.";

    public HomePageTests(int index) {
        this.index = index;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}
        });
    }

    @Before
    public void setUp() {
        browser = System.getProperty("browser", "firefox"); // Если переменная не задана, используем Chrome по умолчанию

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
    }

    @Test
    public void faqSectionTest() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        homePage.expandFAQSectionByIndex(index);
        String actualAnswerText = homePage.getFAQAnswerTextByIndex(index);
        String expectedAnswerText = getExpectedAnswer(index);
        Assert.assertEquals("Текст ответа не совпадает с ожидаемым", expectedAnswerText, actualAnswerText);
    }

    private String getExpectedAnswer(int index) {
        switch (index) {
            case 0: return ANSWER_0;
            case 1: return ANSWER_1;
            case 2: return ANSWER_2;
            case 3: return ANSWER_3;
            case 4: return ANSWER_4;
            case 5: return ANSWER_5;
            case 6: return ANSWER_6;
            case 7: return ANSWER_7;
            default: throw new IllegalArgumentException("Некорректный индекс вопроса: " + index);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

