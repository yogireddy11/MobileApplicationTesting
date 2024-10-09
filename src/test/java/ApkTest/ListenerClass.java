package ApkTest;

import org.apache.logging.log4j.core.util.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;

import static org.apache.logging.log4j.core.util.FileUtils.*;

public class ListenerClass implements ITestListener {


    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        System.out.println(STR."For On Test Skipped methods : \{result.getName()}");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        System.out.println(STR."For On Test success methods : \{result.getName()}");
    }
    @AfterMethod
    public void getTakeScreenshot(){

    }
    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        System.out.println(STR."For On Test fail methods : \{result.getName()}");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        System.out.println(result.getName());
    }

    public void takeScreenShot(){


    }
}
