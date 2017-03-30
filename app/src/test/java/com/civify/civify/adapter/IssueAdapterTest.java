package com.civify.civify.adapter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.civify.model.Issue;
import com.civify.civify.model.Issue.Category;
import com.civify.civify.model.Picture;
import com.civify.civify.service.IssueService;
import com.civify.civify.service.IssueSimpleCallback;
import com.civify.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueAdapterTest {
    private IssueAdapter mIssueAdapter;
    private MockWebServer mMockWebServer;
    private SharedPreferences mSharedPreferences;
    private Gson mGson;
    private Issue mIssue;

    @Before
    public void setUp() throws IOException, ParseException {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        IssueService issueService = retrofit.create(IssueService.class);
        mSharedPreferences = mock(SharedPreferences.class);
        mIssueAdapter = new IssueAdapter(issueService, mSharedPreferences);
        setUpIssue();
    }

    @After
    public void tearDown() throws IOException {
        mIssueAdapter = null;
        mMockWebServer.shutdown();
    }

    @Test
    public void testValidCreateIssue() {
        String jsonBody = mGson.toJson(mIssue);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.createIssue(mIssue, mockCallback);

        ArgumentCaptor<Issue> argumentCaptor = ArgumentCaptor.forClass(Issue.class);
        verify(mockCallback).onSuccess(argumentCaptor.capture());
        assertEquals(mIssue.getTitle(), argumentCaptor.getValue().getTitle());
    }

    private void setUpIssue() throws ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture = new Picture("picture-file-name", "picture-content-type",
                1234, date, "picture-small-url",
                "picture-med-url", "picture-large-url");
        mIssue = new Issue("issue-title", "issue-description", Category.LIGHT_SIGNALS, true,
                45.0f, 46.0f, picture, "user-auth-token");
    }
}
