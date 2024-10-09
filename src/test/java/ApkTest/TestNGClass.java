package ApkTest;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Listeners(ListenerClass.class)
public class TestNGClass {
    WebDriver driver = new FirefoxDriver();


    @Test(priority = 2)
    public void OpenBrowser() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rms-dev.peopletech.com/login");
        String expectedTitle = "RMS";
        String originalTitle = driver.getTitle();
        System.out.println(originalTitle);
        Assert.assertEquals(originalTitle, expectedTitle, "Titles of the website do not match");
    }

    @AfterClass
    public void CloseBrowser() {
        driver.close();
        Reporter.log("Driver Closed After Testing");
    }


    @Test (priority = 1)
    public void AccountTest() {
        int i = 1;
        if(i < 2)
            Assert.assertEquals(i , i);
        i++;
    }
    @Test(priority = 3)
    public void zFail(){

        Assert.assertEquals("display","notDisplay");
    }

    @AfterMethod
    public void getScreenShot(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE){
            System.out.println(result.getStatus());
            File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(STR."C:\\Users\\MusumYogireddy\\Pictures\\Screenshots\\TestNG Failure screenshots\\\{result.getName()}.jpg"));
        }
    }
    @Test
    public void SkipTest() {
        throw new SkipException("Skipping The Test Method ");
    }
}
