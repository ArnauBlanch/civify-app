package com.civify.activity.registration;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.ValidationCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions", "CanBeFinal" }) public class EmailTextWatcherTest {
    private EmailTextWatcher mEmailTextWatcher;
    @Mock
    private Context mContext;
    @Mock
    private UserAdapter mUserAdapter;
    @Captor
    private ArgumentCaptor<ValidationCallback> mCallbackArgCaptor;
    @Mock
    private ImageView mIcon;
    @Mock
    private TextView mText;
    @Mock
    private Resources mResources;
    @Mock
    private View mView;

    @Before
    public void setUp() {
        int iconRes = 0;
        int textRes = 1;
        MockitoAnnotations.initMocks(this);

        when(mView.getContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mResources);
        when(mView.findViewById(iconRes)).thenReturn(mIcon);
        when(mView.findViewById(textRes)).thenReturn(mText);

        mEmailTextWatcher = new EmailTextWatcher(mUserAdapter, mView, iconRes, textRes);
    }

    @After
    public void tearDown() {
        mEmailTextWatcher = null;
    }

    @Test
    public void testValidUnusedEmail() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("validunused@email.com");
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.valid_unused_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedEmail(anyString(),
                mCallbackArgCaptor.capture());

        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.VALID_UNUSED);

        verify(mIcon).setImageResource(R.drawable.ic_checked);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testInvalidEmail() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("invalidemail.com");
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.invalid_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedEmail(anyString(),
                mCallbackArgCaptor.capture());
        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.INVALID);

        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testUsedEmail() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("used@email.com");
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.used_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedEmail(anyString(),
                mCallbackArgCaptor.capture());
        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.USED);

        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testBeforeTextChanged() {
        mEmailTextWatcher
                .beforeTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testOnTextChanged() {
        mEmailTextWatcher
                .onTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testDefaultConstructor() {
        mEmailTextWatcher = new EmailTextWatcher(mock(View.class), 0, 0);
    }
}
