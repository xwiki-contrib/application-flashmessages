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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xwiki.flashmessages.test.po.FlashEntryEditPage;
import org.xwiki.flashmessages.test.po.FlashEntryViewPage;
import org.xwiki.flashmessages.test.po.FlashHomePage;
import org.xwiki.flashmessages.test.po.FlashSlider;
import org.xwiki.panels.test.po.ApplicationsPanel;
import org.xwiki.test.docker.junit5.ExtensionOverride;
import org.xwiki.test.docker.junit5.UITest;
import org.xwiki.test.integration.junit.LogCaptureConfiguration;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.test.ui.po.LiveTableElement;
import org.xwiki.test.ui.po.ViewPage;
import org.xwiki.test.ui.po.editor.ObjectEditPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI tests for the Flash Messages application.
 *
 * @version $Id$
 * @since 1.7
 */
@UITest(
    //TODO After the licensing app has 14.10+ as its parent, remove the overrides and the extra jars.
    extensionOverrides = {
        @ExtensionOverride(
            extensionId = "com.google.code.findbugs:jsr305",
            overrides = {
                "features=com.google.code.findbugs:annotations"
            }
        ),
        @ExtensionOverride(
            extensionId = "org.bouncycastle:bcprov-jdk18on",
            overrides = {
                "org.bouncycastle:bcprov-jdk15"
            }
        )
    },
    extraJARs = {
        "org.bouncycastle:bcprov-jdk15on:jar:1.64",
        // The Solr store is not ready yet to be installed as extension
        "org.xwiki.platform:xwiki-platform-eventstream-store-solr:14.10",
        "org.xwiki.platform:xwiki-platform-search-solr-query:14.10"
    }
)
class FlashIT
{
    private FlashUtil flashUtil;

    private FlashTranslations translation = FlashTranslations.getInstance();

    @BeforeAll
    static void createUsers(TestUtils setup)
    {
        // Create administrator
        setup.createUser("LightYagami", "justice", "email", "light.yagami@xwiki.org", "first_name", "Light",
            "last_name", "Yagami");

        // Create normal user
        setup.createUser("MisaAmane", "love", "email", "misa.amane@xwiki.org", "first_name", "Misa", "last_name",
            "Amane");
    }

    @BeforeEach
    void initialize(TestUtils setup) throws Exception
    {
        this.flashUtil = FlashUtil.getInstance(setup);
        setDefaultlanguage();
        createXWikiAdminGroup(setup);
        createDefaultEntry(setup);
    }

    private void setDefaultlanguage()
    {
        translation.setLanguage("en");
    }

    private void createXWikiAdminGroup(TestUtils setup) throws Exception
    {
        if (!setup.pageExists("XWiki", "XWikiAdminGroup")) {
            // Add Light Yagami as member of XWikiAdminGroup
            setup.loginAsSuperAdmin();
            setup.addObject("XWiki", "XWikiAdminGroup", "XWiki.XWikiGroups", "member", "XWiki.LightYagami");
            setup.addObject("XWiki", "XWikiPreferences", "XWiki.XWikiGlobalRights", "groups", "XWiki.XWikiAdminGroup",
                "allow", 1, "levels", "admin");
            // Make sure the wiki administration is not locked by the superadmin user.
            new ObjectEditPage().clickCancel();
        }
    }

