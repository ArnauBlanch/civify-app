package com.civify.civify.adapter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.adapter.IssueAdapter;
import com.civify.model.Category;
import com.civify.model.Issue;
import com.civify.model.Picture;
import com.civify.service.IssueService;
import com.civify.service.IssueSimpleCallback;
import com.civify.service.ListIssuesSimpleCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueAdapterTest {
    private IssueAdapter mIssueAdapter;
    private MockWebServer mMockWebServer;
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
        Retrofit retrofit = (new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson))).build();
        IssueService issueService = retrofit.create(IssueService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mIssueAdapter = new IssueAdapter(issueService, sharedPreferences);
        setUpIssue();
    }

    @After
    public void tearDown() throws IOException {
        mIssueAdapter = null;
        mMockWebServer.shutdown();
    }

    @Test
    public void testValidCreateIssue() throws InterruptedException {
        String jsonBody = mGson.toJson(mIssue);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.createIssue(mIssue, mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();
        String json = request.getBody().readUtf8();
        JsonObject requestJson = new JsonParser().parse(json).getAsJsonObject();

        // Test mockCallback.onSuccess() is called
        ArgumentCaptor<Issue> argument = forClass(Issue.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        // Test request
        assertEquals("POST", request.getMethod());
        assertEquals("/users/" + mIssue.getUserAuthToken() + "/issues", request.getPath());
        assertEquals("application/json; charset=UTF-8", request.getHeader("Content-Type"));

        // Test request body (issue)
        assertEquals(mIssue.getTitle(), requestJson.get("title").getAsString());
        assertEquals(mIssue.getDescription(), requestJson.get("description").getAsString());
        assertEquals(mIssue.getCategory().toString(), requestJson.get("category").getAsString());
        assertEquals(mIssue.getLongitude(), requestJson.get("longitude").getAsFloat());
        assertEquals(mIssue.getLatitude(), requestJson.get("latitude").getAsFloat());
        assertEquals(mIssue.isRisk(), requestJson.get("risk").getAsBoolean());
        assertEquals(mIssue.isResolved(), requestJson.get("resolved").getAsBoolean());
        assertEquals(mIssue.getConfirmVotes(), requestJson.get("confirm_votes").getAsInt());
        assertEquals(mIssue.getReports(), requestJson.get("reports").getAsInt());
        assertEquals(mIssue.getResolvedVotes(), requestJson.get("resolved_votes").getAsInt());
        assertEquals(mIssue.getUserAuthToken(), requestJson.get("user_auth_token").getAsString());

        // Test request body (picture)
        assertEquals(mIssue.getPicture().getContentType(), requestJson.get("picture")
                .getAsJsonObject().get("content_type").getAsString());
        assertEquals(mIssue.getPicture().getFileName(), requestJson.get("picture")
                .getAsJsonObject().get("file_name").getAsString());
        assertEquals(mIssue.getPicture().getContent(), requestJson.get("picture")
                .getAsJsonObject().get("content").getAsString());

        Issue responseIssue = argument.getValue();

        // Test response body (issue)
        assertEquals(mIssue.getTitle(), responseIssue.getTitle());
        assertEquals(mIssue.getDescription(), responseIssue.getDescription());
        assertEquals(mIssue.getCategory(), responseIssue.getCategory());
        assertEquals(mIssue.getLongitude(), responseIssue.getLongitude());
        assertEquals(mIssue.getLatitude(), responseIssue.getLatitude());
        assertEquals(mIssue.isRisk(), responseIssue.isRisk());
        assertEquals(mIssue.isResolved(), responseIssue.isResolved());
        assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
        assertEquals(mIssue.getReports(), responseIssue.getReports());
        assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());
        assertEquals(mIssue.getUserAuthToken(), responseIssue.getUserAuthToken());

        // Test response body (picture)
        assertEquals(mIssue.getPicture().getContentType(),
                responseIssue.getPicture().getContentType());
        assertEquals(mIssue.getPicture().getFileName(),
                responseIssue.getPicture().getFileName());
        assertEquals(mIssue.getPicture().getContent(),
                responseIssue.getPicture().getContent());
    }

    @Test
    public void testInvalidCreateIssue() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.ISSUE_NOT_CREATED);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.createIssue(mIssue, mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test
    public void testValidGetIssues() throws InterruptedException {
        List<Issue> issueList = new ArrayList<>();
        issueList.add(mIssue);
        issueList.add(mIssue);
        String jsonBody = mGson.toJson(issueList);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        ListIssuesSimpleCallback mockCallback = mock(ListIssuesSimpleCallback.class);

        mIssueAdapter.getIssues(mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        // Test request
        assertEquals("GET", request.getMethod());
        assertEquals("/issues", request.getPath());

        // Test mockCallback.onSuccess() is called;
        ArgumentCaptor<List<Issue>> argument = ArgumentCaptor.forClass((Class) List.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        // Test response body (issue list)
        for (Issue responseIssue : argument.getValue()) {
            // Test response body (issue)
            assertEquals(mIssue.getTitle(), responseIssue.getTitle());
            assertEquals(mIssue.getDescription(), responseIssue.getDescription());
            assertEquals(mIssue.getCategory(), responseIssue.getCategory());
            assertEquals(mIssue.getLongitude(), responseIssue.getLongitude());
            assertEquals(mIssue.getLatitude(), responseIssue.getLatitude());
            assertEquals(mIssue.isRisk(), responseIssue.isRisk());
            assertEquals(mIssue.isResolved(), responseIssue.isResolved());
            assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
            assertEquals(mIssue.getReports(), responseIssue.getReports());
            assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());
            assertEquals(mIssue.getUserAuthToken(), responseIssue.getUserAuthToken());

            // Test response body (picture)
            assertEquals(mIssue.getPicture().getContentType(),
                    responseIssue.getPicture().getContentType());
            assertEquals(mIssue.getPicture().getFileName(),
                    responseIssue.getPicture().getFileName());
            assertEquals(mIssue.getPicture().getContent(),
                    responseIssue.getPicture().getContent());
        }
    }

    @Test
    public void testInvalidGetIssues() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.NO_ISSUES_FOUND);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        ListIssuesSimpleCallback mockCallback = mock(ListIssuesSimpleCallback.class);

        mIssueAdapter.getIssues(mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test
    public void testValidGetIssue() throws InterruptedException {
        String jsonBody = mGson.toJson(mIssue);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.getIssue(mIssue.getIssueAuthToken(), mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        // Test request
        assertEquals("GET", request.getMethod());
        assertEquals("/issues/" + mIssue.getIssueAuthToken(), request.getPath());

        // Test mockCallback.onSuccess() is called;
        ArgumentCaptor<Issue> argument = ArgumentCaptor.forClass(Issue.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        Issue responseIssue = argument.getValue();

        // Test response body (issue)
        assertEquals(mIssue.getTitle(), responseIssue.getTitle());
        assertEquals(mIssue.getDescription(), responseIssue.getDescription());
        assertEquals(mIssue.getCategory(), responseIssue.getCategory());
        assertEquals(mIssue.getLongitude(), responseIssue.getLongitude());
        assertEquals(mIssue.getLatitude(), responseIssue.getLatitude());
        assertEquals(mIssue.isRisk(), responseIssue.isRisk());
        assertEquals(mIssue.isResolved(), responseIssue.isResolved());
        assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
        assertEquals(mIssue.getReports(), responseIssue.getReports());
        assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());
        assertEquals(mIssue.getUserAuthToken(), responseIssue.getUserAuthToken());

        // Test response body (picture)
        assertEquals(mIssue.getPicture().getContentType(),
                responseIssue.getPicture().getContentType());
        assertEquals(mIssue.getPicture().getFileName(),
                responseIssue.getPicture().getFileName());
        assertEquals(mIssue.getPicture().getContent(),
                responseIssue.getPicture().getContent());
    }

    @Test
    public void testInvalidGetIssue() {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.ISSUE_NOT_FOUND);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.getIssue(mIssue.getIssueAuthToken(), mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    private void setUpIssue() throws ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture = new Picture("picture-file-name", "picture-content-type",
                "picture-content", 1234, date, "picture-small-url", "picture-med-url",
                "picture-large-url");
        mIssue = new Issue("issue-title", "issue-description", Category.ROAD_SIGNS, true,
                45.0f, 46.0f, 0, 0, false, 0, date, date, "issue-auth-token", "user-auth-token",
                picture);
    }
}
