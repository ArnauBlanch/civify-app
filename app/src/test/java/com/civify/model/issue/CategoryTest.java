package com.civify.model.issue;

import static junit.framework.Assert.assertEquals;

import com.civify.R;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
    private Category mCategory;

    @Before
    public void setUp() {
        this.mCategory = Category.ILLUMINATION;
    }

    @Test
    public void testMarker() {
        assertEquals(R.drawable.street_lights_pin, mCategory.getMarker());
    }

    @Test
    public void testIcon() {
        assertEquals(R.drawable.street_lights_icon, mCategory.getIcon());
    }

    @Test
    public void testName() {
        assertEquals(R.string.illumination, mCategory.getName());
    }
}
