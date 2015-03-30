package org.xwiki.flashmessages.test.po;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.test.ui.po.BaseElement;

/**
 * Represents a Flash Messages pop-up.
 *
 * @version $Id$
 * @since
 */
public class FlashPopup extends BaseElement
{
    @FindBy(id = "my-modal-popup")
    private WebElement modalPopupElement;

    @FindBy(xpath = "//div[@id='my-modal-popup']/descendant::input[@type = 'submit' and @class = 'button']")
    private WebElement okButtonElement;
    
    /**
     * Get message
     * 
     * @return the pop-up message
     */
    public String getMessage()
    {
        return modalPopupElement.getText();
    }

    /**
     * Contains message
     * 
     * @param date the date of the flash entry
     * @param message the message of the flash entry
     * @return if the described message is present in the pop-up or not
     */
    public Boolean containsMessage(String date, String message)
    {
        String content = getMessage();

        return (content.contains(date) && content.contains(message));
    }

    /**
     * Click the OK button
     * 
     * @return the view page state resulting from closing the pop-up
     */
    public FlashEntryViewPage clickOk()
    {
        okButtonElement.click();
        return new FlashEntryViewPage();
    }
}
