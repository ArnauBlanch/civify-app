package com.civify.civify.activity.registration;

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
import com.civify.civify.adapter.UserAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions" }) public class UsernameTextWatcherTest {
    private UsernameTextWatcher mUsernameTextWatcher;
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

        mUsernameTextWatcher = new UsernameTextWatcher(mUserAdapter, view, iconRes, textRes);
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
        when(mUserAdapter.checkValidUnusedUsername("validUsername"))
                .thenReturn(UserAdapter.VALID_UNUSED);
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.valid_unused_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);
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
        when(mUserAdapter.checkValidUnusedEmail("invalid.username"))
                .thenReturn(UserAdapter.INVALID);
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.invalid_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);
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
        when(mUserAdapter.checkValidUnusedUsername("usedUsername"))
                .thenReturn(UserAdapter.USED);
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.used_username)).thenReturn(text);

        mUsernameTextWatcher.afterTextChanged(mockEditable);
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
