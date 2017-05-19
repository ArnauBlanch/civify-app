package com.civify.model.map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.Nullable;

import com.civify.RobolectricTest;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.issue.IssueDetailsFragment;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Category;
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
import org.robolectric.Robolectric;

@PrepareForTest({CivifyMap.class,
                 GoogleMap.class,
                 UiSettings.class,
                 CameraUpdateFactory.class,
                 BitmapFactory.class,
                 IssueDetailsFragment.class,
                 Marker.class})
public class CivifyMapTest extends RobolectricTest {

    private static final String ISSUE_MOCK_ID = "abc123";
    private DrawerActivity mContext;
    private IssueAdapter mIssueAdapter;
    private Location mFakeLocation;
    private LocationAdapter mLocationAdapter;
    private CivifyMap mMap;
    private GoogleMap mGoogleMap;

    @Before
    public void setUp() throws Exception {
        mockStatics();
        initContext();
        mIssueAdapter = mock(IssueAdapter.class);
        initFakeLocation();
        initLocationAdapterMock();
        initCivifyMapSpy();
        initIssueAdapterMock(null);
        setGoogleMapMock();
    }

    @Test
    public void testContext() {
        assertThat(mMap.getContext(), is(sameInstance(mContext)));
    }

    @Test
    public void testGetMapFragment() throws MapNotLoadedException {
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
    public void testOnMapReadySettings() throws SecurityException, MapNotLoadedException {
        mMap.onMapReady(mGoogleMap);
        verify(mGoogleMap, atLeastOnce()).getUiSettings();
        verify(mLocationAdapter).setOnUpdateLocationListener(mMap);
        verify(mGoogleMap).setMyLocationEnabled(true);
        verify(mGoogleMap).setOnMarkerClickListener(mMap.getMarkers());
        verify(mMap, atLeastOnce()).enableGoogleMyLocation();
        assertThat(mMap.getGoogleMap(), is(sameInstance(mGoogleMap)));
        assertThat(mMap.isMapLoaded(), is(true));
    }

    @Test
    public void testMapEnabled() throws MapNotReadyException {
        mMap.getMapFragment();
        mMap.enable();
        assertThat(mMap.isMapLoaded(), is(false));
        mMap.onMapReady(mGoogleMap);
        assertThat(mMap.isMapLoaded(), is(true));
        assertThat(mMap.isMapReady(), is(false)); // needs location update too

        LatLng expectedLocation = LocationAdapter.getLatLng(mFakeLocation);
        setCameraPosition(LocationAdapter.ZERO);
        assertThat(mGoogleMap.getCameraPosition().target, is(not(expectedLocation)));

        mMap.onUpdateLocation(mFakeLocation);
        assertThat(mMap.getCurrentLocation(), is(mFakeLocation));
        assertThat(mMap.isPlayerSet(), is(true));
        assertThat(mMap.isMapReady(), is(true));
        verify(mMap, atLeastOnce()).center(false);
        assertThat(mMap.getCurrentCameraPosition(), is(mGoogleMap.getCameraPosition().target));
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
    public void testMapRefresh() throws MapNotLoadedException {
        final Issue issueMock = getIssueMock();
        initIssueAdapterMock(new ArrayList<Issue>() {{ add(issueMock); }});
        Marker markerMock = getMarkerMock();
        mMap.onMapReady(mGoogleMap);
        verify(mMap, atLeastOnce()).refreshIssues();
        IssueMarker issueMarker = mMap.getMarkers().get(ISSUE_MOCK_ID);
        assertThat(issueMarker.getAttachedMap(), is(sameInstance(mGoogleMap)));
        assertThat(issueMarker, is(not(nullValue())));
        assertThat(mMap.getMarkers().isEmpty(), is(false));
        assertThat(mMap.getMarkers().size(), is(1));
        assertThat(issueMarker.getIssue(), is(sameInstance(issueMock)));
        assertThat(issueMarker.getPosition(), is(markerMock.getPosition()));
        assertThat(issueMarker.isPresent(), is(true));
        assertThat(issueMarker.isVisible(), is(true));
        assertThat(markerMock.isVisible(), is(true));

        GoogleMap oldMap = mGoogleMap;
        setGoogleMapMock();
        Marker newMarkerMock = getMarkerMock();
        assertThat(mGoogleMap, is(not(sameInstance(oldMap))));
        mMap.onMapReady(mGoogleMap);
        assertThat(issueMarker.getAttachedMap(), is(sameInstance(mGoogleMap)));
        assertThat(issueMarker.getPosition(), is(newMarkerMock.getPosition()));
        assertThat(issueMarker.isPresent(), is(true));
        assertThat(newMarkerMock.isVisible(), is(true));
        assertThat(issueMarker.isVisible(), is(true));
        assertThat(markerMock.isVisible(), is(false));

        initIssueAdapterMock(null); // simulates issue deletion
        mMap.refreshIssues();
        assertThat(mMap.getMarkers().isEmpty(), is(true));
        assertThat(mMap.getMarkers().size(), is(0));
        assertThat(mMap.getMarkers().get(ISSUE_MOCK_ID), is(nullValue()));
        assertThat(issueMarker.getAttachedMap(), is(nullValue()));
        assertThat(issueMarker.isVisible(), is(false));
        assertThat(issueMarker.isPresent(), is(false));
    }

    @Test
    public void testIssueMarkerAdded() throws MapNotLoadedException {
        Issue issueMock = getIssueMock();
        Marker markerMock = getMarkerMock();
        assertThat(mMap.isMapLoaded(), is(false));
        assertThat(mMap.getMarkers(), is(nullValue()));
        try {
            mMap.addIssueMarker(issueMock);
            fail("Trying to add issue when map is not present!");
        } catch (MapNotLoadedException ignore) {}
        mMap.onMapReady(mGoogleMap);
        when(mMap.isMapReady()).thenReturn(true);
        mMap.addIssueMarker(issueMock);
        assertThat(mMap.isMapLoaded(), is(true));
        assertThat(mMap.getMarkers(), is(not(nullValue())));
        assertThat(mMap.getMarkers().onMarkerClick(markerMock), is(true));
        IssueMarker marker = mMap.getMarkers().get(ISSUE_MOCK_ID.toUpperCase());
        assertThat(marker, is(instanceOf(IssueMarker.class)));
        assertThat(marker.getIssue(), is(sameInstance(issueMock)));
        assertThat(marker.getPosition(), is(markerMock.getPosition()));
        assertThat(marker.isPresent(), is(true));
        assertThat(markerMock.isVisible(), is(true));
    }

    @Test(expected = MapNotReadyException.class)
    public void testCenterThrowsExceptionIfMapIsNotReady() throws MapNotReadyException {
        assertThat(mMap.isMapReady(), is(false));
        mMap.center(true);
    }

    private void initFakeLocation() {
        mFakeLocation = LocationAdapter.getLocation(new LatLng(37.42, -122.084));
        assertThat(mFakeLocation.getLatitude(), is(37.42));
        assertThat(mFakeLocation.getLongitude(), is(-122.084));
    }

    private void initLocationAdapterMock() {
        mLocationAdapter = mock(LocationAdapter.class);
        when(mLocationAdapter.getContext()).thenReturn(mContext);
        when(mLocationAdapter.getLastLocation()).thenReturn(mFakeLocation);
    }

    private void initCivifyMapSpy() throws Exception {
        mMap = spy(new CivifyMap(mLocationAdapter, mIssueAdapter));
    }

    private void setGoogleMapMock() {
        mGoogleMap = mock(GoogleMap.class);
        when(mGoogleMap.getUiSettings()).thenReturn(mock(UiSettings.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                setCameraPosition(LocationAdapter.getLatLng(mFakeLocation));
                return null;
            }
        }).when(mGoogleMap).animateCamera(any(CameraUpdate.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                setCameraPosition(LocationAdapter.getLatLng(mFakeLocation));
                return null;
            }
        }).when(mGoogleMap).moveCamera(any(CameraUpdate.class));
    }

