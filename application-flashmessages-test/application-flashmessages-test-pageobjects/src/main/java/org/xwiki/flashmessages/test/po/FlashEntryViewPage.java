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

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents a Flash Messages entry page being viewed.
 *
 * @version $Id$
 * @since 1.1.2
 */
public class FlashEntryViewPage extends FlashPage
{
    @FindBy(xpath = "(//div[@class='xform']//div[@class='row'])[1]/div[1]//dd")
    protected WebElement dateBeginElement;

    @FindBy(xpath = "(//div[@class='xform']//div[@class='row'])[1]/div[2]//dd")
    protected WebElement dateEndElement;

    @FindBy(xpath = "(//div[@class='xform']//div[@class='row'])[2]/div[1]//dd")
    protected WebElement repeatElement;

    @FindBy(xpath = "(//div[@class='xform']//div[@class='row'])[2]/div[2]//dd")
    protected WebElement groupsElement;

    @FindBy(xpath = "//div[@class='xform']/dl/dd")
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
        getUtil().gotoPage(Arrays.asList(FlashHomePage.getSpace(), page), "WebHome", "view", "language=" + language);
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
        getDriver().waitUntilElementIsVisible(By.id("my-modal-popup"));

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
