package com.civify.civify.Utils;

import com.civify.civify.Model.Service;
import com.civify.civify.Model.UserService;

public class ServiceLocator {
  private static ServiceLocator mInstance;

  private UserService mUserService;

  public static ServiceLocator getInstance() {
    if (mInstance == null)
      mInstance = new ServiceLocator();
    return mInstance;
  }

  public Service find(String serviceName) throws Exception {
    switch (serviceName) {
      case "UserService":
        if (mUserService == null)
          mUserService = new UserService();
        return mUserService;

      default:
        throw new Exception("Service doesn't exist");
    }
  }

}
