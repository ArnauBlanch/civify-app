package com.civify.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewAchievementsEventsTest {

    private NewAchievementsEvents mNewAchievementsEvents;

    @Before
    public void setUp() {
        mNewAchievementsEvents = new NewAchievementsEvents();
    }

    @After
    public void tearDown() {
        mNewAchievementsEvents = null;
    }

    @Test
    public void testAchievements() {
        mNewAchievementsEvents.setNewAchievements(true);
        assertThat(mNewAchievementsEvents.isNewAchievements(), is(true));
    }

    @Test
    public void testEvents() {
        mNewAchievementsEvents.setNewEvents(true);
        assertThat(mNewAchievementsEvents.isNewEvents(), is(true));
    }
}
