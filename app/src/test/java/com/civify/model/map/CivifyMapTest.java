package com.civify.model.map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import android.app.Activity;

import com.civify.adapter.LocationAdapter;
import com.google.android.gms.maps.SupportMapFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

@PrepareForTest(CivifyMap.class)
@RunWith(RobolectricTestRunner.class)
public class CivifyMapTest {

    @Mock
    private Activity mContext;
    @Mock
    private LocationAdapter mLocationAdapter;
    private CivifyMap mMap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initLocationAdapterMock();
        initCivifyMapSpy();
    }

    @Test
    public void testContext() {
        assertThat(mMap.getContext(), is(equalTo(mContext)));
    }

    @Test
    public void testGetMapFragment() {
        assertThat(mMap.isMapFragmentSet(), is(false));

        SupportMapFragment mapFragment = mMap.getMapFragment();
        assertThat(mapFragment, is(not(nullValue())));
        assertThat(mMap.isMapFragmentSet(), is(true));
        verify(mMap).setMapFragment(mapFragment);

        when(mMap.isMapReady()).thenReturn(false);
        SupportMapFragment mapFragment2 = mMap.getMapFragment();
        assertThat(mapFragment2, is(not(sameInstance(mapFragment))));

        when(mMap.isMapReady()).thenReturn(true);
        assertThat(mMap.getMapFragment(), is(sameInstance(mapFragment2)));
    }

    @Test
    public void testGetMapAsyncCalled() {
        SupportMapFragment mapFragment = mock(SupportMapFragment.class);
        mMap.setMapFragment(mapFragment);
        verify(mapFragment).getMapAsync(mMap);
    }

    private void initCivifyMapSpy() throws Exception {
        mMap = spy(new CivifyMap(mLocationAdapter));
    }

    private void initLocationAdapterMock() {
        when(mLocationAdapter.getContext()).thenReturn(mContext);
    }
}
