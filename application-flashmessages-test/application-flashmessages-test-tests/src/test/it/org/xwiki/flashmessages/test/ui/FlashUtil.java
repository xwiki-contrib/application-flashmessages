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
package org.xwiki.flashmessages.test.ui;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.xwiki.flashmessages.test.po.AddEntryDialog;
import org.xwiki.flashmessages.test.po.FlashEntryEditPage;
import org.xwiki.flashmessages.test.po.FlashEntryViewPage;
import org.xwiki.flashmessages.test.po.FlashHomePage;
import org.xwiki.flashmessages.test.po.FlashPopup;
import org.xwiki.flashmessages.test.po.FlashSlider;
import org.xwiki.test.ui.po.ViewPage;

/**
 * Flash Messages test utilities.
 *
 * @version $Id$
 * @since
 */
public class FlashUtil extends ViewPage
{
    private static FlashUtil instance;

    private FlashEntry defaultEntry;

    private FlashUtil()
    {
        // Dummy
    }

    /**
     * Get singleton instance
     * 
     * @return the instance of FlashUtil
     */
    public static FlashUtil getInstance()
    {
        if (instance == null) {
            instance = new FlashUtil();
        }

        return instance;
    }

    /**
     * Set default entry
     * 
     * @param entry the FlashEntry object to set as default
     */
    public void setDefaultEntry(FlashEntry entry)
    {
        this.defaultEntry = entry;
    }

    /**
     * Get default entry
     * 
     * @param entry the default FlashEntry object
     */
    public FlashEntry getDefaultEntry()
    {
        return this.defaultEntry;
    }

    /**
     * Get the default entry name
     * 
     * @return the default entry's name
     */
    public String getDefaultEntryName()
    {
        return this.defaultEntry.getName();
    }

    /**
     * Get default entry formatted start date
     * 
     * @return the default entry's formatted start date
     */
    public String getDefaultEntryFormattedDateBegin()
    {
        return getFormattedDate(this.defaultEntry.getDateBegin());
    }

    /**
     * Get default entry formatted end date
     * 
     * @return the default entry's formatted end date
     */
    public String getDefaultEntryFormattedDateEnd()
    {
        return getFormattedDate(this.defaultEntry.getDateEnd());
    }

    /**
     * Get default entry message
     * 
     * @return the default entry's message
     */
    public String getDefaultEntryMessage()
    {
        return this.defaultEntry.getMessage();
    }

    /**
     * Login with a certain user
     * 
     * @param username the username of the user
     */
    public void login(String username)
    {
        login(username, "");
    }

    /**
     * Login with a certain user
     * 
     * @param username the username of the user
     * @param password the password of the user
     */
    public void login(String username, String password)
    {
        if (username.equals("guest")) {
            getUtil().forceGuestUser();
            return;
        }

        getDriver().get(getUtil().getURLToLoginAndGotoPage(username, password, getUtil().getURLToNonExistentPage()));
        getUtil().recacheSecretToken();
    }

    /**
     * Get the view page of the default entry
     * 
     * @return the default entry's view page
     */
    public FlashEntryViewPage getDefaultEntryViewPage()
    {
        return FlashEntryViewPage.gotoPage(defaultEntry.getName());
    }

    /**
     * Get the edit page of the default entry
     * 
     * @return teh default entry's edit page
     */
    public FlashEntryEditPage getDefaultEntryEditPage()
    {
        return FlashEntryEditPage.gotoPage(defaultEntry.getName());
    }

    /**
     * Create a new Flash Message
     * 
     * @param entry the object representing the entry content
     * @return the resulting entry document in view mode
     * @throws Exception 
     */
    public FlashEntryViewPage createEntry(FlashEntry entry) throws Exception
    {
        if (getUtil().pageExists("Flash", entry.getName())) {
            getUtil().deletePage("Flash", entry.getName());
        }

        FlashHomePage homePage = FlashHomePage.gotoPage();

        AddEntryDialog newEntryDialog = homePage.clickAddNewEntry();
        newEntryDialog.setName(entry.getName());

        FlashEntryEditPage entryEditPage = newEntryDialog.clickAdd();

        entryEditPage.setDateBegin(entry.getDateBegin());
        entryEditPage.setDateEnd(entry.getDateEnd());
        entryEditPage.setRepeat(entry.getRepeat());

        if (entry.getRepeat()) {
            entryEditPage.setRepeatInterval(entry.getRepeatInterval());
            entryEditPage.setRepeatFrequency(entry.getRepeatFrequency());
            entryEditPage.setRepeatDays(entry.getRepeatDays());
        }

        entryEditPage.setGroups(entry.getGroups());
        entryEditPage.setMessage(entry.getMessage());

        return entryEditPage.clickSaveAndView();
    }

