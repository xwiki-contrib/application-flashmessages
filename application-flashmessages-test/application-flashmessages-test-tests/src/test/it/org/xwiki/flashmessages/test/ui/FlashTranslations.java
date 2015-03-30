package org.xwiki.flashmessages.test.ui;

import java.util.HashMap;

/**
 * Represents a Flash Messages translations file.
 *
 * @version $Id$
 * @since
 */
public class FlashTranslations
{
    private String language;

    private HashMap<String, HashMap<String, String>> translations;

    private static FlashTranslations instance;

    protected FlashTranslations()
    {
        this.language = "en";
        this.translations = new HashMap<String, HashMap<String, String>>();

        /*
         * English
         */

        HashMap<String, String> english = addLanguage("en");

        // Core
        english.put("notallowed", "You are not allowed to view this document or perform this action.");

        // Administration
        english.put("admin.flashmessages", "Flash Messages");
        english.put("flash.creation.utcinfo", "The time used for alerts is based on UTC. To check the UTC time you can use the button 'now' in the calendar");
        english.put("flash.home.title", "Flash entries");
        english.put("flash.home.msginfo", "See here the history of FLASH messages published on the knowledge base.");
        english.put("flash.panels.quicklinktitle", "Flash Messages");

        // Livetable
        english.put("flash.livetable.doc.name", "Name");
        english.put("flash.livetable.dateBegin", "Start date");
        english.put("flash.livetable.dateEnd", "End date");
        english.put("flash.livetable.groups", "Groups");
        english.put("flash.livetable.message", "Message");
        english.put("flash.livetable.doc.date", "Update date");
        english.put("flash.livetable._actions.edit", "Edit");
        english.put("flash.livetable._actions.delete", "Delete");
        english.put("flash.livetable._actions", "Actions");

        // Class
        english.put("Flash.FlashClass_dateBegin", "Start date");
        english.put("Flash.FlashClass_dateEnd", "End date");
        english.put("Flash.FlashClass_repeat", "Repeat");
        english.put("Flash.FlashClass_repeatInterval", "Repeat interval");
        english.put("Flash.FlashClass_repeatInterval_daily", "Daily");
        english.put("Flash.FlashClass_repeatInterval_weekly", "Weekly");
        english.put("Flash.FlashClass_repeatInterval_monthly", "Monthly");
        english.put("Flash.FlashClass_repeatInterval_yearly", "Yearly");
        english.put("Flash.FlashClass_repeatFrequency", "Repeat frequency");
        english.put("Flash.FlashClass_repeatDays", "Repeat days");
        english.put("Flash.FlashClass_repeatDays_monday", "Monday");
        english.put("Flash.FlashClass_repeatDays_tuesday", "Tuesday");
        english.put("Flash.FlashClass_repeatDays_wednesday", "Wednesday");
        english.put("Flash.FlashClass_repeatDays_thursday", "Thursday");
        english.put("Flash.FlashClass_repeatDays_friday", "Friday");
        english.put("Flash.FlashClass_repeatDays_saturday", "Saturday");
        english.put("Flash.FlashClass_repeatDays_sunday", "Sunday");
        english.put("Flash.FlashClass_message", "Message");
        english.put("Flash.FlashClass_groups", "Groups");

        // Sheet
        english.put("flash.repeat.summary", "Repeat summary");
        english.put("flash.repeat.summary.every", "Every");
        english.put("flash.repeat.summary.on", "on");
        english.put("flash.repeat.summary.day", "day");
        english.put("flash.repeat.summary.daily", "days");
        english.put("flash.repeat.summary.weekly", "weeks");
        english.put("flash.repeat.summary.monthly", "months");
        english.put("flash.repeat.summary.yearly", "years");

        /*
         * French
         */

        HashMap<String, String> french = addLanguage("fr");

        // Core
        french.put("notallowed", "Vous n'êtes pas autorisé à voir ce document ou à réaliser cette action.");

        // Administration
        french.put("admin.flashmessages", "Messages Flash");
        french.put("flash.creation.utcinfo", "Le temps utilisé pour les alertes est basé sur UTC. Pour vérifier l'heure UTC, vous pouvez utiliser le bouton 'now' dans le calendrier");
        french.put("flash.home.title", "Entrées flash");
        french.put("flash.home.msginfo", "Voir ici l’historique de messages Flash publiés sur la base de connaissances.");
        french.put("flash.panels.quicklinktitle", "Messages Flash");

        // Livetable
        french.put("flash.livetable.doc.name", "Nom");
        french.put("flash.livetable.dateBegin", "Date de début");
        french.put("flash.livetable.dateEnd", "Date de fin");
        french.put("flash.livetable.groups", "Groupes");
        french.put("flash.livetable.message", "Message");
        french.put("flash.livetable.doc.date", "Date de mise à jour");
        french.put("flash.livetable._actions.edit", "Éditer");
        french.put("flash.livetable._actions.delete", "Supprimer");
        french.put("flash.livetable._actions", "Actions");

        // Class
        french.put("Flash.FlashClass_dateBegin", "Date de début");
        french.put("Flash.FlashClass_dateEnd", "Date de fin");
        french.put("Flash.FlashClass_repeat", "Répétition");
        french.put("Flash.FlashClass_repeatInterval", "Intervalle de répétition");
        french.put("Flash.FlashClass_repeatInterval_daily", "Tous les jours");
        french.put("Flash.FlashClass_repeatInterval_weekly", "Hebdomadaire");
        french.put("Flash.FlashClass_repeatInterval_monthly", "Mensuel");
        french.put("Flash.FlashClass_repeatInterval_yearly", "Annuel");
        french.put("Flash.FlashClass_repeatFrequency", "La fréquence de répétition");
        french.put("Flash.FlashClass_repeatDays", "Jours de répétition");
        french.put("Flash.FlashClass_repeatDays_monday", "Lundi");
        french.put("Flash.FlashClass_repeatDays_tuesday", "Mardi");
        french.put("Flash.FlashClass_repeatDays_wednesday", "Mercredi");
        french.put("Flash.FlashClass_repeatDays_thursday", "Jeudi");
        french.put("Flash.FlashClass_repeatDays_friday", "Vendredi");
        french.put("Flash.FlashClass_repeatDays_saturday", "Samedi");
        french.put("Flash.FlashClass_repeatDays_sunday", "Dimanche");
        french.put("Flash.FlashClass_message", "Message");
        french.put("Flash.FlashClass_groups", "Groupes");

        // Sheet
        french.put("flash.repeat.summary", "Résumé de répétition");
        french.put("flash.repeat.summary.every", "Tous les");
        french.put("flash.repeat.summary.on", "sur");
        french.put("flash.repeat.summary.day", "jour");
        french.put("flash.repeat.summary.daily", "journées");
        french.put("flash.repeat.summary.weekly", "semaines");
        french.put("flash.repeat.summary.monthly", "mois");
        french.put("flash.repeat.summary.yearly", "ans");

    }

