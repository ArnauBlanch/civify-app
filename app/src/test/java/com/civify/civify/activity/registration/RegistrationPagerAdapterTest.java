package com.civify.civify.activity.registration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RegistrationPagerAdapter.class)
public class RegistrationPagerAdapterTest {
    private RegistrationPagerAdapter mRegistrationPagerAdapter;

    @Before
    public void setUp() {
        mRegistrationPagerAdapter = new RegistrationPagerAdapter(mock(FragmentManager.class));
    }

    @After
    public void tearDown() {
        mRegistrationPagerAdapter = null;
    }

    @Test
    public void testGetNegativeItem() {
        assertThat(mRegistrationPagerAdapter.getItem(-2), nullValue());
    }

    @Test
    public void testGetInvalidItem() {
        assertThat(mRegistrationPagerAdapter.getItem(10), nullValue());
    }

    @Test
    public void testGetValidItem() throws Exception {
        RegistrationFragment newFragment = mock(RegistrationFragment.class);
        Bundle mockBundle = mock(Bundle.class);
        whenNew(RegistrationFragment.class).withNoArguments()
                .thenReturn(newFragment);
        whenNew(Bundle.class).withNoArguments().thenReturn(mockBundle);
        assertThat(newFragment, is(mRegistrationPagerAdapter.getItem(2)));
        verify(mockBundle).putInt("page", 2);
        verify(newFragment).setArguments(mockBundle);

    }

    @Test
    public void testGetCount() {
        assertThat(4, is(mRegistrationPagerAdapter.getCount()));
    }
}
