package org.xwiki.flashmessages.test.ui;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a Flash Messages entry.
 *
 * @version $Id$
 * @since
 */
public class FlashEntry
{
    private String name;

    private Calendar dateBegin;

    private Calendar dateEnd;

    private Boolean repeat;

    private String repeatInterval;

    private int repeatFrequency;

    private ArrayList<String> repeatDays;

    private ArrayList<String> groups;

    private String message;

    /**
     * FlashEntry constructor
     * 
     * @param name the name of the entry
     * @param dateBegin the entry start date
     * @param dateEnd the entry end date
     * @param repeat is it a recurring message
     * @param repeatInterval the repeat interval
     * @param repeatFrequency the repeat frequency
     * @param repeatDays the repeat days
     * @param groups the xwiki groups
     * @param message the display message
     */
    public FlashEntry(String name, Calendar dateBegin, Calendar dateEnd, Boolean repeat, String repeatInterval,
        int repeatFrequency, ArrayList<String> repeatDays, ArrayList<String> groups, String message)
    {
        this.name = name;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.repeat = repeat;
        this.repeatInterval = repeatInterval;
        this.repeatFrequency = repeatFrequency;
        this.repeatDays = repeatDays;
        this.groups = groups;
        this.message = message;
    }

    /**
     * Get entry name
     * 
     * @return the entry's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set entry name
     * 
     * @param name the entry's name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set start date
     * 
     * @return the entry's start date
     */
    public Calendar getDateBegin()
    {
        return dateBegin;
    }

    /**
     * Set start date
     * 
     * @param dateBegin the entry's start date
     */
    public void setDateBegin(Calendar dateBegin)
    {
        this.dateBegin = dateBegin;
    }

    /**
     * Get end date
     * 
     * @return the entry's end date
     */
    public Calendar getDateEnd()
    {
        return dateEnd;
    }

    /**
     * Set end date
     * 
     * @param dateEnd the entry's end date
     */
    public void setDateEnd(Calendar dateEnd)
    {
        this.dateEnd = dateEnd;
    }

    /**
     * Get repeat
     * 
     * @return
     */
    public Boolean getRepeat()
    {
        return repeat;
    }

    /**
     * Set repeat
     * 
     * @param repeat
     */
    public void setRepeat(Boolean repeat)
    {
        this.repeat = repeat;
    }

    /**
     * Get repeat interval
     * 
     * @return the period of time by which the entry repeats
     */
    public String getRepeatInterval()
    {
        return repeatInterval;
    }

    /**
     * Set repeat interval
     * 
     * @param the period of time by which the entry repeats
     */
    public void setRepeatInterval(String repeatInterval)
    {
        this.repeatInterval = repeatInterval;
    }

    /**
     * Get repeat frequency
     * 
     * @return the frequency with which the entry is displayed
     */
    public int getRepeatFrequency()
    {
        return repeatFrequency;
    }

    /**
     * Set repeat frequency
     * 
     * @param the frequency with which the entry is displayed
     */
    public void setRepeatFrequency(int repeatFrequency)
    {
        this.repeatFrequency = repeatFrequency;
    }

    /**
     * Get repeat days
     * 
     * @return the list of days in which the entry is active
     */
    public ArrayList<String> getRepeatDays()
    {
        return repeatDays;
    }

    /**
     * Set repeat days
     * 
     * @param repeatDays the list of days in which the entry is active
     */
    public void setRepeatDays(ArrayList<String> repeatDays)
    {
        this.repeatDays = repeatDays;
    }

    /**
     * Get groups
     * 
     * @return the list of groups towards the entry is aimed
     */
    public ArrayList<String> getGroups()
    {
        return groups;
    }

    /**
     * Set groups
     * 
     * @param groups the list of groups towards the entry is aimed
     */
    public void setGroups(ArrayList<String> groups)
    {
        this.groups = groups;
    }

    /**
     * Get message
     * 
     * @return the entry display message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Set message
     * 
     * @param message the entry display message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}
