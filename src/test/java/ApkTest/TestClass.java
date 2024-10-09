package ApkTest;

import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.net.Priority;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class TestClass {

    AndroidDriver driver;
    WebDriverWait driverWait;
    DesiredCapabilities capabilities;


    @BeforeClass
    public void setup(){
         capabilities = getDesiredCapabilities();
        try {
            URL url = new URL("http://localhost:4723/wd/hub");
            driver = new AndroidDriver(url,capabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            PageFactory.initElements(driver,this);

        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    private  DesiredCapabilities getDesiredCapabilities() {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("appium:udid", "08433312AK070338");
        capabilities.setCapability("platformName", "android");
        capabilities.setCapability("appium:platformVersion", "12");
        capabilities.setCapability("appium:appPackage", "org.simple.clinic.staging");
        capabilities.setCapability("appium:appActivity", "org.simple.clinic.setup.SetupActivity");
        capabilities.setCapability("appium:ensureWebviewsHavePages", true);
        capabilities.setCapability("appium:nativeWebScreenshot", true);
        capabilities.setCapability("appium:newCommandTimeout", 3600);
        capabilities.setCapability("appium:connectHardwareKeyboard", true);
        return capabilities;
    }

    @Test(priority = 1)
    public void initialStart(){
        WebElement logo = driver.findElement(By.id("org.simple.clinic.staging:id/splashSimpleLogo"));assertTrue(logo.isDisplayed(),"initial logo not displaying!!");
        WebElement splashImg = driver.findElement(By.id("org.simple.clinic.staging:id/splashLottieView"));
        assertTrue(splashImg.isDisplayed(),"splashImg not displaying in mainActivity");
         driver.findElement(By.id("org.simple.clinic.staging:id/nextButton")).click();
    }
    @Test(priority = 2,dependsOnMethods = "initialStart")
    public void getStarted(){
        WebElement firstOne = driver.findElement(By.id("org.simple.clinic.staging:id/introOneTextView"));
        WebElement secondOne = driver.findElement(By.id("org.simple.clinic.staging:id/introTwoTextView"));
        WebElement thirdOne = driver.findElement(By.id("org.simple.clinic.staging:id/introThreeTextView"));
        assertTrue(firstOne.isDisplayed());
        assertTrue(secondOne.isDisplayed());
        assertTrue(thirdOne.isDisplayed());
        WebElement getStart = driver.findElement(By.id("org.simple.clinic.staging:id/getStartedButton"));
        if (getStart.isDisplayed()){
            getStart.click();
        }else {
            throw new NotFoundException("Get Start button not displayed!!");
        }

    }
    @Test(priority = 3,dependsOnMethods = "getStarted")
    public void agreeTermsAndPolicy(){
        WebElement accept = driver.findElement(By.xpath("//android.widget.TextView[@text=\"AGREE AND CONTINUE\"]"));
        if (accept.isDisplayed()){
            accept.click();
        }else {
            throw new RuntimeException("AgreeTermAndPolicy button not exist!!");
        }

    }
    @Test(priority = 4)
    public void selectCountry() throws InterruptedException {
        driverWait = new WebDriverWait(driver,Duration.ofSeconds(60));
        String id = "org.simple.clinic.staging:id/supportedCountriesList";

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement country = driver.findElement(By.id(id));

        List<WebElement> findCountryDropDown = country.findElements(By.className("android.widget.RadioButton"));
        for (int i=0;i< findCountryDropDown.size();i++){

            country = driver.findElement(By.id(id));
            findCountryDropDown = country.findElements(By.className("android.widget.RadioButton"));
            WebElement fetchCountry = findCountryDropDown.get(i);
            String printCountryOptions = fetchCountry.getText();
            fetchCountry.click();
            WebElement clinic  = driverWait.
                    until(ExpectedConditions.visibilityOfElementLocated(By.id("org.simple.clinic.staging:id/statesList")));
            List<WebElement> findStateOptions = clinic.findElements(By.className("android.widget.RadioButton"));

            for(WebElement getStateOptions : findStateOptions){
                String printStateOptions = getStateOptions.getText();
                System.out.println(STR."\{printCountryOptions} : \{printStateOptions}");
            }
            driver.navigate().back();
        }
                driver.findElement(By.
                xpath("//android.widget.RadioButton[@resource-id=\"org.simple.clinic.staging:id/countryButton\" and @text=\"India\"]")).click();
        driver.findElement(By.xpath(
                "//android.widget.RadioButton[@resource-id=\"org.simple.clinic.staging:id/stateRadioButton\" and @text=\"New Magnesia\"]")).click();
    }

    @DataProvider(name = "data")
    public Object[] dataMethod(){
       Object[] returnObj = new Object[3];
       returnObj[0] = "";
       returnObj[1] = "324";
       returnObj[2] = "8374033532";
            return returnObj;
    }
    @Test(priority = 7,dataProvider = "data")
    public void verifyMobileNum(String mobileNum) {
        WebElement countryCode = driver.findElement(By.id("org.simple.clinic.staging:id/isdCodeEditText"));
        String getCountryCode = countryCode.getText();
        assertEquals(getCountryCode, "+91", "Country code not matched!!");

        WebElement mobileNumTxt = driver.findElement(By.id("org.simple.clinic.staging:id/phoneNumberEditText"));

        if (mobileNumTxt.isDisplayed() && mobileNumTxt.isEnabled()) {
                mobileNumTxt.clear();
                mobileNumTxt.sendKeys(mobileNum);
                driver.findElement(By.id("org.simple.clinic.staging:id/nextButton")).click();
                try {
                    WebElement errorMessage = driver.findElement(By.id("org.simple.clinic.staging:id/validationErrorTextView"));
                    if (errorMessage.isDisplayed()) {
                        System.err.println(STR."Error Message: \{errorMessage.getText()}");
                    }
                } catch (Exception e) {
                    System.out.println(STR."Successfully entered mobile number: \{mobileNum}");
                }
            }
         else {
            throw new RuntimeException("Mobile number text field is not visible or enabled!");
        }
    }


    @Test(priority = 8)
    public void enterFullName(){
        WebElement nameTxtBox = driver.findElement(By.id("org.simple.clinic.staging:id/fullNameEditText"));
        String enterName = "Original Gangster";
        if (nameTxtBox.isDisplayed() && nameTxtBox.isEnabled()){
                nameTxtBox.clear();
                nameTxtBox.sendKeys(enterName);
                WebElement continueBtn = driver.findElement(By.id("org.simple.clinic.staging:id/nextButton"));
                continueBtn.click();
        } else {
            throw new RuntimeException("Name text box not exist!!");
        }
    }
    @DataProvider(name = "setPin")
    public Object[][] pinData(){
        Object[][] pin = new Object[][]{
               // {"9876", "7872"},
                {"1234", "1234"},

        };
        return pin;
    }

    @Test(priority = 9,dataProvider = "setPin")
    public void setUpPin(String pin, String conPin) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement pinSet = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.simple.clinic.staging:id/pinEditText")));
           pinSet.clear();
           pinSet.sendKeys(pin);
           WebElement confirmPin = driver.findElement(By.id("org.simple.clinic.staging:id/confirmPinEditText"));
           confirmPin.clear();
           confirmPin.sendKeys(conPin);
            if (pin.equals(conPin)) {
                System.out.println(STR."PIN and Confirm PIN matched for PIN: \{pin}");
            } else {
                System.err.println(STR."PIN and Confirm PIN did not match for PIN: \{pin}");
                WebElement pinDoesNotMatchErrorMsg =
                        driver.findElement(By.xpath("//android.widget.TextView[@text=\"PIN doesn’t match the original PIN\"]"));
                if (pinDoesNotMatchErrorMsg.isDisplayed()) {
                    System.err.println(STR."Error Message : \{pinDoesNotMatchErrorMsg.getText()}");
                }
                driver.navigate().back();
            }
    }
    @Test(priority = 10)
    public void setPermissions(){

        WebElement permissionBtn = driver.findElement(By.id("org.simple.clinic.staging:id/allowAccessButton"));
        permissionBtn.click();
       // List<WebElement> locationTypes = driver.findElements(By.className("android.widget.Button"));
        WebElement whileUsingApk = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
        whileUsingApk.click();
    }
    @Test(priority = 11)
    public void facilityName() throws InterruptedException {
        Thread.sleep(3000);
        WebElement searchBar = driver.findElement(By.id("org.simple.clinic.staging:id/searchEditText"));
        List<WebElement> availableFacility = driver.findElements(By.className("android.widget.TextView"));
        int i=0;
        while (i< availableFacility.size()){
            WebElement facility = availableFacility.get(i);
            System.out.println(facility.getText());
            i++;
        }

        searchBar.sendKeys("CHC Cabbagetown");
    }

    @AfterMethod
    public void getScreenShot(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE){
            System.out.println(result.getStatus());
            File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(STR."C:\\Users\\MusumYogireddy\\Pictures\\Screenshots\\TestNG Failure screenshots\\\{result.getName()}.jpg"));
        }
    }
    @AfterMethod
    public void getSuccessMethodScreenShot(ITestResult successResult) throws IOException {
        if(successResult.getStatus() == ITestResult.SUCCESS){
            File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(STR."C:\\Users\\MusumYogireddy\\Pictures\\Screenshots\\TestNg success screeenshot\{successResult.getName()}.png"));
        }
    }

    @AfterClass
    public void tearDown(){
            driver.quit();
    }
}
