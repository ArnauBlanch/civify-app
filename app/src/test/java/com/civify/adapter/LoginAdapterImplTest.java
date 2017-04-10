package com.civify.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.civify.model.User;
import com.civify.service.CivifyLoginService;
import com.civify.service.CivifyMeService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressLint("CommitPrefEdits") //Not using shared preferences with persistence
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({LoginAdapterImpl.class, CivifyMeService.class, CivifyLoginService.class})
public class LoginAdapterImplTest {

    private LoginAdapterImpl mLoginAdapter;
    private LoginFinishedCallback mLoginFinishedCallbackMock;
    private User mUser;
    private MockWebServer mMockServer;
    private SharedPreferences mSharedPreferences;
    @Captor private ArgumentCaptor<LoginError> mCallbackArgCaptor;
    @Captor private ArgumentCaptor<User> mUserCallbackArgCaptor;

    @Before
    public void setUp() throws IOException {
        mMockServer = new MockWebServer();
        mMockServer.start();
        Retrofit retrofit = (new Retrofit.Builder().baseUrl(mMockServer.url("").toString())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())).build();
        CivifyLoginService civifyLoginService = retrofit.create(CivifyLoginService.class);
        CivifyMeService civifyMeService = retrofit.create(CivifyMeService.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        mSharedPreferences = mock(SharedPreferences.class);
        mLoginFinishedCallbackMock = mock(LoginFinishedCallback.class);
        when(mSharedPreferences.edit()).thenReturn(editor);
        mUser = new User("username", "name", "surname",
                "email", "password", "password");
        mLoginAdapter =
                new LoginAdapterImpl(civifyLoginService, civifyMeService, mSharedPreferences);
    }

    @After
    public void tearDown() throws IOException {
        mLoginAdapter = null;
    }

    @Test
    public void loginSucceededWhenUserExistsWithEmail() throws InterruptedException {
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("auth_token", "faketoken");
        Gson gson = new Gson();
        String userObject = gson.toJson(mUser);
        when(mSharedPreferences.getString(anyString(),anyString()))
                .thenReturn("not empty");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(loginResponseBody.toString()));
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(userObject));
        mLoginAdapter.login("mUser@mail.com", "password", mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginSucceeded(mUserCallbackArgCaptor.capture());
        assertEquals(mUser.getEmail(), mUserCallbackArgCaptor.getValue().getEmail());
        assertEquals(mUser.getUsername(), UserAdapter.getCurrentUser().getUsername());
    }

    @Test
    public void loginSucceededOnLoginWhenUserExistsWithUsername() throws InterruptedException {
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("auth_token", "faketoken");
        Gson gson = new Gson();
        String userObject = gson.toJson(mUser);
        when(mSharedPreferences.getString(anyString(),anyString()))
                .thenReturn("not empty");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(loginResponseBody.toString()));
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(userObject));
        mLoginAdapter.login("username", "password", mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginSucceeded(mUserCallbackArgCaptor.capture());
        assertEquals(mUser.getSurname(), mUserCallbackArgCaptor.getValue().getSurname());
        assertEquals(mUser.getUsername(), UserAdapter.getCurrentUser().getUsername());
    }

    @Test
    public void loginFailedOnLoginWhenUserNotExistsUsernameOrEmail() {
        LoginError notExistsError = new LoginError(LoginError.ErrorType.USER_NOT_EXISTS,
                LoginAdapterImpl.USER_NOT_EXISTS_MESSAGE);
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("username", "non-existing-username");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(loginResponseBody.toString()));
        mLoginAdapter.login("mUser@email.com", "password", mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginFailed(mCallbackArgCaptor.capture());
        assertEquals(notExistsError.getType(), mCallbackArgCaptor.getValue().getType());
    }

    @Test
    public void loginSucceededOnIsLoggedWhenUserExistsAndLogged() {
        Gson gson = new Gson();
        String userObject = gson.toJson(mUser);
        when(mSharedPreferences.getString(anyString(),anyString()))
                .thenReturn("not empty");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(userObject));
        mLoginAdapter.isLogged(mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginSucceeded(mUserCallbackArgCaptor.capture());
        assertEquals(mUser.getEmail(), mUserCallbackArgCaptor.getValue().getEmail());
        assertEquals(mUser.getUsername(), UserAdapter.getCurrentUser().getUsername());
    }

    @Test
    public void loginFailedWhenNotAuthorized() {
        LoginError notExistsError = new LoginError(LoginError.ErrorType.INVALID_CREDENTIALS,
                LoginAdapterImpl.INVALID_CREDENTIALS_MESSAGE);
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("username", "invalid credentials");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setBody(loginResponseBody.toString()));
        mLoginAdapter.login("mUser@email.com", "password", mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginFailed(mCallbackArgCaptor.capture());
        assertEquals(notExistsError.getType(), mCallbackArgCaptor.getValue().getType());
    }

    @Test
    public void loginFailedWhenNeedsLoginEmtpyToken() {
        LoginError notExistsError = new LoginError(LoginError.ErrorType.NOT_LOGGED_IN,
                LoginAdapterImpl.NEEDS_LOGIN_MESSAGE);
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("message", "invalid token");
        when(mSharedPreferences.getString(anyString(),anyString()))
                .thenReturn("");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(loginResponseBody.toString()));
        mLoginAdapter.isLogged(mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginFailed(mCallbackArgCaptor.capture());
        assertEquals(notExistsError.getType(), mCallbackArgCaptor.getValue().getType());
    }

    @Test
    public void loginFailedWhenNeedsLoginBadToken() {
        LoginError notExistsError = new LoginError(LoginError.ErrorType.NOT_LOGGED_IN,
                LoginAdapterImpl.NEEDS_LOGIN_MESSAGE);
        JsonObject loginResponseBody = new JsonObject();
        loginResponseBody.addProperty("message", "invalid token");
        when(mSharedPreferences.getString(anyString(),anyString()))
                .thenReturn("badtoken");
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setBody(loginResponseBody.toString()));
        mLoginAdapter.isLogged(mLoginFinishedCallbackMock);
        verify(mLoginFinishedCallbackMock, timeout(1000))
                .onLoginFailed(mCallbackArgCaptor.capture());
        assertEquals(notExistsError.getType(), mCallbackArgCaptor.getValue().getType());
    }
}
