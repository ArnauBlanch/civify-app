package com.civify.civify.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.civify.civify.Controller.UserController;
import com.civify.civify.R;

public class RegistrationFragment extends Fragment {
  private View mView;
  private static final String PAGE = "page";
  private RegistrationActivity mActivity;

  private int mPage;

  public static RegistrationFragment newInstance(int page) {
    RegistrationFragment frag = new RegistrationFragment();
    Bundle b = new Bundle();
    b.putInt(PAGE, page);
    frag.setArguments(b);
    return frag;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!getArguments().containsKey(PAGE))
      throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
    mPage = getArguments().getInt(PAGE);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mActivity = (RegistrationActivity) getActivity();
    // Select a layout based on the current page
    int layoutResId;
    switch (mPage) {
      case 0:
        layoutResId = R.layout.registration_page_name;
        break;
      case 1:
        layoutResId = R.layout.registration_page_username;
        break;
      case 2:
        layoutResId = R.layout.registration_page_email;
        break;
      default:
        layoutResId = R.layout.registration_page_password;
    }

    // Inflate the layout resource file
    mView = mActivity.getLayoutInflater().inflate(layoutResId, container, false);

    // Set the current page index as the View's tag (useful in the PageTransformer)
    mView.setTag(mPage);

    Log.i("mPage", String.valueOf(mPage));
    switch (mPage) {
      case 0:
        ((Button)mView.findViewById(R.id.button0)).setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mActivity.checkNameSurname())
              mActivity.nextPage();
          }
        });
        break;
      case 1:
        setupUsernameListener();
        ((Button)mView.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mActivity.checkUsername() == UserController.VALID_UNUSED)
              mActivity.nextPage();
          }
        });
        break;
      case 2:
        setupEmailListener();
        ((Button)mView.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mActivity.checkEmail() == UserController.VALID_UNUSED)
              mActivity.nextPage();
          }
        });
        break;
      default:
        setupPasswordListener();
        setupPasswordMatchListener();
        ((Button)mView.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (mActivity.checkPassword() && mActivity.checkPasswordMatch())
              mActivity.register();
          }
        });
    }

    return mView;
  }

  public void setupUsernameListener() {
    EditText usernameEditText = (EditText)mView.findViewById(R.id.username_input);
    usernameEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override public void afterTextChanged(Editable s) {
        ((RegistrationActivity) getActivity()).usernameChecker();
      }
    });
  }

  public void setupEmailListener() {
    EditText emailEditText = (EditText)mView.findViewById(R.id.email_input);
    emailEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override public void afterTextChanged(Editable s) {
        ((RegistrationActivity)getActivity()).emailChecker();
      }
    });
  }

  public void setupPasswordListener() {
    EditText passwordEditText = (EditText)mView.findViewById(R.id.password_input);
    passwordEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override public void afterTextChanged(Editable s) {
        ((RegistrationActivity)getActivity()).passwordChecker();
      }
    });
  }

  public void setupPasswordMatchListener() {
    EditText passwordMatchEditText = (EditText)mView.findViewById(R.id.password2_input);
    passwordMatchEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
      @Override public void afterTextChanged(Editable s) {
        ((RegistrationActivity)getActivity()).passwordMatchChecker();
      }
    });
  }

}