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

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions" }) public class EmailTextWatcherTest {
    private EmailTextWatcher mEmailTextWatcher;
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
        //noinspection ResourceType
        when(view.findViewById(textRes)).thenReturn(mText);

        mEmailTextWatcher = new EmailTextWatcher(mUserAdapter, view, iconRes, textRes);
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
        when(mUserAdapter.checkValidUnusedEmail("validunused@email.com"))
                .thenReturn(UserAdapter.VALID_UNUSED);
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.valid_unused_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);
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
        when(mUserAdapter.checkValidUnusedEmail("invalidemail.com"))
                .thenReturn(UserAdapter.INVALID);
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.invalid_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);
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
        when(mUserAdapter.checkValidUnusedEmail("used@email.com"))
                .thenReturn(UserAdapter.USED);
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.used_email)).thenReturn(text);

        mEmailTextWatcher.afterTextChanged(mockEditable);
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
