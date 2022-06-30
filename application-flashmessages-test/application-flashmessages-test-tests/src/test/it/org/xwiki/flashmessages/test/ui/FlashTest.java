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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.flashmessages.test.po.*;
import org.xwiki.test.ui.AbstractTest;
import org.xwiki.test.ui.SuperAdminAuthenticationRule;
import org.xwiki.test.ui.po.LiveTableElement;
import org.xwiki.test.ui.po.ViewPage;
import org.junit.Assert;
import org.xwiki.panels.test.po.ApplicationsPanel;

/**
 * UI tests for the Flash Messages application.
 *
 * @version $Id$
 * @since
 */
public class FlashTest extends AbstractTest
{
    private FlashUtil flashUtil = FlashUtil.getInstance();

    private FlashTranslations translation = FlashTranslations.getInstance();

    @Rule
    public SuperAdminAuthenticationRule superAdmin = new SuperAdminAuthenticationRule(getUtil());

    @BeforeClass
    public static void createUsers()
    {
        // Create administrator
        getUtil().createUser("LightYagami", "justice", "email", "light.yagami@xwiki.org", "first_name", "Light", "last_name", "Yagami");
        
        // Create normal user
        getUtil().createUser("MisaAmane", "love", "email", "misa.amane@xwiki.org", "first_name", "Misa", "last_name", "Amane");
    }

    @Before
    public void initialize() throws Exception
    {
        setDefaultlanguage();
        createXWikiAdminGroup();
        createDefaultEntry();
    }

    private void setDefaultlanguage()
    {
        translation.setLanguage("en");
    }

    private void createXWikiAdminGroup() throws Exception
    {
        if (!getUtil().pageExists("XWiki", "XWikiAdminGroup")) {
            // Add Light Yagami as member of XWikiAdminGroup
            getUtil().addObject("XWiki", "XWikiAdminGroup", "XWiki.XWikiGroups");
            getUtil().updateObject("XWiki", "XWikiAdminGroup", "XWiki.XWikiGroups", 0, "member", "XWiki.LightYagami");
        }
    }

    private void createDefaultEntry() throws Exception
    {
        // Create the default entry object

        if (flashUtil.getDefaultEntry() == null) {
            FlashEntry defaultEntry = new FlashEntry("Default",
                flashUtil.getDate(0, 0, -2, 0, 0, true),
                flashUtil.getDate(0, 0, 2, 0, 0, true),
                true,
                "weekly",
                2,
                new ArrayList<String>(Arrays.asList(flashUtil.getCurrentDayOfTheWeek())),
                new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
                "Hi! It is like hello, only shorter.");
            flashUtil.setDefaultEntry(defaultEntry);
        }

        // Create the default entry document inside the wiki
        if (!getUtil().pageExists("Flash", flashUtil.getDefaultEntry().getName())) {
            // Login as an administrator
            flashUtil.login("LightYagami", "justice");

            // Create the document
            flashUtil.createEntry(flashUtil.getDefaultEntry());

            // Go to the entry view page
            FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

            if (entryViewPage.hasPopup()) {
                FlashPopup flashPopup = entryViewPage.getPopup();
                flashPopup.clickOk();
            }

            // Re-authenticate as superadmin
            superAdmin.authenticate();
        }
    }

    @Test
    public void testGuestRights()
    {
        // Login as guest.
        flashUtil.login("guest");

        // Navigate to the application's home page where only administrators have rights.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // We should have been redirected to the login page.
        Assert.assertEquals(homePage.getMetaDataValue("space"), "XWiki");
        Assert.assertEquals(homePage.getMetaDataValue("page"), "XWikiLogin");

        // I don't care about entry page view rights since guest
        // isn't in any groups and will have no flash messages aimed towards him.
    }

