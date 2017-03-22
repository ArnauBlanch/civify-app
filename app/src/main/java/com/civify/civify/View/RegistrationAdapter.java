package com.civify.civify.View;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegistrationAdapter extends FragmentPagerAdapter {
  private int currentPosition;

  public RegistrationAdapter(FragmentManager fm) {
    super(fm);
    currentPosition = 0;
  }

  @Override
  public Fragment getItem(int position) {
    currentPosition = position;
    return RegistrationFragment.newInstance(position);
  }

  @Override
  public int getCount() {
    return 4;
  }
}