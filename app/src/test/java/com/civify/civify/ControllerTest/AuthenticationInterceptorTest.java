package com.civify.civify.ControllerTest;

import com.civify.civify.controller.AuthenticationInterceptor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static junit.framework.Assert.assertEquals;

public class AuthenticationInterceptorTest {

    private AuthenticationInterceptor authenticationInterceptor;
    private MockWebServer mockWebServer;
    @Before
    public void setUp() {
        authenticationInterceptor = new AuthenticationInterceptor("fakeToken");
        mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void shutDown() {
        authenticationInterceptor = null;
        try {
            mockWebServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mockWebServer = null;
    }

    @Test
    public void interceptionIntroducingAuthorizationAndAcceptHeadersCorrect() {

        mockWebServer.enqueue(new MockResponse());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(authenticationInterceptor).build();

        try {
            okHttpClient.newCall(new Request.Builder().url(mockWebServer.url("/")).build()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecordedRequest request = null;
        try {
            request = mockWebServer.takeRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("fakeToken", request.getHeader("Authorization"));
        assertEquals("application/json", request.getHeader("Accept"));

    }

}
