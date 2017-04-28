package com.civify.adapter.issue;

public class IssueAdapterTest {

    /*private IssueAdapter mIssueAdapter;
    private MockWebServer mMockWebServer;
    private Gson mGson;
    private Issue mIssue;
    private User mUser;

    @Before public void setUp() throws IOException, java.text.ParseException {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
        IssueService issueService = retrofit.create(IssueService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mIssueAdapter = new IssueAdapter(issueService, sharedPreferences);
        setUpIssue();
    }

    @After public void tearDown() throws IOException {
        mIssueAdapter = null;
        mMockWebServer.shutdown();
    }

    @Test public void testValidCreateIssue() throws InterruptedException {
        String jsonBody = mGson.toJson(mIssue);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_CREATED)
                        .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        UserAdapter.setCurrentUser(mUser);

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
        assertEquals(mIssue.getCategory().toString().toLowerCase(),
                requestJson.get("category").getAsString());
        assertEquals(mIssue.getLongitude(), requestJson.get("longitude").getAsFloat());
        assertEquals(mIssue.getLatitude(), requestJson.get("latitude").getAsFloat());
        assertEquals(mIssue.isRisk(), requestJson.get("risk").getAsBoolean());

        // Test request body (picture)
        assertEquals(mIssue.getPicture().getContentType(),
                requestJson.get("picture").getAsJsonObject().get("content_type").getAsString());
        assertEquals(mIssue.getPicture().getFileName(),
                requestJson.get("picture").getAsJsonObject().get("file_name").getAsString());
        assertEquals(mIssue.getPicture().getContent(),
                requestJson.get("picture").getAsJsonObject().get("content").getAsString());

        Issue responseIssue = argument.getValue();

        // Test response body (issue)
        assertEquals(mIssue.getTitle(), responseIssue.getTitle());
        assertEquals(mIssue.getDescription(), responseIssue.getDescription());
        assertEquals(mIssue.getCategory(), responseIssue.getCategory());
        assertEquals(mIssue.getLongitude(), responseIssue.getLongitude());
        assertEquals(mIssue.getLatitude(), responseIssue.getLatitude());
        assertEquals(mIssue.isRisk(), responseIssue.isRisk());
        assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
        assertEquals(mIssue.getReports(), responseIssue.getReports());
        assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());

        // Test response body (picture)
        assertEquals(mIssue.getPicture().getContentType(),
                responseIssue.getPicture().getContentType());
        assertEquals(mIssue.getPicture().getFileName(), responseIssue.getPicture().getFileName());
    }

    @Test public void testInvalidCreateIssue() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.RECORD_DOES_NOT_EXIST);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                        .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        UserAdapter.setCurrentUser(mUser);

        mIssueAdapter.createIssue(mIssue, mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test public void testValidGetIssues() throws InterruptedException {
        List<Issue> issueList = new ArrayList<>();
        issueList.add(mIssue);
        issueList.add(mIssue);
        String jsonBody = mGson.toJson(issueList);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        ListIssuesSimpleCallback mockCallback = mock(ListIssuesSimpleCallback.class);

        mIssueAdapter.getIssues(mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        // Test request
        assertEquals("GET", request.getMethod());
        assertEquals("/issues", request.getPath());

        // Test mockCallback.onSuccess() is called;
        ArgumentCaptor<List<Issue>> argument = forClass((Class) List.class);
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
            assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
            assertEquals(mIssue.getReports(), responseIssue.getReports());
            assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());

            // Test response body (picture)
            assertEquals(mIssue.getPicture().getContentType(),
                    responseIssue.getPicture().getContentType());
            assertEquals(mIssue.getPicture().getFileName(),
                    responseIssue.getPicture().getFileName());
        }
    }

    @Test public void testInvalidGetIssues() throws InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.RECORD_DOES_NOT_EXIST);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                        .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        ListIssuesSimpleCallback mockCallback = mock(ListIssuesSimpleCallback.class);

        mIssueAdapter.getIssues(mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test public void testValidGetIssue() throws InterruptedException {
        String jsonBody = mGson.toJson(mIssue);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.getIssue(mIssue.getIssueAuthToken(), mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        // Test request
        assertEquals("GET", request.getMethod());
        assertEquals("/issues/" + mIssue.getIssueAuthToken(), request.getPath());

        // Test mockCallback.onSuccess() is called;
        ArgumentCaptor<Issue> argument = forClass(Issue.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        Issue responseIssue = argument.getValue();

        // Test response body (issue)
        assertEquals(mIssue.getTitle(), responseIssue.getTitle());
        assertEquals(mIssue.getDescription(), responseIssue.getDescription());
        assertEquals(mIssue.getCategory(), responseIssue.getCategory());
        assertEquals(mIssue.getLongitude(), responseIssue.getLongitude());
        assertEquals(mIssue.getLatitude(), responseIssue.getLatitude());
        assertEquals(mIssue.isRisk(), responseIssue.isRisk());
        assertEquals(mIssue.getConfirmVotes(), responseIssue.getConfirmVotes());
        assertEquals(mIssue.getReports(), responseIssue.getReports());
        assertEquals(mIssue.getResolvedVotes(), responseIssue.getResolvedVotes());

        // Test response body (picture)
        assertEquals(mIssue.getPicture().getContentType(),
                responseIssue.getPicture().getContentType());
        assertEquals(mIssue.getPicture().getFileName(), responseIssue.getPicture().getFileName());
    }

    @Test public void testInvalidGetIssue() {
        JsonObject body = new JsonObject();
        body.addProperty("message", IssueAdapter.RECORD_DOES_NOT_EXIST);
        MockResponse mockResponse =
                new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                        .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        IssueSimpleCallback mockCallback = mock(IssueSimpleCallback.class);

        mIssueAdapter.getIssue(mIssue.getIssueAuthToken(), mockCallback);

        // Test mockCallback.onFailure() is called
        verify(mockCallback, timeout(1000)).onFailure();
    }

    private void setUpIssue() throws java.text.ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat =
                new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale.getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture =
                new Picture("picture-file-name", "picture-content-type", "picture-content");
        mIssue = new Issue("issue-title", "issue-description", Category.ROAD_SIGNS, true, 45.0f,
                46.0f, 0, 0, 0, date, date, "issue-auth-token", "user-auth-token", picture);
        mUser = new User("username", "name", "surname", "email@email.com", "mypass", "mypass");
        mUser.setUserAuthToken("user-auth-token");
    }*/
}
