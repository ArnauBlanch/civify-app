package com.civify.civify.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.civify.civify.model.Issue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import retrofit2.Call;
import retrofit2.Response;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Response.class)
public class ResponseCallbackTest {
    private ResponseCallback<Issue> mResponseCallback;

    @Before
    public void setUp() {
        mResponseCallback = new ResponseCallback<>();
    }

    @After
    public void tearDown() {
        mResponseCallback = null;
    }


    @Test
    public void testOnResponse() {
        Response<Issue> mockResponse = PowerMockito.mock(Response.class);
        Call<Issue> mockCall = mock(Call.class);
        mResponseCallback.onResponse(mockCall, mockResponse);
        assertEquals(mockResponse, mResponseCallback.getResponse());
    }

    @Test
    public void testOnFailure() {
        Throwable mockThrowable = mock(Throwable.class);
        Call<Issue> mockCall = mock(Call.class);
        mResponseCallback.onFailure(mockCall, mockThrowable);
    }
}
