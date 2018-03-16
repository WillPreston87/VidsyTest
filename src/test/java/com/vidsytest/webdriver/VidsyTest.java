package com.vidsytest.webdriver;

import com.vidsytest.selenium.BrandSelectPage;
import com.vidsytest.selenium.BriefPage;
import com.vidsytest.selenium.LogInPage;
import com.vidsytest.selenium.Text;
import org.junit.Test;
import org.openqa.selenium.By;

import io.ddavison.conductor.Browser;
import io.ddavison.conductor.Config;
import io.ddavison.conductor.Locomotive;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Config(
        browser = Browser.CHROME,
        url = "https://app.staging.vidsy.co/login"
)

public class VidsyTest extends Locomotive {

    // Functions

    private void CheckPage(String Page) {
        // This is a function to detect which page to check is correct
        System.out.println("Checking page: '" + Page + "'...");
        if (Page.equals("Log In")) {
            CheckLogInPage();
        }
        else if (Page.equals("Brand Select")) {
            CheckBrandSelectPage();
        }
        else if (Page.equals("Brief")) {
            CheckBriefPage();
        }
    }

    private boolean CheckLogInPage() {
        // This is a function to check that the Log In page appears correctly
        try {
            validatePresent(LogInPage.EMAIL_FORM)
                    .validatePresent(LogInPage.PASSWORD_FORM)
                    .validatePresent(LogInPage.LOG_IN_BUTTON)
                    .validatePresent(LogInPage.LOGO);
            return true;
        }
        catch (Exception validation){
            logError("Log In Page does not appear correctly");
            return false;
        }
    }

    private void CheckBrandSelectPage() {
        // This is a function to check that the Brand Select page appears correctly
        try {
            validatePresent(BrandSelectPage.CLOSE_BUTTON)
                    .validatePresent(BrandSelectPage.BRAND_BUTTON);
        }
        catch (Exception validation){
            logError("Brand Select Page does not appear correctly");
        }
    }

    private void CheckBriefPage() {
        // This is a function to check that the Brief page appears correctly
        try {
            validatePresent(BriefPage.VIDSY_LOGO_BUTTON)
                    .validatePresent(BriefPage.ACCOUNT_BUTTON)
                    .validatePresent(BriefPage.LOG_OUT_BUTTON)
                    .validatePresent(BriefPage.CREATE_BRIEF_BUTTON)
                    .validatePresent(BriefPage.CREATE_BRIEF_CARD);
        }
        catch (Exception validation){
            logError("Brief Page does not appear correctly");
        }
    }

    private boolean LogInAccount(String Username, String Password) {
        // This is a function to attempt a log in
        CheckPage("Log In");
        System.out.println("Attempting to log into" + Username + " account with " + Password + " password...");
        if (Username.equals("Registered")) {
            driver.findElement(By.name("email")).sendKeys(Text.username);
        }
        else if (Username.equals("Unregistered")) {
            driver.findElement(By.name("email")).sendKeys(Text.wrong_username);
        }
        else if (Username.equals("Invalid")) {
            driver.findElement(By.name("email")).sendKeys(Text.invalid_username);
        }
        if (Password.equals("Correct")) {
            driver.findElement(By.name("password")).sendKeys(Text.password);
        }
        else if (Password.equals("Incorrect")) {
            driver.findElement(By.name("password")).sendKeys(Text.wrong_password);
        }
        click(LogInPage.LOG_IN_BUTTON);
        if (CheckLoggedIn()) {
            System.out.println("Successfully logged in with account");
            return true;
        }
        else {
            System.out.println("Log in failed");
            return false;
        }
    }

    private void SelectBrand(String Brand) {
        // This is a function to select a brand and check that is selected correctly
        CheckPage("Brand Select");
        System.out.println("Selecting brand: " + Brand);
        driver.findElement(By.xpath("//button[@class='brand-card' and contains(.,'" + Brand + "')]")).click();
        if (Brand.equals(getText(BriefPage.BRAND_NAME))) {
            System.out.println(Brand + "selected successfully");
        }
        else {
            logError("Wrong brand selected. " + getText(BriefPage.BRAND_NAME) + " selected instead of " + Brand);
        }
    }

    private boolean CheckLoggedIn() {
        // This is a function to check that a log in attempt as been successful
        try {
            CheckLogInPage();
            return false;
        }
        catch (Exception validation) {
            return true;
        }
    }

