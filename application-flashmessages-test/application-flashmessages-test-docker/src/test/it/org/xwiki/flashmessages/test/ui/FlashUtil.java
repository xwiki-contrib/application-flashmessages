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
import org.xwiki.flashmessages.test.po.FlashEntryEditPage;
import org.xwiki.flashmessages.test.po.FlashEntryViewPage;
import org.xwiki.flashmessages.test.po.FlashHomePage;
import org.xwiki.flashmessages.test.po.FlashSlider;
import org.xwiki.repository.test.SolrTestUtils;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.test.ui.po.CreatePagePage;

/**
 * Flash Messages test utilities.
 *
 * @version $Id$
 * @since
 */
public class FlashUtil
{
    private static FlashUtil instance;
    
    private TestUtils setup;

    private FlashEntry defaultEntry;

    private FlashUtil(TestUtils setup)
    {
        this.setup = setup;
    }

    /**
     * Get singleton instance
     * 
     * @param setup the generic test utils
     * @return the instance of FlashUtil
     */
    public static FlashUtil getInstance(TestUtils setup)
    {
        if (instance == null) {
            instance = new FlashUtil(setup);
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
            this.setup.forceGuestUser();
            return;
        }

        this.setup.getDriver()
            .get(this.setup.getURLToLoginAndGotoPage(username, password, this.setup.getURLToNonExistentPage()));
        this.setup.recacheSecretToken();
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
        if (this.setup.pageExists("Flash", entry.getName())) {
            this.setup.deletePage("Flash", entry.getName());
        }

        FlashHomePage homePage = FlashHomePage.gotoPage();

        CreatePagePage createPage = homePage.createPage();
        createPage.getDocumentPicker().setTitle(entry.getName());
        createPage.clickCreate();

        FlashEntryEditPage entryEditPage = new FlashEntryEditPage(); 
        entryEditPage.setDateBegin(entry.getDateBegin());
        entryEditPage.setDateEnd(entry.getDateEnd());
        entryEditPage.setRepeat(entry.getRepeat());

        if (entry.getRepeat()) {
            entryEditPage.setRepeatInterval(entry.getRepeatInterval());
            entryEditPage.setRepeatFrequency(entry.getRepeatFrequency());
            entryEditPage.setRepeatDays(entry.getRepeatDays());
        }

        entryEditPage.setGroups(entry.getGroups());
        entryEditPage.setWikiScope(entry.getWikiScope());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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
    public String getCurrentDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();

        // Get the index of the current day of the week, adjusted to a 0-based index
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 2;

        // Wrap around if the dayIndex is less than 0
        if (dayIndex < 0) {
            dayIndex = getDaysOfTheWeek().size() - 1;
        }

        return getDaysOfTheWeek().get(dayIndex);
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
        Assert.assertTrue(this.setup.pageExists("Flash", entry.getName()));

        // Wait for flashmessage to be registered with solr.
        waitUntilSolrReindex();

        // Get the Flash Message view page
        entryViewPage = FlashEntryViewPage.gotoPage(entry.getName());

        // Click the pop-up notification
        if (shouldBeInSlider && entryViewPage.hasPopup()) {
            entryViewPage.getPopup().clickOk();
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

    public void waitUntilSolrReindex() throws Exception
    {
        System.out.println("Waiting for solr to finish indexing. This may take a while...");
        new SolrTestUtils(setup, "http://localhost:8080/xwiki").waitEmpyQueue();
        System.out.println("Solr indexing finished.");
    }
}
