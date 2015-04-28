/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
            if (m.findElement(By.className("date")).getAttribute("textContent").contains(date)
                && m.findElement(By.className("message")).getAttribute("textContent").contentEquals(message)) {
                return true;
            }
        }

        return false;
    }
}
