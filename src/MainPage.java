import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class MainPage {
    public WebDriver driver = new ChromeDriver();

    @BeforeSuite public void beforeSuite(){
        driver.get("https://video.firat.edu.tr/");
        driver.manage().window().maximize();
    }

    @Test (dataProvider = "getData") public void checkVideoLinks(String pageLink ) throws IOException {
        SoftAssert soft = new SoftAssert();

        driver.get(pageLink);
        List<WebElement> links = driver.findElements(By.xpath("//div[@class='container']//a"));
        for (WebElement a: links) {
            String url = a.getAttribute("href");
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.connect();
            soft.assertTrue(conn.getResponseCode() < 400,a.getText() + "          BROKEN LINK");
//            System.out.print(a.getText() + "  ");
        }
        soft.assertAll();
    }

    @DataProvider public Object[] getData(){
        Object[] element = new Object[3];
        element[0] = driver.findElement(By.xpath("//ul[@class='menu-list']//li[1]/a")).getAttribute("href");
        element[1] = driver.findElement(By.xpath("//ul[@class='menu-list']//li[2]/a")).getAttribute("href");
        element[2] = driver.findElement(By.xpath("//ul[@class='menu-list']//li[3]/a")).getAttribute("href");

        return element;
    }

    @AfterSuite public void afterSuite() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }
}