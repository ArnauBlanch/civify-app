package com.civify.civify.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import com.civify.model.MessageResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageResponseTest {

    @Test public void setMessage() throws Exception {

    }

    private MessageResponse mMessageResponse;

    @Before public void setUp() throws Exception {
        mMessageResponse = new MessageResponse();
    }

    @After public void tearDown() throws Exception {
        mMessageResponse = null;
    }

    @Test public void testMessage() throws Exception {
        mMessageResponse.setMessage("Test message.");
        assertThat(mMessageResponse.getMessage(), is("Test message."));
    }
}