    @Test
    public void testRegularUserRights()
    {
        // Login as Misa Amane (regular user).
        flashUtil.login("MisaAmane", "love");

        /*
         * Home page
         */

        // Navigate to the application's home page.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Check the not allowed message.
        Assert.assertTrue(homePage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: view
         */

        // Navigate to the default entry view page
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

        // Check the not allowed message.
        Assert.assertFalse(entryViewPage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: edit
         */

        // Navigate to the default entry edit page
        FlashEntryEditPage entryEditPage = flashUtil.getDefaultEntryEditPage();

        // Check the not allowed message.
        Assert.assertTrue(entryEditPage.containsXWikiMessage(translation.getKey("notallowed")));
    }

    @Test
    public void testAdminRights()
    {
        // Login as Light Yagami (administrator).
        flashUtil.login("LightYagami", "justice");

        /*
         * Home page
         */

        // Navigate to the application's home page.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Check the not allowed message.
        Assert.assertFalse(homePage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: view
         */

        // Navigate to the default entry view page
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

        // Check the not allowed message.
        Assert.assertFalse(entryViewPage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: edit
         */

        // Navigate to the default entry edit page
        FlashEntryEditPage entryEditPage = flashUtil.getDefaultEntryEditPage();

        // Check the not allowed message.
        Assert.assertFalse(entryEditPage.containsXWikiMessage(translation.getKey("notallowed")));
        entryEditPage.clickCancel();
    }

    @Test
    public void testApplicationPanel()
    {
        // Navigate to the Flash Messages application by clicking on the application panel entry
        ApplicationsPanel applicationPanel = ApplicationsPanel.gotoPage();
        ViewPage vp = applicationPanel.clickApplication(translation.getKey("flash.panels.quicklinktitle"));

        // Verify we're on the right page!
        Assert.assertEquals(vp.getMetaDataValue("space"), FlashHomePage.getSpace());
        Assert.assertEquals(vp.getMetaDataValue("page"), FlashHomePage.getPage());
    }

    @Test
    public void testBreadCrumb()
    {
        // Go back to the home page by clicking on the breadcrumb
        ViewPage vp = flashUtil.getDefaultEntryViewPage().clickBreadcrumbLink(translation.getKey("flash.home.title"));

        // Verify we're on the right page!
        Assert.assertEquals(vp.getMetaDataValue("space"), FlashHomePage.getSpace());
        Assert.assertEquals(vp.getMetaDataValue("page"), FlashHomePage.getPage());
    }

    @Test
    public void testLocalization()
    {
        FlashHomePage homePage;
        LiveTableElement liveTable;
        FlashEntryEditPage entryEditPage;
        FlashEntryViewPage entryViewPage;

        // Get the default entry page name
        String entryPage = flashUtil.getDefaultEntryViewPage().getMetaDataValue("page");

        // Enable support for multiple languages
        getUtil().addObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences");
        getUtil().updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "multilingual", 1);
        getUtil().updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "languages", "en,fr");
        getUtil().updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "default_language", "en");

        for (String language : Arrays.asList("en", "fr")) {
            // Set the current language
            translation.setLanguage(language);

            // Home page
            homePage = FlashHomePage.gotoPage(language);
            Assert.assertEquals(homePage.getTitle(), translation.getKey("flash.home.title"));
            Assert.assertEquals(homePage.getInfoMessage(), translation.getKey("flash.home.msginfo"));
            liveTable = homePage.getLiveTable();
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.doc.name")));
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.dateBegin")));
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.dateEnd")));
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.doc.date")));
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.groups")));
            Assert.assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.message")));

            // Entry: view
            entryViewPage = FlashEntryViewPage.gotoPage(entryPage, language);
            Assert.assertEquals(entryViewPage.getDateBeginLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateBegin"));
            Assert.assertEquals(entryViewPage.getDateEndLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateEnd"));
            Assert.assertEquals(entryViewPage.getRepeatLabel(), translation.getKeyUppercase("Flash.FlashClass_repeat"));
            Assert.assertEquals(entryViewPage.getGroupsLabel(), translation.getKeyUppercase("Flash.FlashClass_groups"));
            Assert.assertEquals(entryViewPage.getMessageLabel(),
                translation.getKeyUppercase("Flash.FlashClass_message"));

            // Entry: edit
            entryEditPage = FlashEntryEditPage.gotoPage(entryPage, language);
            Assert.assertEquals(entryEditPage.getDateBeginLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateBegin"));
            Assert.assertEquals(entryEditPage.getDateEndLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateEnd"));
            Assert.assertEquals(entryEditPage.getRepeatLabel(), translation.getKeyUppercase("Flash.FlashClass_repeat"));
            Assert.assertEquals(entryEditPage.getRepeatIntervalLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatInterval"));
            Assert.assertEquals(entryEditPage.getRepeatInterval(), flashUtil.getRepeatIntervals(translation));
            Assert.assertEquals(entryEditPage.getRepeatFrequencyLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatFrequency"));
            Assert.assertEquals(entryEditPage.getRepeatDaysLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatDays"));
            Assert.assertEquals(entryEditPage.getRepeatDays(), flashUtil.getDaysOfTheWeek(translation));
            Assert.assertEquals(entryEditPage.getRepeatSummaryLabel(),
                translation.getKeyUppercase("flash.repeat.summary"));
            Assert.assertEquals(entryEditPage.getGroupsLabel(), translation.getKeyUppercase("Flash.FlashClass_groups"));
            Assert.assertEquals(entryEditPage.getMessageLabel(),
                translation.getKeyUppercase("Flash.FlashClass_message"));
            entryEditPage.clickCancel();
        }
    }

    @Test
    public void testDataEntry()
    {
        // Login as Light Yagami (administrator).
        flashUtil.login("LightYagami", "justice");

        // Go to the application's home page
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Livetable
        LiveTableElement liveTable = homePage.getLiveTable();
        Assert.assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.doc.name"),
            flashUtil.getDefaultEntryName()));
        Assert.assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.dateBegin"),
            flashUtil.getDefaultEntryFormattedDateBegin()));
        Assert.assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.dateEnd"),
            flashUtil.getDefaultEntryFormattedDateEnd()));
        Assert.assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.message"),
            flashUtil.getDefaultEntryMessage()));

        // Entry view
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();
        // This is probably the dumbest thing I wrote
        // The date format in the livetable uses a single space where as in the rest of the app a double space separator is used between the date and time
        Assert.assertEquals(entryViewPage.getDateBegin(),
            flashUtil.getDefaultEntryFormattedDateBegin().replace("  ", " "));
        Assert.assertEquals(entryViewPage.getDateEnd(), flashUtil.getDefaultEntryFormattedDateEnd().replace("  ", " "));
        Assert.assertEquals(entryViewPage.getMessage(), flashUtil.getDefaultEntryMessage());

        // Slider
        FlashSlider flashSlider = entryViewPage.getSlider();
        Assert.assertTrue(flashSlider.containsMessage(flashUtil.getDefaultEntryFormattedDateBegin(),
            flashUtil.getDefaultEntryMessage()));
    }

