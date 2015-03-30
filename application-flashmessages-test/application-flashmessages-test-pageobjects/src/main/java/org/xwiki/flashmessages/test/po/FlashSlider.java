package org.xwiki.flashmessages.test.po;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.xwiki.test.ui.po.BaseElement;

/**
 * Represents a Flash Messages slider.
 *
 * @version $Id$
 * @since
 */
public class FlashSlider extends BaseElement
{
    @FindBy(id = "flashMessages")
    private WebElement sliderElement;

    @FindBy(xpath = "//div[@id='flashMessages']/descendant::ul[@class = 'messages']")
    private WebElement sliderMessagesElement;
    
    @FindBy(xpath = "//div[@id='flashMessages']/descendant::ul[@class = 'navigation']")
    private WebElement sliderNavigationElement;
    
    /**
     * Contains message
     * 
     * @param date the date of the flash entry
     * @param message the message of the flash entry
     * @return if the described message is present in the slider or not
     */
    public Boolean containsMessage(String date, String message)
    {
        List<WebElement> messages = sliderMessagesElement.findElements(By.tagName("li"));

        for (WebElement m : messages) {
            if (m.findElement(By.className("date")).getAttribute("textContent").contains(date) &&
                m.findElement(By.className("message")).getAttribute("textContent").contentEquals(message)) {
                return true;
            }
        }

        return false;
    }
}
