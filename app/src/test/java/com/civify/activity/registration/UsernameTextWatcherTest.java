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

import com.civify.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.ValidationCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions", "CanBeFinal" }) public class UsernameTextWatcherTest {
    private UsernameTextWatcher mUsernameTextWatcher;
    @Mock
    private Context mContext;
    @Mock
    private UserAdapter mUserAdapter;
    @Mock
    private ImageView mIcon;
    @Mock
    private TextView mText;
    @Mock
    private Resources mResources;
    @Mock
    private View mView;
    @Captor
    private ArgumentCaptor<ValidationCallback> mCallbackArgCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        int iconRes = 0;
        int textRes = 1;

        when(mView.getContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mResources);
        when(mView.findViewById(iconRes)).thenReturn(mIcon);
        when(mView.findViewById(textRes)).thenReturn(mText);

        mUsernameTextWatcher = new UsernameTextWatcher(mUserAdapter, mView, iconRes, textRes);
    }

    @After
    public void tearDown() {
        mUsernameTextWatcher = null;
    }

    @Test
    public void testValidUnusedUsername() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("validUsername");
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.valid_unused_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedUsername(anyString(),
                mCallbackArgCaptor.capture());
        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.VALID_UNUSED);

        verify(mIcon).setImageResource(R.drawable.ic_checked);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testInvalidUsername() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("invalid.username");
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.invalid_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedUsername(anyString(),
                mCallbackArgCaptor.capture());
        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.INVALID);

        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testUsedUsername() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("usedUsername");
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.used_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);

        verify(mUserAdapter).checkValidUnusedUsername(anyString(),
                mCallbackArgCaptor.capture());
        mCallbackArgCaptor.getValue().onValidationResponse(UserAdapter.USED);

        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testBeforeTextChanged() {
        mUsernameTextWatcher
                .beforeTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testOnTextChanged() {
        mUsernameTextWatcher
                .onTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testDefaultConstructor() {
        mUsernameTextWatcher = new UsernameTextWatcher(mock(View.class), 0, 0);
    }
}
