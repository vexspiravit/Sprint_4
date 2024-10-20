package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import pageobjects.HomePageScooter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class HomePageTestsFireFox {
    private WebDriver driver;
    private HomePageScooter homePage;
    private int index;
    private static Map<Integer, String> expectedAnswers = new HashMap<>();

    // Заполняем карту с ожидаемыми ответами
    static {
        expectedAnswers.put(0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой.");
        expectedAnswers.put(1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.");
        expectedAnswers.put(2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.");
        expectedAnswers.put(3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее.");
        expectedAnswers.put(4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.");
        expectedAnswers.put(5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.");
        expectedAnswers.put(6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.");
        expectedAnswers.put(7, "Да, обязательно. Всем самокатов! И Москве, и Московской области.");
    }

    public HomePageTestsFireFox(int index) {
        this.index = index;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {0}, // Проверка 1-го вопроса
                {1}, // Проверка 2-го вопроса
                {2}, // Проверка 3-го вопроса
                {3}, // Проверка 4-го вопроса
                {4}, // Проверка 5-го вопроса
                {5}, // Проверка 6-го вопроса
                {6}, // Проверка 7-го вопроса
                {7}  // Проверка 8-го вопроса
        });
    }

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        // Укажите путь к Firefox на Mac OS
        System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        homePage = new HomePageScooter(driver);
    }

    @Test
    public void testFAQSection() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        homePage.expandFAQSectionByIndex(index);

        // Получаем текст ответа по индексу
        String actualAnswerText = homePage.getFAQAnswerTextByIndex(index);

        // Получаем ожидаемый текст из карты
        String expectedAnswerText = expectedAnswers.get(index);

        // Проверяем совпадение текстов
        Assert.assertEquals("Текст ответа не совпадает с ожидаемым", expectedAnswerText, actualAnswerText);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
