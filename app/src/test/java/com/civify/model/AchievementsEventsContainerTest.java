package com.civify.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.civify.model.achievement.Achievement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AchievementsEventsContainerTest {

    private AchievementsEventsContainer mAchievementsEventsContainer;
    private List<Achievement> mAchievements;
    private List<EventStub> mEvents;

    @Before
    public void setUp() {
        this.mAchievementsEventsContainer = new AchievementsEventsContainer();
        mAchievements = Arrays.asList(mock(Achievement.class), mock(Achievement.class));
        mEvents = Arrays.asList(mock(EventStub.class), mock(EventStub.class));
    }

    @After
    public void tearDown() {
        this.mAchievementsEventsContainer = null;
    }

    @Test
    public void testAchievements() {
        mAchievementsEventsContainer.setAchievementList(mAchievements);
        assertThat(mAchievementsEventsContainer.getAchievementList(), is(mAchievements));
    }

    @Test
    public void testEvents() {
        mAchievementsEventsContainer.setEventList(mEvents);
        assertThat(mAchievementsEventsContainer.getEventList(), is(mEvents));
    }

    @Test
    public void testConstructor() {
        mAchievementsEventsContainer = new AchievementsEventsContainer(mAchievements, mEvents);
        assertThat(mAchievementsEventsContainer.getAchievementList(), is(mAchievements));
        assertThat(mAchievementsEventsContainer.getEventList(), is(mEvents));
    }
}