    /**
     * Get a date forwards or backwards in time
     * 
     * @param yearOffset the number of years
     * @param monthOffset the number of months
     * @param weekOffset the number of weeks
     * @param dayOffset the number of days
     * @param hourOffset the number of hours
     * @return the computed date
     */
    public Calendar getDate(int yearOffset, int monthOffset, int weekOffset, int dayOffset, int hourOffset, Boolean midnight)
    {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, yearOffset);
        calendar.add(Calendar.MONTH, monthOffset);
        calendar.add(Calendar.DATE, dayOffset + 7 * weekOffset);

        // Recurrent events should begin at midnight due to the fact that the date difference is not rounded up
        // For example 1day 20hours != 2 days
        if (midnight) {
            calendar.set(Calendar.HOUR, 0);
        } else {
            calendar.add(Calendar.HOUR, hourOffset);
        }

        calendar.set(Calendar.MINUTE, 0); // play it safe due to DatePicker working in 5min increments

        return calendar;
    }

    /**
     * Get the string representation of a date in the format used by the DatePicker
     * 
     * @param calendar the Calendar object representing the date
     * @return the formatted date
     */
    public String getFormattedDate(Calendar calendar)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");

        return dateFormat.format(calendar.getTime());
    }

    /**
     * Get the days of the week
     * 
     * @return list of days of the week values
     */
    public List<String> getDaysOfTheWeek()
    {
        return new LinkedList<String>(Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"));
    }

    /**
     * Get the current day of the week
     * 
     * @return the current day of the week
     */
    public String getCurrentDayOfTheWeek()
    {
        Calendar calendar = Calendar.getInstance();

        return getDaysOfTheWeek().get(calendar.get(Calendar.DAY_OF_WEEK) - 2);
    }

    /**
     * Get the translated list of days of the week
     * 
     * @param translation the FlashTranslations object set to a specific language
     * @return list of translated days of the week values
     */
    public List<String> getDaysOfTheWeek(FlashTranslations translation)
    {
        List<String> daysOfTheWeek = new LinkedList<String>();

        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_monday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_tuesday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_wednesday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_thursday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_friday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_saturday"));
        daysOfTheWeek.add(translation.getKey("Flash.FlashClass_repeatDays_sunday"));

        return daysOfTheWeek;
    }

    /**
     * Get the translated list of repeat interval options
     * 
     * @param translation the FlashTranslations object set to a specific language
     * @return list of translated repeat interval values
     */
    public List<String> getRepeatIntervals(FlashTranslations translation)
    {
        List<String> repeatIntervals = new LinkedList<String>();

        repeatIntervals.add(translation.getKey("Flash.FlashClass_repeatInterval_daily"));
        repeatIntervals.add(translation.getKey("Flash.FlashClass_repeatInterval_weekly"));
        repeatIntervals.add(translation.getKey("Flash.FlashClass_repeatInterval_monthly"));
        repeatIntervals.add(translation.getKey("Flash.FlashClass_repeatInterval_yearly"));

        return repeatIntervals;
    }

    /**
     * Test a Flash Message entry
     * 
     * @param entry FlashEntry object containing the entry data
     * @param shouldBeInSlider should the entry be displayed in the slider (active or not)
     * @throws Exception 
     */
    public FlashEntryViewPage testMessage(FlashEntry entry, Boolean shouldBeInSlider) throws Exception
    {
        // Login as Light Yagami (administrator).
        login("LightYagami", "justice");

        // Create entry and get the resulting view page
        FlashEntryViewPage entryViewPage = createEntry(entry);

        // Check if the entry document was created
        Assert.assertTrue(getUtil().pageExists("Flash", entry.getName()));

        // Get the Flash Message view page
        entryViewPage = FlashEntryViewPage.gotoPage(entryViewPage.getMetaDataValue("page"));

        // Click the pop-up notification
        if (shouldBeInSlider && entryViewPage.hasPopup()) {
            FlashPopup flashPopup = entryViewPage.getPopup();
            entryViewPage = flashPopup.clickOk();
            entryViewPage = entryViewPage.reload();
        }

        if (entryViewPage.hasSlider()) {
            // Check if the message is present in the slider
            FlashSlider flashSlider = entryViewPage.getSlider();

            // Check if the message is in the slider
            Boolean isInSlider = flashSlider.containsMessage(getFormattedDate(entry.getDateBegin()), entry.getMessage());
            Assert.assertTrue(shouldBeInSlider ? isInSlider : !isInSlider);
        } else {
            // Check if it is ok that the slider is not present
            Assert.assertTrue(!shouldBeInSlider);
        }

        return entryViewPage;
    }
}
