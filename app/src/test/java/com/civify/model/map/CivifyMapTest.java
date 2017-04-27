package com.civify.model.map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.Nullable;

import com.civify.RobolectricTest;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.service.issue.ListIssuesSimpleCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({CivifyMap.class,
                 GoogleMap.class,
                 UiSettings.class,
                 CameraUpdateFactory.class,
                 Marker.class})
public class CivifyMapTest extends RobolectricTest {

    private static final String ISSUE_MOCK_ID = "abc123";
    private Activity mContext;
    private IssueAdapter mIssueAdapter;
    private Location mFakeLocation;
    private LocationAdapter mLocationAdapter;
    private CivifyMap mMap;
    private GoogleMap mGoogleMap;

    @Before
    public void setUp() throws Exception {
        mContext = mock(Activity.class);
        mIssueAdapter = mock(IssueAdapter.class);
        initFakeLocation();
        initLocationAdapterMock();
        initCivifyMapSpy();
        initIssueAdapterMock(null);
        setGoogleMap();
        mockStatics();
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
        verify(mMap, atLeastOnce()).setMapFragment(mapFragment);

        when(mMap.isMapReady()).thenReturn(false);
        SupportMapFragment mapFragment2 = mMap.getMapFragment();
        assertThat(mapFragment2, is(not(sameInstance(mapFragment))));

        when(mMap.isMapReady()).thenReturn(true);
        assertThat(mMap.getMapFragment(), is(sameInstance(mapFragment2)));

        CivifyMarkers markers = mMap.getMarkers();
        mMap.outdateToBeRefreshed();
        assertThat(mMap.getMapFragment(), is(not(sameInstance(mapFragment2))));
        assertThat(mMap.getMarkers(), is(sameInstance(markers)));
        verify(mMap, never()).refreshIssues();
    }

    @Test
    public void testGetMapAsyncCalled() {
        SupportMapFragment mapFragment = mock(SupportMapFragment.class);
        mMap.setMapFragment(mapFragment);
        verify(mapFragment).getMapAsync(mMap);
    }

    @Test
    public void testOnMapReadySettings() throws SecurityException {
        final Issue mockIssue = getIssueMock();
        initIssueAdapterMock(new ArrayList<Issue>() {{ add(mockIssue); }});
        setMarkerMock();
        mMap.onMapReady(mGoogleMap);
        assertThat(mMap.getGoogleMap(), is(sameInstance(mGoogleMap)));
        verify(mGoogleMap, atLeastOnce()).getUiSettings();
        verify(mLocationAdapter).setOnUpdateLocationListener(mMap);
        assertThat(mMap.getMarkers(), is(not(nullValue())));
        verify(mMap, atLeastOnce()).refreshIssues();
        assertThat(mMap.getMarkers().get(ISSUE_MOCK_ID).getIssue(), is(mockIssue));
        verify(mMap, atLeastOnce()).enableGoogleMyLocation();
        verify(mGoogleMap).setMyLocationEnabled(true);
        verify(mGoogleMap).setOnMarkerClickListener(mMap.getMarkers());
    }

    @Test
    public void testMapEnabled() throws MapNotReadyException {
        mMap.getMapFragment();
        mMap.enable();
        mMap.onMapReady(mGoogleMap);
        assertThat(mMap.isMapReady(), is(false)); // needs location update too

        LatLng expectedLocation = LocationAdapter.getLatLng(mFakeLocation);
        setCameraPosition(LocationAdapter.ZERO);
        assertThat(mGoogleMap.getCameraPosition().target, is(not(expectedLocation)));

        mMap.onUpdateLocation(mFakeLocation);
        assertThat(mMap.getCurrentLocation(), is(mFakeLocation));
        assertThat(mMap.isPlayerSet(), is(true));
        assertThat(mMap.isMapReady(), is(true));
        verify(mMap, atLeastOnce()).center();
        assertThat(LocationAdapter.getLatLng(mMap.getCurrentCameraPosition()),
                is(mGoogleMap.getCameraPosition().target));
        assertThat(mGoogleMap.getCameraPosition().target, is(expectedLocation));
    }