    @Test
    public void testMarkingAsSeen() throws Exception
    {
        FlashEntry entry = new FlashEntry("MarkAsSeen",
            flashUtil.getDate(0, 0, 0, -1, 0, true),
            flashUtil.getDate(0, 0, 0, 1, 0, true), 
            true, 
            "daily", 
            1, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Daily");
        
        // Create test message and close the 1st time pop-up
        FlashEntryViewPage entryViewPage = flashUtil.testMessage(entry, true);

        // Reload page as viewing a second time
        entryViewPage.reload();

        // The pop-up should be present only once per user
        // And it should have been shown during the creation phase
        Assert.assertFalse(entryViewPage.hasPopup());
    }

    @Test
    public void testNonRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("NonRecurringActiveMessage",
            flashUtil.getDate(0, 0, 0, 0, -1, false),
            flashUtil.getDate(0, 0, 0, 0, 1, false), 
            false, 
            "daily", 
            1, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "NonRecurringActiveMessage");
        
        flashUtil.testMessage(entry, true);
    }

    @Test
    public void testNonRecurringInctiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("NonRecurringInactiveMessage",
            flashUtil.getDate(0, 0, 0, 0, -5, false),
            flashUtil.getDate(0, 0, 0, 0, -2, false), 
            false, 
            "daily", 
            1, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "NonRecurringInactiveMessage");
        
        flashUtil.testMessage(entry, false);
    }

    @Test
    public void testDailyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("DailyRecurringActiveMessage",
            flashUtil.getDate(0, 0, 0, -2, 0, true),
            flashUtil.getDate(0, 0, 0, 2, 0, true), 
            true, 
            "daily", 
            2, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "Every 2 days");
        
        flashUtil.testMessage(entry, true);
    }

    @Test
    public void testDailyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("DailyRecurringInactiveMessage",
            flashUtil.getDate(0, 0, 0, -2, 0, true),
            flashUtil.getDate(0, 0, 1, 1, 0, true), 
            true, 
            "daily", 
            5, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "Every 3 days");
        
        flashUtil.testMessage(entry, false);
    }

    @Test
    public void testWeeklyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("WeeklyRecurringActiveMessage",
            flashUtil.getDate(0, -2, 0, 0, 0, true),
            flashUtil.getDate(0, 0, 2, 0, 0, true),
            true, 
            "weekly", 
            2, 
            new ArrayList<String>(Arrays.asList(flashUtil.getCurrentDayOfTheWeek())), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 2 weeks");
        
        flashUtil.testMessage(entry, true);
    }

    @Test
    public void testWeeklyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("WeeklyRecurringInactiveMessage",
            flashUtil.getDate(0, 0, -2, 0, 0, true),
            flashUtil.getDate(0, 0, 2, 1, 0, true), 
            true, 
            "weekly", 
            2, 
            new ArrayList<String>(Arrays.asList(flashUtil.getCurrentDayOfTheWeek() == "monday" ? "tuesday":"monday")), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 2 weeks");
        
        flashUtil.testMessage(entry, false);
    }

    @Test
    public void testMonthlyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("MonthlyRecurringActiveMessage",
            flashUtil.getDate(-2, 0, 0, 0, 0, true),
            flashUtil.getDate(5, 0, 0, 0, 0, true), 
            true, 
            "monthly", 
            6, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 6 months");
        
        flashUtil.testMessage(entry, true);
    }

    @Test
    public void testMonthlyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("MonthlyRecurringInactiveMessage",
            flashUtil.getDate(-4, 0, 0, 0, 0, true),
            flashUtil.getDate(3, 0, 0, 0, 0, true), 
            true, 
            "monthly", 
            9, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 9 months");
        
        flashUtil.testMessage(entry, false);
    }

    @Test
    public void testYearlyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("YearlyRecurringActiveMessage",
            flashUtil.getDate(-2, 0, 0, 0, 0, true),
            flashUtil.getDate(5, 0, 0, 0, 0, true), 
            true, 
            "yearly", 
            2, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 2 years today");
        
        flashUtil.testMessage(entry, true);
    }

    @Test
    public void testYearlyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("YearlyRecurringInactiveMessage",
            flashUtil.getDate(-4, 0, 0, 0, 0, true),
            flashUtil.getDate(3, 0, 0, 0, 0, true), 
            true, 
            "yearly", 
            3, 
            new ArrayList<String>(), 
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "Every 3 years today");
        
        flashUtil.testMessage(entry, false);
    }
}
