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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.xwiki.test.ui.po.editor.DatePicker;

/**
 * Represents a Flash Messages entry page being edited.
 *
 * @version $Id$
 * @since
 */
public class FlashEntryEditPage extends FlashPage
{
    @FindBy(id = "Flash.FlashClass_0_dateBegin")
    private WebElement dateBeginElement;

    @FindBy(id = "Flash.FlashClass_0_dateEnd")
    private WebElement dateEndElement;

    @FindBy(id = "Flash.FlashClass_0_repeat")
    private WebElement repeatElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_repeatInterval']")
    private WebElement repeatIntervalLabelElement;

    @FindBy(id = "Flash.FlashClass_0_repeatInterval")
    private WebElement repeatIntervalElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_repeatFrequency']")
    private WebElement repeatFrequencyLabelElement;

    @FindBy(id = "Flash.FlashClass_0_repeatFrequency")
    private WebElement repeatFrequencyElement;

    @FindBy(xpath = "//label[@for='Flash.FlashClass_0_repeatDays']")
    private WebElement repeatDaysLabelElement;

    @FindBy(name = "Flash.FlashClass_0_repeatDays")
    private List<WebElement> repeatDaysElements;

    @FindBy(xpath = "//label[@for='repeatSummary']")
    private WebElement repeatSummaryLabelElement;

    @FindBy(id = "repeatSummary")
    private WebElement repeatSummaryElement;

    @FindBy(id = "Flash.FlashClass_0_groups")
    protected WebElement groupsElement;

    @FindBy(id = "Flash.FlashClass_0_message")
    protected WebElement messageElement;

    @FindBy(name = "action_save")
    private WebElement saveAndViewButtonElement;

    @FindBy(name = "action_cancel")
    private WebElement cancelButtonElement;

    /**
     * Go to page
     * 
     * @param the page name of the flash entry page
     * @return the edit page of the requested entry
     */
    public static FlashEntryEditPage gotoPage(String page)
    {
        return gotoPage(page, "en");
    }

    /**
     * Go to page
     * 
     * @param page the page name of the flash entry page
     * @param language the language in which to display the page
     * @return the edit page of the requested entry
     */
    public static FlashEntryEditPage gotoPage(String page, String language)
    {
        getUtil().gotoPage(FlashHomePage.getSpace(), page, "inline", "language=" + language);
        return new FlashEntryEditPage();
    }

    /**
     * Set value for date element
     * 
     * @param element the element representing a date input
     * @param date the date value to set
     */
    private void setDate(WebElement element, Calendar calendar)
    {
        // Click the input field to get the date picker
        element.click();

        DatePicker datePicker = new DatePicker();
        datePicker.setYear(getFormattedDate(calendar, "yyyy"));
        datePicker.setMonth(getFormattedDate(calendar, "MMMM"));
        datePicker.setDay(getFormattedDate(calendar, "d"));
        datePicker.setHour(getFormattedDate(calendar, "h a"));
        datePicker.setMinute(getFormattedDate(calendar, "mm"));

        // Click the input field again so that the date picker disappears
        // Do so in order to avoid issues when trying to set values to other fields right after setting a date
        element.click();
    }

