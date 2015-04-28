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
