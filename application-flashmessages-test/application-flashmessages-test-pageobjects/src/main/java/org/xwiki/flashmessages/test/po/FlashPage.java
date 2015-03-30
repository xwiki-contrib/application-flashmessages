package org.xwiki.flashmessages.test.po;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Represents the common ground between the Flash Messages view and edit pages.
 *
 * @version $Id$
 * @since
 */
public class FlashPage extends ViewPage
{
    @FindBy(id = "document-title")
    protected WebElement titleElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_dateBegin']")
    protected WebElement dateBeginLabelElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_dateEnd']")
    protected WebElement dateEndLabelElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_repeat']")
    protected WebElement repeatLabelElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_groups']")
    protected WebElement groupsLabelElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_message']")
    protected WebElement messageLabelElement;

    /**
     * Get title
     * 
     * @return the page's title
     */
    public String getTitle()
    {
        return titleElement.getText();
    }

    /**
     * Get start date label
     * 
     * @return label for the start date
     */
    public String getDateBeginLabel()
    {
        return dateBeginLabelElement.getText();
    }

    /**
     * Get end date label
     * 
     * @return label for the end date
     */
    public String getDateEndLabel()
    {
        return dateEndLabelElement.getText();
    }

    /**
     * Get repeat label
     * 
     * @return label for repeat
     */
    public String getRepeatLabel()
    {
        return repeatLabelElement.getText();
    }

    /**
     * Get groups label
     * 
     * @return label for groups
     */
    public String getGroupsLabel()
    {
        return groupsLabelElement.getText();
    }

    /**
     * Get message label
     * 
     * @return label for message
     */
    public String getMessageLabel()
    {
        return messageLabelElement.getText();
    }
    
    /**
     * Check if the page contains xwiki message
     * 
     * @param message the message that should be contained within the page
     * @return if the message is present in the page or not
     */
    public Boolean containsXWikiMessage(String message)
    {
        List<WebElement> messages = getUtil().findElementsWithoutWaiting(getDriver(), By.className("xwikimessage"));
        
        for (WebElement msg : messages) {
            if (msg.getText().contains(message)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Check if an element is present in the DOM
     * 
     * @param id of the element to check
     * @return if the element is present in the page or not
     */
    public boolean elementExists(String id)
    {
        List<WebElement> elements = getDriver().findElements(By.id(id));
        
        if(!elements.isEmpty())
        {
            return elements.get(0).isDisplayed();
        }
        
        return !elements.isEmpty();
    }
}