    private void initIssueAdapterMock(@Nullable final Collection<Issue> extraIssues) {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ArrayList<Issue> issues = new ArrayList<>();
                if (extraIssues != null) issues.addAll(extraIssues);
                invocation.getArgumentAt(0, ListIssuesSimpleCallback.class).onSuccess(issues);
                return null;
            }
        }).when(mIssueAdapter).getIssues(any(ListIssuesSimpleCallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ((Runnable)invocation.getArguments()[0]).run();
                return null;
            }
        }).when(mLocationAdapter).setOnPermissionsRequestedListener(any(Runnable.class));
    }

    private Marker getMarkerMock() {
        final Marker markerMock = mock(Marker.class);
        final Answer<Void> onMarkerRemove = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                when(markerMock.isVisible()).thenReturn(false);
                return null;
            }
        };
        final Answer<Void> onMarkerSetPosition = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                when(markerMock.getPosition()).thenReturn(LocationAdapter.getLatLng(mFakeLocation));
                return null;
            }
        };
        when(mGoogleMap.addMarker(any(MarkerOptions.class))).then(new Answer<Marker>() {
            @Override
            public Marker answer(InvocationOnMock invocation) {
                when(markerMock.isVisible()).thenReturn(true);
                doAnswer(onMarkerRemove).when(markerMock).remove();
                doAnswer(onMarkerSetPosition)
                        .when(markerMock).setPosition(eq(LocationAdapter.getLatLng(mFakeLocation)));
                return markerMock;
            }
        });
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                when(markerMock.getTag()).thenReturn(ISSUE_MOCK_ID);
                return null;
            }
        }).when(markerMock).setTag(ISSUE_MOCK_ID);
        return markerMock;
    }

    private Issue getIssueMock() {
        Issue mock = mock(Issue.class);
        when(mock.getIssueAuthToken()).thenReturn(ISSUE_MOCK_ID);
        when(mock.getLatitude()).thenReturn(mFakeLocation.getLatitude());
        when(mock.getLongitude()).thenReturn(mFakeLocation.getLongitude());
        when(mock.getCategory()).thenReturn(Category.ILLUMINATION);
        return mock;
    }

    private void setCameraPosition(LatLng location) {
        when(mGoogleMap.getCameraPosition()).thenReturn(CameraPosition.fromLatLngZoom(location, 0));
    }

    private static void mockStatics() {
        mockStatic(CameraUpdateFactory.class);
        mockStatic(BitmapFactory.class);
        mockStatic(IssueDetailsFragment.class);
    }

    private void initContext() {
        mContext = Robolectric.buildActivity(DrawerActivity.class).get();
    }
}
