package org.xwiki.flashmessages.test.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents a Flash Messages entry page being viewed.
 *
 * @version $Id$
 * @since
 */
public class FlashEntryViewPage extends FlashPage
{
    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_dateBegin']/following::dd")
    protected WebElement dateBeginElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_dateEnd']/following::dd")
    protected WebElement dateEndElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_repeat']/following::dd")
    protected WebElement repeatElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_groups']/following::dd")
    protected WebElement groupsElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_message']/following::dd")
    protected WebElement messageElement;

    /**
     * Go to page
     * 
     * @param page the page name of the flash entry page
     * @return the view page of the requested entry
     */
    public static FlashEntryViewPage gotoPage(String page)
    {
        return gotoPage(page, "en");
    }

    /**
     * Go to page
     * 
     * @param page the page name of the flash entry page
     * @param language the language in which to display the page
     * @return the view page of the requested entry
     */
    public static FlashEntryViewPage gotoPage(String page, String language)
    {
        getUtil().gotoPage(FlashHomePage.getSpace(), page, "view", "language=" + language);
        return new FlashEntryViewPage();
    }

    /**
     * Reload the current page
     * 
     * @return view page of the current entry
     */
    public FlashEntryViewPage reload()
    {
        getUtil().gotoPage(getDriver().getCurrentUrl());
        return new FlashEntryViewPage();
    }

    /**
     * Has slider
     * 
     * @return if the slider is present on the current page or not
     */
    public Boolean hasSlider()
    {
        return elementExists("flashMessages");
    }

    /**
     * Get slider
     * 
     * @return the FlashSlider element
     */
    public FlashSlider getSlider()
    {
        return new FlashSlider();
    }

    /**
     * Has pop-up
     * 
     * @return if the pop-up message is present on the current page
     */
    public Boolean hasPopup()
    {
        return elementExists("my-modal-popup");
    }

    /**
     * Get pop-up
     * 
     * @return the FlashPopup element
     */
    public FlashPopup getPopup()
    {
        this.waitUntilElementIsVisible(By.id("my-modal-popup"));

        return new FlashPopup();
    }

    /**
     * Get start date
     * 
     * @return the string representation on the start date
     */
    public String getDateBegin()
    {
        return dateBeginElement.getText();
    }

    /**
     * Get end date
     * 
     * @return the string representation of the end date
     */
    public String getDateEnd()
    {
        return dateEndElement.getText();
    }

    /**
     * Get message
     * 
     * @return the entry's display message
     */
    public String getMessage()
    {
        return messageElement.getText();
    }
}