    private void createDefaultEntry(TestUtils setup) throws Exception
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
                "currentWiki",
                "Hi! It is like hello, only shorter.");
            flashUtil.setDefaultEntry(defaultEntry);
        }

        // Create the default entry document inside the wiki
        if (!setup.pageExists("Flash", flashUtil.getDefaultEntry().getName())) {
            // Login as an administrator
            flashUtil.login("LightYagami", "justice");

            // Create the document
            flashUtil.createEntry(flashUtil.getDefaultEntry());

            // Wait for flashmessage to be registered with solr.
            flashUtil.waitUntilSolrReindex();

            // Go to the entry view page
            FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

            if (entryViewPage.hasPopup()) {
                entryViewPage.getPopup().clickOk();
            }

            // Re-authenticate as superadmin
            setup.loginAsSuperAdmin();
        }
    }

    @Test
    void testGuestRights()
    {
        // Login as guest.
        flashUtil.login("guest");

        // Navigate to the application's home page where only administrators have rights.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // We should have been redirected to the login page.
        assertEquals(homePage.getMetaDataValue("space"), "XWiki");
        assertEquals(homePage.getMetaDataValue("page"), "XWikiLogin");

        // I don't care about entry page view rights since guest
        // isn't in any groups and will have no flash messages aimed towards him.
    }

    @Test
    void testRegularUserRights()
    {
        // Login as Misa Amane (regular user).
        flashUtil.login("MisaAmane", "love");

        /*
         * Home page
         */

        // Navigate to the application's home page.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Check the not allowed message.
        assertTrue(homePage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: view
         */

        // Navigate to the default entry view page
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

        // Check the not allowed message.
        assertFalse(entryViewPage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: edit
         */

        // Navigate to the default entry edit page
        FlashEntryEditPage entryEditPage = flashUtil.getDefaultEntryEditPage();

        // Check the not allowed message.
        assertTrue(entryEditPage.containsXWikiMessage(translation.getKey("notallowed")));
    }

    @Test
    void testAdminRights()
    {
        // Login as Light Yagami (administrator).
        flashUtil.login("LightYagami", "justice");

        /*
         * Home page
         */

        // Navigate to the application's home page.
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Check the not allowed message.
        assertFalse(homePage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: view
         */

        // Navigate to the default entry view page
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();

        // Check the not allowed message.
        assertFalse(entryViewPage.containsXWikiMessage(translation.getKey("notallowed")));

        /*
         * Entry: edit
         */

        // Navigate to the default entry edit page
        FlashEntryEditPage entryEditPage = flashUtil.getDefaultEntryEditPage();

        // Check the not allowed message.
        assertFalse(entryEditPage.containsXWikiMessage(translation.getKey("notallowed")));
        entryEditPage.clickCancel();
    }

    @Test
    void testApplicationPanel()
    {
        // Navigate to the Flash Messages application by clicking on the application panel entry
        ApplicationsPanel applicationPanel = ApplicationsPanel.gotoPage();
        ViewPage vp = applicationPanel.clickApplication(translation.getKey("flash.panels.quicklinktitle"));

        // Verify we're on the right page!
        assertEquals(vp.getMetaDataValue("space"), FlashHomePage.getSpace());
        assertEquals(vp.getMetaDataValue("page"), FlashHomePage.getPage());
    }

    @Test
    void testBreadCrumb()
    {
        // Go back to the home page by clicking on the breadcrumb
        ViewPage vp = flashUtil.getDefaultEntryViewPage().clickBreadcrumbLink(translation.getKey("flash.home.title"));

        // Verify we're on the right page!
        assertEquals(vp.getMetaDataValue("space"), FlashHomePage.getSpace());
        assertEquals(vp.getMetaDataValue("page"), FlashHomePage.getPage());
    }

    @Test
    void testLocalization(TestUtils setup)
    {
        FlashHomePage homePage;
        LiveTableElement liveTable;
        FlashEntryEditPage entryEditPage;
        FlashEntryViewPage entryViewPage;

        // Get the default entry page name
        String entryPage = flashUtil.getDefaultEntryName();

        // Enable support for multiple languages
        this.flashUtil.login("LightYagami", "justice");
        setup.addObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences");
        setup.updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "multilingual", 1);
        setup.updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "languages", "en,fr");
        setup.updateObject("XWiki", "XWikiPreferences", "XWiki.XWikiPreferences", 0, "default_language", "en");

        for (String language : Arrays.asList("en", "fr")) {
            // Set the current language
            translation.setLanguage(language);

            // Home page
            homePage = FlashHomePage.gotoPage(language);
            assertEquals(translation.getKey("flash.home.title"), homePage.getTitle());
            assertEquals(homePage.getInfoMessage(), translation.getKey("flash.home.msginfo"));
            liveTable = homePage.getLiveTable();
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.doc.title")));
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.dateBegin")));
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.dateEnd")));
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.doc.date")));
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.groups")));
            assertTrue(liveTable.hasColumn(translation.getKey("flash.livetable.message")));

            // Entry: view
            entryViewPage = FlashEntryViewPage.gotoPage(entryPage, language);
            assertEquals(entryViewPage.getDateBeginLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateBegin"));
            assertEquals(entryViewPage.getDateEndLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateEnd"));
            assertEquals(entryViewPage.getRepeatLabel(), translation.getKeyUppercase("Flash.FlashClass_repeat"));
            assertEquals(entryViewPage.getGroupsLabel(), translation.getKeyUppercase("Flash.FlashClass_groups"));
            assertEquals(entryViewPage.getMessageLabel(),
                translation.getKeyUppercase("Flash.FlashClass_message"));

            // Entry: edit
            entryEditPage = FlashEntryEditPage.gotoPage(entryPage, language);
            assertEquals(entryEditPage.getDateBeginLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateBegin"));
            assertEquals(entryEditPage.getDateEndLabel(),
                translation.getKeyUppercase("Flash.FlashClass_dateEnd"));
            assertEquals(entryEditPage.getRepeatLabel(), translation.getKeyUppercase("Flash.FlashClass_repeat"));
            assertEquals(entryEditPage.getRepeatIntervalLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatInterval"));
            assertEquals(entryEditPage.getRepeatInterval(), flashUtil.getRepeatIntervals(translation));
            assertEquals(entryEditPage.getRepeatFrequencyLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatFrequency"));
            assertEquals(entryEditPage.getRepeatDaysLabel(),
                translation.getKeyUppercase("Flash.FlashClass_repeatDays"));
            assertEquals(entryEditPage.getRepeatDays(), flashUtil.getDaysOfTheWeek(translation));
            assertEquals(entryEditPage.getRepeatSummaryLabel(),
                translation.getKeyUppercase("flash.repeat.summary"));
            assertEquals(entryEditPage.getGroupsLabel(), translation.getKeyUppercase("Flash.FlashClass_groups"));
            assertEquals(entryEditPage.getMessageLabel(),
                translation.getKeyUppercase("Flash.FlashClass_message"));
            entryEditPage.clickCancel();
        }
    }

    @Test
    void testDataEntry()
    {
        // Login as Light Yagami (administrator).
        flashUtil.login("LightYagami", "justice");

        // Go to the application's home page
        FlashHomePage homePage = FlashHomePage.gotoPage();

        // Livetable
        LiveTableElement liveTable = homePage.getLiveTable();
        assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.doc.title"),
            flashUtil.getDefaultEntryName()));
        assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.dateBegin"),
            flashUtil.getDefaultEntryFormattedDateBegin()));
        assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.dateEnd"),
            flashUtil.getDefaultEntryFormattedDateEnd()));
        assertTrue(liveTable.hasRow(translation.getKey("flash.livetable.message"),
            flashUtil.getDefaultEntryMessage()));

        // Entry view
        FlashEntryViewPage entryViewPage = flashUtil.getDefaultEntryViewPage();
        // This is probably the dumbest thing I wrote
        // The date format in the livetable uses a single space where as in the rest of the app a double space separator
        // is used between the date and time
        assertEquals(entryViewPage.getDateBegin(),
            flashUtil.getDefaultEntryFormattedDateBegin().replace("  ", " "));
        assertEquals(entryViewPage.getDateEnd(), flashUtil.getDefaultEntryFormattedDateEnd().replace("  ", " "));
        assertEquals(entryViewPage.getMessage(), flashUtil.getDefaultEntryMessage());

        // Slider
        FlashSlider flashSlider = entryViewPage.getSlider();
        assertTrue(flashSlider.containsMessage(flashUtil.getDefaultEntryFormattedDateBegin(),
            flashUtil.getDefaultEntryMessage()));
    }

    @Test
    void testMarkingAsSeen() throws Exception
    {
        FlashEntry entry = new FlashEntry("MarkAsSeen",
            flashUtil.getDate(0, 0, 0, -1, 0, true),
            flashUtil.getDate(0, 0, 0, 1, 0, true),
            true,
            "daily",
            1,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Daily");

        // Create test message and close the 1st time pop-up
        FlashEntryViewPage entryViewPage = flashUtil.testMessage(entry, true);

        // Reload page as viewing a second time
        entryViewPage = entryViewPage.reload();

        // The pop-up should be present only once per user
        // And it should have been shown during the creation phase
        assertFalse(entryViewPage.hasPopup());
    }

    @Test
    void testNonRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("NonRecurringActiveMessage",
            flashUtil.getDate(0, 0, 0, 0, -1, false),
            flashUtil.getDate(0, 0, 0, 0, 1, false),
            false,
            "daily",
            1,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "currentWiki",
            "NonRecurringActiveMessage");

        flashUtil.testMessage(entry, true);
    }

    @Test
    void testNonRecurringInctiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("NonRecurringInactiveMessage",
            flashUtil.getDate(0, 0, 0, 0, -5, false),
            flashUtil.getDate(0, 0, 0, 0, -2, false),
            false,
            "daily",
            1,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "currentWiki",
            "NonRecurringInactiveMessage");

        flashUtil.testMessage(entry, false);
    }

    @Test
    void testDailyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("DailyRecurringActiveMessage",
            flashUtil.getDate(0, 0, 0, -2, 0, true),
            flashUtil.getDate(0, 0, 0, 2, 0, true),
            true,
            "daily",
            2,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "currentWiki",
            "Every 2 days");

        flashUtil.testMessage(entry, true);
    }

    @Test
    void testDailyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("DailyRecurringInactiveMessage",
            flashUtil.getDate(0, 0, 0, -2, 0, true),
            flashUtil.getDate(0, 0, 1, 1, 0, true),
            true,
            "daily",
            5,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAdminGroup")),
            "currentWiki",
            "Every 3 days");

        flashUtil.testMessage(entry, false);
    }

    @Test
    void testWeeklyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("WeeklyRecurringActiveMessage",
            flashUtil.getDate(0, -2, 0, 0, 0, true),
            flashUtil.getDate(0, 0, 2, 0, 0, true),
            true,
            "weekly",
            2,
            new ArrayList<String>(Arrays.asList(flashUtil.getCurrentDayOfTheWeek())),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 2 weeks");

        flashUtil.testMessage(entry, true);
    }

    @Test
    void testWeeklyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("WeeklyRecurringInactiveMessage",
            flashUtil.getDate(0, 0, -2, 0, 0, true),
            flashUtil.getDate(0, 0, 2, 1, 0, true),
            true,
            "weekly",
            2,
            new ArrayList<String>(Arrays.asList(flashUtil.getCurrentDayOfTheWeek() == "monday" ? "tuesday" : "monday")),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 2 weeks");

        flashUtil.testMessage(entry, false);
    }

    @Test
    void testMonthlyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("MonthlyRecurringActiveMessage",
            flashUtil.getDate(-2, 0, 0, 0, 0, true),
            flashUtil.getDate(5, 0, 0, 0, 0, true),
            true,
            "monthly",
            6,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 6 months");

        flashUtil.testMessage(entry, true);
    }

    @Test
    void testMonthlyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("MonthlyRecurringInactiveMessage",
            flashUtil.getDate(-4, 0, 0, 0, 0, true),
            flashUtil.getDate(3, 0, 0, 0, 0, true),
            true,
            "monthly",
            9,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 9 months");

        flashUtil.testMessage(entry, false);
    }

    @Test
    void testYearlyRecurringActiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("YearlyRecurringActiveMessage",
            flashUtil.getDate(-2, 0, 0, 0, 0, true),
            flashUtil.getDate(5, 0, 0, 0, 0, true),
            true,
            "yearly",
            2,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 2 years today");

        flashUtil.testMessage(entry, true);
    }

    @Test
    void testYearlyRecurringInactiveMessage() throws Exception
    {
        FlashEntry entry = new FlashEntry("YearlyRecurringInactiveMessage",
            flashUtil.getDate(-4, 0, 0, 0, 0, true),
            flashUtil.getDate(3, 0, 0, 0, 0, true),
            true,
            "yearly",
            3,
            new ArrayList<String>(),
            new ArrayList<String>(Arrays.asList("XWikiAllGroup")),
            "currentWiki",
            "Every 3 years today");

        flashUtil.testMessage(entry, false);
    }

    @AfterEach
    void tearDown(LogCaptureConfiguration logCaptureConfiguration)
    {
        logCaptureConfiguration.registerExcludes(
            // The icon theme picker used on the XWiki.XWikiPreferences requires programming right.
            "QueryException: The query requires programming right."
        );
    }
}
