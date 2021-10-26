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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.xwiki.test.ui.po.BaseModal;
import org.xwiki.test.ui.po.ConfirmationModal;

/**
 * Represents a Flash Messages pop-up.
 *
 * @version $Id$
 * @since 1.1.2
 */
public class FlashPopup extends ConfirmationModal
{
    public static boolean isPresent()
    {
        return !getUtil().getDriver().findElementsWithoutWaiting(By.className("modal-backdrop")).isEmpty();
    }

    public FlashPopup()
    {
        super(By.id("flashPopup"));
        waitUntilReady();
    }

    /**
     * Contains message
     * 
     * @param date the date of the flash entry
     * @param message the message of the flash entry
     * @return if the described message is present in the pop-up or not
     */
    public boolean containsMessage(String date, String message)
    {
        String content = getMessage();
        return (content.contains(date) && content.contains(message));
    }

    private void waitUntilReady()
    {
        WebElement okButton = this.container.findElement(By.cssSelector(".modal-footer .btn-primary"));
        getDriver().waitUntilCondition(ExpectedConditions.elementToBeClickable(okButton));
    }

    @Override
    protected BaseModal waitForClosed()
    {
        getDriver().waitUntilCondition(new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                return !isPresent();
            }
        });
        return this;
    }
}
