package com.vidsytest.selenium;

import org.openqa.selenium.By;

public class LogInPage {

    // text elements
    public static final By LOGO = By.className("avatar__thumbnail");

    // interactive elements
    public static final By EMAIL_FORM = By.name("email");
    public static final By PASSWORD_FORM = By.name("password");
    public static final By LOG_IN_BUTTON = By.className("login__button");
}
