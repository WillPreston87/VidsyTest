package com.vidsytest.selenium;

import org.openqa.selenium.By;

public class BriefPage {

    // text elements
    public static final By BRAND_NAME = By.className("navigation__current-info");

    // interactive elements
    public static final By VIDSY_LOGO_BUTTON = By.className("avatar__thumbnail");
    public static final By ACCOUNT_BUTTON = By.partialLinkText("/settings");
    public static final By LOG_OUT_BUTTON = By.xpath("//button[contains(.,'Log out']");
    public static final By CREATE_BRIEF_BUTTON = By.className("sub-navigation__button ui-link is-interactive");
    public static final By CREATE_BRIEF_CARD = By.className("create-campaign-card");
}