    @Test
    public void testMapResume() {
        when(mMap.isMapReady()).thenReturn(true);
        when(mLocationAdapter.isRequestingPermissions()).thenReturn(false);
        mMap.disable();
        verify(mLocationAdapter).disconnect();
        mMap.enable();
        verify(mLocationAdapter).connect();
        when(mLocationAdapter.isRequestingPermissions()).thenReturn(true);
        mMap.disable();
        verify(mLocationAdapter, atMost(1)).disconnect(); // only before
    }

    @Test
    public void testIssueMarkerAdded() throws MapNotReadyException {
        mMap.onMapReady(mGoogleMap);
        when(mMap.isMapReady()).thenReturn(true);
        Issue issueMock = getIssueMock();
        Marker markerMock = setMarkerMock();
        mMap.addIssueMarker(issueMock);
        CivifyMarker<?> marker = mMap.getMarkers().get(ISSUE_MOCK_ID.toUpperCase());
        assertThat(marker, is(instanceOf(IssueMarker.class)));
        assertThat(marker.getIssue(), is(sameInstance(issueMock)));
        assertThat(mMap.getMarkers().onMarkerClick(markerMock), is(true));
    }

    @Test(expected = MapNotReadyException.class)
    public void testCenterThrowsExceptionIfMapIsNotReady() throws MapNotReadyException {
        assertThat(mMap.isMapReady(), is(false));
        mMap.center();
    }

    private void initFakeLocation() {
        mFakeLocation = LocationAdapter.getLocation(new LatLng(42.87, 8.9));
        assertThat(mFakeLocation.getLatitude(), is(42.87));
        assertThat(mFakeLocation.getLongitude(), is(8.9));
    }

    private void initLocationAdapterMock() {
        mLocationAdapter = mock(LocationAdapter.class);
        when(mLocationAdapter.getContext()).thenReturn(mContext);
        when(mLocationAdapter.getLastLocation()).thenReturn(mFakeLocation);
    }

    private void initCivifyMapSpy() throws Exception {
        mMap = spy(new CivifyMap(mLocationAdapter, mIssueAdapter));
    }

    private void setGoogleMap() {
        mGoogleMap = mock(GoogleMap.class);
        when(mGoogleMap.getUiSettings()).thenReturn(mock(UiSettings.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                setCameraPosition(LocationAdapter.getLatLng(mFakeLocation));
                return null;
            }
        }).when(mGoogleMap).animateCamera(any(CameraUpdate.class));
    }

    private void initIssueAdapterMock(@Nullable final Collection<Issue> extraIssues) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ArrayList<Issue> issues = new ArrayList<>();
                if (extraIssues != null) issues.addAll(extraIssues);
                invocation.getArgumentAt(0, ListIssuesSimpleCallback.class).onSuccess(issues);
                return null;
            }
        }).when(mIssueAdapter).getIssues(any(ListIssuesSimpleCallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(mLocationAdapter).setOnPermissionsRequestedListener(any(Runnable.class));
    }

    private Marker setMarkerMock() {
        final Marker markerMock = mock(Marker.class);
        when(mGoogleMap.addMarker(any(MarkerOptions.class))).thenReturn(markerMock);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                when(markerMock.getTag()).thenReturn(ISSUE_MOCK_ID);
                return null;
            }
        }).when(markerMock).setTag(ISSUE_MOCK_ID);
        return markerMock;
    }

    private Issue getIssueMock() {
        Issue mock = mock(Issue.class);
        when(mock.getIssueAuthToken()).thenReturn(ISSUE_MOCK_ID);
        return mock;
    }

    private void setCameraPosition(LatLng location) {
        when(mGoogleMap.getCameraPosition()).thenReturn(CameraPosition.fromLatLngZoom(location, 0));
    }

    private static void mockStatics() {
        mockStatic(CameraUpdateFactory.class);
    }
}
