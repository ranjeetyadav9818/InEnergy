package com.inenergis.customer;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
/**
 * Created by Antonio on 31/03/2017.
 */
public class SearchCustomerTest {


    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.gecko.driver", "/etc/gecko/geckodriver");
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testSeleniumJava() throws Exception {
        driver.get(baseUrl + "/DRCC/dashboard.jsf");
        driver.findElement(By.id("form:j_username")).clear();
        driver.findElement(By.id("form:j_username")).sendKeys("admin@inenergis.com");
        driver.findElement(By.id("form:j_password")).clear();
        driver.findElement(By.id("form:j_password")).sendKeys("demo");
        driver.findElement(By.id("form:j_idt19")).click();
        driver.findElement(By.linkText("Customers")).click();
        driver.findElement(By.id("formCL:name")).clear();
        driver.findElement(By.id("formCL:name")).sendKeys("hearney");
        driver.findElement(By.id("formCL:search")).click();
        driver.findElement(By.xpath("//tbody[@id='formCL:TEvDet:TBServAgr_data']/tr/td")).click();
        assertEquals("Service Agreement 8773600005", driver.findElement(By.xpath("//div[contains(@class, 'ui-carousel-header-title')]")).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
