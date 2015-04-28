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
import org.openqa.selenium.support.FindBys;
import org.xwiki.test.ui.po.LiveTableElement;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Represents actions that can be done on the Flash.WebHome page.
 *
 * @version $Id$
 * @since
 */
public class FlashHomePage extends ViewPage
{
    @FindBy(id = "document-title")
    private WebElement titleElement;

    @FindBys({@FindBy(id = "actionBox"), @FindBy(className = "add")})
    private WebElement addEntryLink;

    @FindBy(className = "infomessage")
    private WebElement infoMessageElement;

    /**
     * Go to page
     * 
     * @param page the page name of the flash entry page
     * @return the view page of the requested entry
     */
    public static FlashHomePage gotoPage()
    {
        return gotoPage("en");
    }

    /**
     * Go to page
     * 
     * @param language the language in which to display the page
     * @return the view page of the requested entry
     */
    public static FlashHomePage gotoPage(String language)
    {
        getUtil().gotoPage(getSpace(), getPage(), "view", "language=" + language);

        return new FlashHomePage();
    }

    /**
     * Get space
     * 
     * @return the space of the Flash Messages application
     */
    public static String getSpace()
    {
        return "Flash";
    }

    /**
     * Get page
     * 
     * @return the WebHome of the Flash Messages application
     */
    public static String getPage()
    {
        return "WebHome";
    }

    /**
     * Clicks on the link to add a new application entry.
     *
     * @return the pane used to input the entry name
     */
    public AddEntryDialog clickAddNewEntry()
    {
        addEntryLink.click();

        return new AddEntryDialog();
    }

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
     * Get info message
     * 
     * @return the message at the top of the page
     */
    public String getInfoMessage()
    {
        return infoMessageElement.getText().trim();
    }

    /**
     * Get livetable
     * 
     * @return the livetable element
     */
    public LiveTableElement getLiveTable()
    {
        LiveTableElement liveTable = new LiveTableElement("flash");
        liveTable.waitUntilReady();

        return liveTable;
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
}