    /**
     * Get the singleton instance
     * 
     * @return the FlashTranslations instance
     */
    public static FlashTranslations getInstance()
    {
        if (instance == null) {
            instance = new FlashTranslations();
        }

        return instance;
    }

    /**
     * Add a new language
     * 
     * @param language the two letter language code
     * @return the blank entry in the translations map corresponding to the new language
     */
    private HashMap<String, String> addLanguage(String language)
    {
        this.translations.put(language, new HashMap<String, String>());
        return this.translations.get(language);
    }

    /**
     * Get the current language
     * 
     * @return the current language two letter code
     */
    public String getLanguage()
    {
        return this.language;
    }

    /**
     * Set the current language
     * 
     * @param language
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * Get the value for a translation key
     * 
     * @param key a translation key
     * @return the string value corresponding to the translation key
     */
    public String getKey(String key)
    {
        return translations.get(this.language).get(key);
    }

    /**
     * Get the value for a translation key in specific language
     * 
     * @param key a translation key
     * @param language the language code for the language from which to take the key value
     * @return the string value corresponding to the translation key
     */
    public String getKey(String key, String language)
    {
        return translations.get(language).get(key);
    }

    /**
     * Get the value of a key in upper-case
     * 
     * @param key a translation key
     * @return the upper-case string value corresponding to the translation key
     */
    public String getKeyUppercase(String key)
    {
        return getKey(key).toUpperCase();
    }

    /**
     * Get the value of a key in upper-case
     * 
     * @param key a translation key
     * @param language the language code for the language from which to take the key value
     * @return the upper-case string value corresponding to the translation key
     */
    public String getKeyUppercase(String key, String language)
    {
        return getKey(key, language).toUpperCase();
    }
}
