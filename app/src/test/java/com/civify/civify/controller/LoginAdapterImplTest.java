package com.civify.civify.controller;


import android.content.SharedPreferences;

import com.civify.civify.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.mock.Calls;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginAdapterImplTest {

    private MockWebServer mMockWebServer;
    @Mock private User user;
    @Mock private ServiceGenerator serviceGenerator;
    @Mock private SharedPreferences mSharedPreferences;
    @Mock private CivifyEmailCredentials mCivifyEmailCredentials;
    @Mock private CivifyUsernameCredentials mCivifyUsernameCredentials;
    @Mock private LoginFinishedCallback loginFinishedCallback;
    @InjectMocks private LoginAdapterImpl mLoginAdapterImpl;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();



        when(serviceGenerator.createService(CivifyMeService.class))
                .thenReturn(authToken -> null);
    }

    @Test
    public void userReturnedOnLoginUsingUsernameTokenNotStored() {
        when(mSharedPreferences.getString(LoginAdapterImpl.AUTH_TOKEN, "")).thenReturn("");
        when(serviceGenerator.createService(CivifyLoginService.class))
                .thenReturn(new CivifyLoginService() {
                    @Override
                    public Call<String> loginWithEmail(@Body CivifyEmailCredentials credentials) {
                        return Calls.response("");
                    }

                    @Override
                    public Call<String> loginWithUsername(@Body CivifyUsernameCredentials credentials) {
                        return null;
                    }
                });
        when(serviceGenerator.createService(CivifyMeService.class))
                .thenReturn(new CivifyMeService() {
                    @Override
                    public Call<User> getUser(@Header("Authorization") String authToken) {
                        return Calls.response(user);
                    }
                });

        mLoginAdapterImpl.login("username", "password", loginFinishedCallback);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(loginFinishedCallback).onLoginSucceeded(argumentCaptor.capture());
    }

    @Test
    public void userReturnedOnLoginUsingEmailTokenNotStored() {

    }

    @Test
    public void userReturnedOnLoginUsingUsernameAndValidTokenStored() {

    }

    @Test
    public void userReturnedOnLoginUsingEmailAndValidTokenStored() {

    }

    @Test
    public void loginErrorReturnedOnLoginWhenUserNotExists() {

    }

    @Test
    public void loginErrorReturnedOnLoginWhenInvalidCredentials() {

    }

    @Test
    public void isLoggedErrorReturnedOnIsLoggedWhenNotLoggedIn() {

    }

    @After
    public void tearDown() throws IOException {
        mMockWebServer.shutdown();

    }
}
