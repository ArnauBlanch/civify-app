package com.civify.activity.registration;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions" }) public class PasswordTextWatcherTest {
    private PasswordTextWatcher mPasswordTextWatcher;
    private Context mContext;
    private UserAdapter mUserAdapter;
    private ImageView mIcon;
    private TextView mText;
    private Resources mResources;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        View view = mock(View.class);
        int iconRes = 0;
        int textRes = 1;
        mUserAdapter = mock(UserAdapter.class);
        mIcon = mock(ImageView.class);
        mText = mock(TextView.class);
        mResources = mock(Resources.class);

        when(view.getContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mResources);
        when(view.findViewById(iconRes)).thenReturn(mIcon);
        when(view.findViewById(textRes)).thenReturn(mText);

        mPasswordTextWatcher = new PasswordTextWatcher(mUserAdapter, view, iconRes, textRes);
    }

    @After
    public void tearDown() {
        mPasswordTextWatcher = null;
    }

    @Test
    public void testValidPassword() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("validPassw0rd");
        when(mUserAdapter.checkValidPassword("validPassw0rd"))
                .thenReturn(true);
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.valid_password)).thenReturn(text);

        mPasswordTextWatcher.afterTextChanged(mockEditable);
        verify(mIcon).setImageResource(R.drawable.ic_checked);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testInvalidPassword() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mockEditable.toString()).thenReturn("invalidpassword");
        when(mUserAdapter.checkValidPassword("invalidpassword"))
                .thenReturn(false);
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.invalid_password)).thenReturn(text);

        mPasswordTextWatcher.afterTextChanged(mockEditable);
        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testBeforeTextChanged() {
        mPasswordTextWatcher
                .beforeTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testOnTextChanged() {
        mPasswordTextWatcher
                .onTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testDefaultConstructor() {
        mPasswordTextWatcher = new PasswordTextWatcher(mock(View.class), 0, 0);
    }
}
