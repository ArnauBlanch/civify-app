package com.civify.civify.adapter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.civify.civify.model.User;
import com.civify.civify.service.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.net.HttpURLConnection;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({UserAdapter.class, UserService.class})
public class UserAdapterTest {
    private UserAdapter mUserAdapter;
    private MockWebServer mMockServer;
    @Captor
    private ArgumentCaptor<ValidationCallback> mCallbackArgCaptor;

    @Before
    public void setUp() throws Exception {
        mMockServer = new MockWebServer();
        mMockServer.start();
        Retrofit retrofit = (new Retrofit.Builder().baseUrl(mMockServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create())).build();
        UserService userService = retrofit.create(UserService.class);
        mUserAdapter = new UserAdapter(userService);
    }

    @After
    public void tearDown() throws Exception {
        mUserAdapter = null;
    }

    @Test
    public void registerNotExistingUser() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_CREATED);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody(body.toString()));
        SimpleCallback mockCallback = mock(SimpleCallback.class);

        mUserAdapter.registerUser(
                new User("username", "name", "surname", "email@email.com", "validPassw0rd",
                        "validPassw0rd"), mockCallback);

        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("username", requestJson.get("username").getAsString());
        assertEquals("name", requestJson.get("first_name").getAsString());
        assertEquals("surname", requestJson.get("last_name").getAsString());
        assertEquals("email@email.com", requestJson.get("email").getAsString());
        assertEquals("a59aea522d1b3a2d0d4b927e4f065909d10a6f8c289901f591644d885aff23ca",
                requestJson.get("password").getAsString());
        assertEquals("a59aea522d1b3a2d0d4b927e4f065909d10a6f8c289901f591644d885aff23ca",
                requestJson.get("password_confirmation").getAsString());
        verify(mockCallback, timeout(1000)).onSuccess();
    }

    @Test
    public void registerExistingUser() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_NOT_CREATED);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody(body.toString()));
        SimpleCallback mockCallback = mock(SimpleCallback.class);

        mUserAdapter.registerUser(
                new User("username", "name", "surname", "email@email.com", "validPassw0rd",
                        "validPassw0rd"), mockCallback);

        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("username", requestJson.get("username").getAsString());
        assertEquals("name", requestJson.get("first_name").getAsString());
        assertEquals("surname", requestJson.get("last_name").getAsString());
        assertEquals("email@email.com", requestJson.get("email").getAsString());
        assertEquals("a59aea522d1b3a2d0d4b927e4f065909d10a6f8c289901f591644d885aff23ca",
                requestJson.get("password").getAsString());
        assertEquals("a59aea522d1b3a2d0d4b927e4f065909d10a6f8c289901f591644d885aff23ca",
                requestJson.get("password_confirmation").getAsString());

        verify(mockCallback, timeout(1000)).onFailure();

    }

    @Test
    public void checkValidUnusedEmail() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_DOESNT_EXIST);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString()));
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedEmail("valid@email.com", mockCallback);

        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users/search", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("valid@email.com", requestJson.get("email").getAsString());

        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.VALID_UNUSED);
    }

    @Test
    public void checkInvalidEmail() {
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedEmail("invalidemail.com", mockCallback);

        assertEquals(0, mMockServer.getRequestCount());
        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.INVALID);
    }

    @Test
    public void checkUsedEmail() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_EXISTS);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(body.toString()));
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedEmail("used@email.com", mockCallback);

        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users/search", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("used@email.com", requestJson.get("email").getAsString());

        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.USED);
    }

    @Test
    public void checkValidUnusedUsername() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_DOESNT_EXIST);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString()));
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedUsername("validUsername", mockCallback);
        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users/search", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("validUsername", requestJson.get("username").getAsString());
        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.VALID_UNUSED);
    }

    @Test
    public void checkInvalidUsername() {
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedUsername(".invalidUsername", mockCallback);
        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.INVALID);
    }

    @Test
    public void checkUsedUsername() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", UserAdapter.USER_EXISTS);
        mMockServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(body.toString()));
        ValidationCallback mockCallback = mock(ValidationCallback.class);

        mUserAdapter.checkValidUnusedUsername("usedUsername", mockCallback);
        RecordedRequest request = mMockServer.takeRequest();
        String json = request.getUtf8Body();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();
        assertEquals("POST", request.getMethod());
        assertEquals("/users/search", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));
        assertEquals("usedUsername", requestJson.get("username").getAsString());
        verify(mockCallback, timeout(1000)).onValidationResponse(UserAdapter.USED);
    }

    @Test
    public void checkValidPassword() {
        assertEquals(true, mUserAdapter.checkValidPassword("validPassw0rd"));
    }

    @Test
    public void checkInvalidPassword() {
        assertEquals(false, mUserAdapter.checkValidPassword("invalidpassword"));
    }
}