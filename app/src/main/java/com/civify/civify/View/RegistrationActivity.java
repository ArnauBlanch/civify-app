package com.civify.civify.View;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.civify.civify.Controller.UserController;
import com.civify.civify.Model.UserService;
import com.civify.civify.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationActivity extends AppCompatActivity {
  private UserController mUserController = new UserController(new UserService());
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Nunito-Regular.ttf").setFontAttrId(R.attr.fontPath).build()
    );
    setContentView(R.layout.registration_layout);

    mViewPager = (ViewPager) findViewById(R.id.viewpager);

    // Set an Adapter on the ViewPager
    mViewPager.setAdapter(new RegistrationAdapter(getSupportFragmentManager()));
    mViewPager.setCurrentItem(0);
    mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount()-1);
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void nextPage() {
    if (mViewPager.getCurrentItem() < mViewPager.getAdapter().getCount() - 1) {
      mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }
  }

  public void previousPage() {
    if (mViewPager.getCurrentItem() > 0) {
      mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }
  }

  @Override
  public void onBackPressed() {
    if (mViewPager.getCurrentItem() == 0)
      super.onBackPressed();
    else
      previousPage();
  }

  public boolean checkNameSurname() {
    EditText nameEditText = (EditText)findViewById(R.id.name_input);
    EditText surnameEditText = (EditText)findViewById(R.id.surname_input);
    ImageView nameValidationIcon = (ImageView)findViewById(R.id.name_validation_icon);
    TextView nameValidationText = (TextView)findViewById(R.id.name_validation_text);
    ImageView surnameValidationIcon = (ImageView)findViewById(R.id.surname_validation_icon);
    TextView surnameValidationText = (TextView)findViewById(R.id.surname_validation_text);

    boolean nameOkay = (nameEditText.getText().toString().length() > 2);
    boolean surnameOkay = (surnameEditText.getText().toString().length() > 2);
    if (nameOkay) {
      nameValidationIcon.setVisibility(View.INVISIBLE);
      nameValidationText.setVisibility(View.INVISIBLE);
    } else {
      nameValidationIcon.setVisibility(View.VISIBLE);
      nameValidationText.setVisibility(View.VISIBLE);
      nameValidationIcon.setImageResource(R.drawable.ic_cancel);
      nameValidationText.setText(getString(R.string.enter_name));
      nameValidationText.setTextColor(getResources().getColor(R.color.red));
    }
    if (surnameOkay) {
      surnameValidationIcon.setVisibility(View.INVISIBLE);
      surnameValidationText.setVisibility(View.INVISIBLE);
    } else {
      surnameValidationIcon.setVisibility(View.VISIBLE);
      surnameValidationText.setVisibility(View.VISIBLE);
      surnameValidationIcon.setImageResource(R.drawable.ic_cancel);
      surnameValidationText.setText(getString(R.string.enter_surname));
      surnameValidationText.setTextColor(getResources().getColor(R.color.red));
    }
    return nameOkay && surnameOkay;
  }

  public void usernameChecker() {
    ImageView usernameValidationIcon = (ImageView)findViewById(R.id.username_validation_icon);
    TextView usernameValidationText = (TextView)findViewById(R.id.username_validation_text);

    switch (checkUsername()) {
      case UserController.VALID_UNUSED:
        usernameValidationIcon.setImageResource(R.drawable.ic_checked);
        usernameValidationText.setText(getString(R.string.valid_unused_username));
        usernameValidationText.setTextColor(getResources().getColor(R.color.green));
        break;
      case UserController.INVALID:
        usernameValidationIcon.setImageResource(R.drawable.ic_cancel);
        usernameValidationText.setText(getString(R.string.invalid_username));
        usernameValidationText.setTextColor(getResources().getColor(R.color.red));
        break;
      case UserController.USED:
        usernameValidationIcon.setImageResource(R.drawable.ic_cancel);
        usernameValidationText.setText(getString(R.string.used_username));
        usernameValidationText.setTextColor(getResources().getColor(R.color.red));
    }
  }

  public int checkUsername() {
    EditText usernameEditText = (EditText)findViewById(R.id.username_input);
    String username = usernameEditText.getText().toString();
    return mUserController.checkValidUnusedUsername(username);
  }

  public void register() {
    String username = ((EditText)findViewById(R.id.username_input)).getText().toString();
    String name = ((EditText)findViewById(R.id.name_input)).getText().toString();
    String surname = ((EditText)findViewById(R.id.surname_input)).getText().toString();
    String email = ((EditText)findViewById(R.id.email_input)).getText().toString();
    String password = ((EditText)findViewById(R.id.password_input)).getText().toString();
    try {
      mUserController.registerUser(username, name, surname, email, password);
    }
    catch (Exception e) {
      Snackbar.make(findViewById(R.id.registration_layout), e.getMessage(), Snackbar.LENGTH_SHORT).show();
    }
  }

  public void emailChecker() {
    ImageView emailValidationIcon = (ImageView)findViewById(R.id.email_validation_icon);
    TextView emailValidationText = (TextView)findViewById(R.id.email_validation_text);

    switch (checkEmail()) {
      case UserController.VALID_UNUSED:
        emailValidationIcon.setImageResource(R.drawable.ic_checked);
        emailValidationText.setText(getString(R.string.valid_unused_email));
        emailValidationText.setTextColor(getResources().getColor(R.color.green));
        break;
      case UserController.INVALID:
        emailValidationIcon.setImageResource(R.drawable.ic_cancel);
        emailValidationText.setText(getString(R.string.invalid_email));
        emailValidationText.setTextColor(getResources().getColor(R.color.red));
        break;
      case UserController.USED:
        emailValidationIcon.setImageResource(R.drawable.ic_cancel);
        emailValidationText.setText(getString(R.string.used_email));
        emailValidationText.setTextColor(getResources().getColor(R.color.red));
        break;
    }
  }

  public int checkEmail() {
    EditText emailEditText = (EditText)findViewById(R.id.email_input);
    String email = emailEditText.getText().toString();
    return mUserController.checkValidUnusedEmail(email);
  }

  public void passwordChecker() {
    ImageView passwordValidationIcon = (ImageView)findViewById(R.id.password_validation_icon);
    TextView passwordValidationText = (TextView)findViewById(R.id.password_validation_text);

    if (checkPassword()) {
      passwordValidationIcon.setImageResource(R.drawable.ic_checked);
      passwordValidationText.setText(getString(R.string.valid_password));
      passwordValidationText.setTextColor(getResources().getColor(R.color.green));
    } else {
      passwordValidationIcon.setImageResource(R.drawable.ic_cancel);
      passwordValidationText.setText(getString(R.string.invalid_password));
      passwordValidationText.setTextColor(getResources().getColor(R.color.red));
    }
  }

  public boolean checkPassword() {
    EditText passwordEditText = (EditText)findViewById(R.id.password_input);
    String password = passwordEditText.getText().toString();
    return mUserController.checkValidPassword(password);
  }

  public void passwordMatchChecker() {
    ImageView passwordMatchValidationIcon = (ImageView)findViewById(R.id.password2_validation_icon);
    TextView passwordMatchValidationText = (TextView)findViewById(R.id.password2_validation_text);

    if (checkPasswordMatch()) {
      passwordMatchValidationIcon.setImageResource(R.drawable.ic_checked);
      passwordMatchValidationText.setText(getString(R.string.matching_passwords));
      passwordMatchValidationText.setTextColor(getResources().getColor(R.color.green));
    } else {
      passwordMatchValidationIcon.setImageResource(R.drawable.ic_cancel);
      passwordMatchValidationText.setText(getString(R.string.not_matching_passwords));
      passwordMatchValidationText.setTextColor(getResources().getColor(R.color.red));
    }
  }

  public boolean checkPasswordMatch() {
    EditText passwordEditText = (EditText)findViewById(R.id.password_input);
    EditText passwordMatchEditText = (EditText)findViewById(R.id.password2_input);
    String password = passwordEditText.getText().toString();
    String password2 = passwordMatchEditText.getText().toString();

    return password.equals(password2);
  }
}