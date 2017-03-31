package com.civify.civify.activity.registration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.civify.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "deprecation", "JUnitTestMethodWithNoAssertions" }) public class PasswordMatchTextWatcherTest {
    private PasswordMatchTextWatcher mPasswordMatchTextWatcher;
    private Context mContext;
    private ImageView mIcon;
    private TextView mText;
    private Resources mResources;
    private EditText mPasswordInput;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        View view = mock(View.class);
        int iconRes = 0;
        int textRes = 1;
        int passwordRes = 2;
        mIcon = mock(ImageView.class);
        mText = mock(TextView.class);
        mResources = mock(Resources.class);
        mPasswordInput = mock(EditText.class);

        when(view.getContext()).thenReturn(mContext);
        when(mContext.getResources()).thenReturn(mResources);
        when(view.findViewById(iconRes)).thenReturn(mIcon);
        when(view.findViewById(textRes)).thenReturn(mText);
        when(view.findViewById(passwordRes)).thenReturn(mPasswordInput);

        mPasswordMatchTextWatcher = new PasswordMatchTextWatcher(view, iconRes,
                textRes, passwordRes);
    }

    @After
    public void tearDown() {
        mPasswordMatchTextWatcher = null;
    }

    @Test
    public void testMatchingPasswords() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        Editable mockEditable2 = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mPasswordInput.getText()).thenReturn(mockEditable);
        when(mockEditable.toString()).thenReturn("validPassw0rd");
        when(mockEditable2.toString()).thenReturn("validPassw0rd");
        when(mResources.getColor(R.color.green)).thenReturn(color);
        when(mContext.getString(R.string.matching_passwords)).thenReturn(text);

        mPasswordMatchTextWatcher.afterTextChanged(mockEditable2);
        verify(mIcon).setImageResource(R.drawable.ic_checked);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testNotMatchingPasswords() throws Resources.NotFoundException {
        Editable mockEditable = mock(Editable.class);
        Editable mockEditable2 = mock(Editable.class);
        int color = 50;
        String text = "message";

        when(mPasswordInput.getText()).thenReturn(mockEditable);
        when(mockEditable.toString()).thenReturn("validPassw0rd");
        when(mockEditable2.toString()).thenReturn("validpassword");
        when(mResources.getColor(R.color.red)).thenReturn(color);
        when(mContext.getString(R.string.not_matching_passwords)).thenReturn(text);

        mPasswordMatchTextWatcher.afterTextChanged(mockEditable2);
        verify(mIcon).setImageResource(R.drawable.ic_cancel);
        verify(mText).setText(text);
        verify(mText).setTextColor(color);
    }

    @Test
    public void testBeforeTextChanged() {
        mPasswordMatchTextWatcher
                .beforeTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testOnTextChanged() {
        mPasswordMatchTextWatcher
                .onTextChanged(mock(CharSequence.class), 0, 0, 0);
    }

    @Test
    public void testDefaultConstructor() {
        mPasswordMatchTextWatcher = new PasswordMatchTextWatcher(mock(View.class), 0, 0, 0);
    }
}