    /**
     * Get formatted date
     * 
     * @param calendar the calendar object containing the date
     * @param format the output date format
     */
    private String getFormattedDate(Calendar calendar, String format)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(calendar.getTime());
    }

    /**
     * Set start date
     * 
     * @param dateBegin the date value to set
     */
    public void setDateBegin(Calendar dateBegin)
    {
        setDate(dateBeginElement, dateBegin);
    }

    /**
     * Set end date
     * 
     * @param dateEnd the date value to set
     */
    public void setDateEnd(Calendar dateEnd)
    {
        setDate(dateEndElement, dateEnd);
    }

    /**
     * Set repeat
     * 
     * @param repeat
     */
    public void setRepeat(Boolean repeat)
    {
        Select statusSelect = new Select(repeatElement);
        statusSelect.selectByValue(repeat ? "1" : "0");
    }

    /**
     * Get the "Repeat interval" label
     * 
     * @return the repeat interval label text
     */
    public String getRepeatIntervalLabel()
    {
        return repeatIntervalLabelElement.getText();
    }

    /**
     * Get the list of options for the repeatInterval
     * 
     * @return list of values for the repeat interval (daily/weekly/monthly/yearly)
     */
    public List<String> getRepeatInterval()
    {
        List<String> repeatInterval = new LinkedList<String>();
        List<WebElement> options = repeatIntervalElement.findElements(By.tagName("option"));

        for (WebElement option : options) {
            repeatInterval.add(option.getAttribute("label"));
        }

        return repeatInterval;
    }

    /**
     * Set repeat interval
     *
     * @param interval time-frame of the recurring event (daily/weekly/monthly/yearly)
     */
    public void setRepeatInterval(String interval)
    {
        Select statusSelect = new Select(repeatIntervalElement);
        statusSelect.selectByValue(interval);
    }

    /**
     * Get the "Repeat frequency" label
     * 
     * @return the repeat frequency label text
     */
    public String getRepeatFrequencyLabel()
    {
        return repeatFrequencyLabelElement.getText();
    }

    /**
     * Set the frequency of the event
     * 
     * @param frequency the frequency of the recurring event (1-30)
     */
    public void setRepeatFrequency(int frequency)
    {
        Select statusSelect = new Select(repeatFrequencyElement);
        statusSelect.selectByValue(Integer.toString(frequency));
    }

    /**
     * Get the "Repeat days" label
     * 
     * @return the repeat days label text
     */
    public String getRepeatDaysLabel()
    {
        return repeatDaysLabelElement.getText();
    }

    /**
     * Get the translated days of the week
     * 
     * @return list of the days of the week in the current language
     */
    public List<String> getRepeatDays()
    {
        List<String> repeatDays = new LinkedList<String>();
        for (int i = 0; i < 7; i++) {
            repeatDays.add(getUtil().findElementWithoutWaiting(getDriver(),
                By.xpath("//label[@for='xwiki-form-repeatDays-0-" + i + "']")).getText());
        }

        return repeatDays;
    }

    /**
     * Set the days in which the event will take place
     * 
     * @param days list of days in which the entry is displayed
     */
    public void setRepeatDays(List<String> days)
    {
        List<String> daysOfTheWeek =
            Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");

        for (String day : days) {
            getUtil().findElementWithoutWaiting(getDriver(),
                By.id("xwiki-form-repeatDays-0-" + daysOfTheWeek.indexOf(day))).click();
        }
    }

    /**
     * Get the "Repeat summary" label
     * 
     * @return the repeat summary label text
     */
    public String getRepeatSummaryLabel()
    {
        return repeatSummaryLabelElement.getText();
    }

    /**
     * Set the groups the groups towards the message is aimed to
     * 
     * @param list of XWiki groups for which the entry is displayed
     */
    public void setGroups(List<String> groups)
    {
        for (String group : groups) {
            groupsElement.sendKeys(group);
            waitUntilElementIsVisible(By.className("suggestItem"));
            getUtil().findElementWithoutWaiting(getDriver(), By.className("suggestItem")).click();
            waitUntilElementDisappears(By.className("suggestItem"));
        }
    }

    /**
     * Set the message to type in the Flash Message entry
     * 
     * @param message the entry's display message
     */
    public void setMessage(String message)
    {
        this.messageElement.clear();
        this.messageElement.sendKeys(message);
    }

    /**
     * Click the Save & View button
     * 
     * @return the resulting view page
     */
    public FlashEntryViewPage clickSaveAndView()
    {
        saveAndViewButtonElement.click();

        return new FlashEntryViewPage();
    }

    /**
     * Click the Cancel button
     * 
     * @return the resulting view page
     */
    public FlashEntryViewPage clickCancel()
    {
        cancelButtonElement.click();

        return new FlashEntryViewPage();
    }
}