    private int CountBriefs() {
        // This is a function that counts the amount of briefs on the brief page
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        List<WebElement> Briefs = driver.findElements(By.className("brief-card"));
        return Briefs.size();
    }

    private boolean CheckBriefs() {
        // This is a function that prints back the text in the briefs as a way of verifying they exist
        int BriefCount = CountBriefs();
        System.out.println("Number of Briefs: " + BriefCount);
        if (BriefCount != 0) {
            String BriefText = getText(By.className("container-fluid"));
            System.out.println(BriefText);
            return true;
        }
        return false;
    }

    // Tests below

    @Test
    public void Test1LogIntoVidsyBrand(){
        // Scenario: This test is to see if i can log into the Vidsy platform with valid user credentials and select the Vidsy brand
        // GIVEN I have a registered account on the Vidsy platform
        // WHEN I enter valid user credentials on the log in page
        // AND I select Vidsy as my chosen brand
        // THEN the brief page for Vidsy is displayed correctly
        // AND a list of briefs are displayed

        System.out.println("********** RUNNING TEST 1: LogIntoVidsyBrand **********");
        LogInAccount("Registered", "Correct");
        SelectBrand("Vidsy");
        if (CheckBriefs()) {
            System.out.println("********** TEST 1 PASSED: LogIntoVidsyBrand **********");
        }
        else {
            logError("********** TEST 1 FAILED: LogIntoVidsyBrand **********");
        }
    }

    @Test
    public void Test2LogIntoListerineBrand() {
        // Scenario: This test is to see if i can log into the Vidsy platform with valid user credentials and select the Listerine brand
        // GIVEN I have a registered account on the Listerine platform
        // WHEN I enter valid user credentials on the log in page
        // AND I select Listerine as my chosen brand
        // THEN the brief page for Listerine is displayed correctly
        // AND a list of briefs are displayed

        System.out.println("********** RUNNING TEST 2: LogIntoListerineBrand **********");
        LogInAccount("Registered", "Correct");
        SelectBrand("Listerine");
        if (CheckBriefs()) {
            System.out.println("********** TEST 2 PASSED: LogIntoListerineBrand **********");
        }
        else {
            System.out.println("********** TEST 2 FAILED: LogIntoListerineBrand **********");
        }
    }

    @Test
    public void Test3UnableToLoginWithUnregisteredEmail() {
        // Scenario: This test is to see that I am unable to log into Vidsy with an unregistered email
        // GIVEN I don't have a registered account on the Vidsy platform
        // WHEN I enter unregistered user credentials on the log in page
        // THEN I am unable to log in

        System.out.println("********** RUNNING TEST 3: UnableToLoginWithUnregisteredEmail **********");
        if (LogInAccount("Unregistered", "Correct")) {
            logError("********** TEST FAILED: UnableToLoginWithInvalidEmail **********");
        }
        else {
            System.out.println("********** TEST 3 PASSED: UnableToLoginWithUnregisteredEmail **********");
        }
    }

    @Test
    public void Test4UnableToLoginWithIncorrectPassword() {
        // Scenario: This test is to see that I am unable to log into Vidsy with an invalid email
        // GIVEN I have a registered account on the Vidsy platform
        // WHEN I enter a registered email on the log in page
        // AND I enter the incorrect password for that registered email
        // THEN I am unable to log in

        System.out.println("********** RUNNING TEST 4: UnableToLoginWithUnregisteredEmail **********");
        if (LogInAccount("Registered", "Incorrect")) {
            logError("********** TEST 4 FAILED: UnableToLoginWithInvalidEmail **********");
        }
        else {
            System.out.println("********** TEST 4 PASSED: UnableToLoginWithUnregisteredEmail **********");
        }
    }

    @Test
    public void Test5UnableToLoginWithInvalid() {
        // Scenario: This test is to see that I am unable to log into Vidsy with an unregistered email
        // GIVEN I don't have a registered account on the Vidsy platform
        // WHEN I enter an invalid email on the log in page
        // AND a valid password
        // THEN I am unable to log in
        // AND a message appears saying the email is invalid

        System.out.println("********** RUNNING TEST 5: UnableToLoginWithUnregisteredEmail **********");
        if (LogInAccount("Invalid", "Incorrect")) {
            logError("********** TEST 5 FAILED: UnableToLoginWithInvalidEmail **********");
        }
        else {
            System.out.println("********** TEST  5PASSED: UnableToLoginWithUnregisteredEmail **********");
        }
    }


}
