package com.civify.civify.ControllerTest;


import android.util.Log;

import com.civify.civify.Controller.CivifyLoginService;
import com.civify.civify.Controller.ILoginAdapter;
import com.civify.civify.Controller.LoginFinishedCallback;
import com.civify.civify.Controller.ServiceGenerator;
import com.civify.civify.Model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import retrofit2.mock.Calls;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/*
Using Fake Call stub instead of a mock (retrofit2.mock.Calls)
Using PowerMock to mockStatic Log
 */

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest({ILoginAdapter.class,
        User.class,
        ServiceGenerator.class,
        Log.class})

public class ILoginAdapterTest {

    @Mock User user;
    @Mock ServiceGenerator serviceGenerator;
    @InjectMocks  private ILoginAdapter iLoginAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(Log.class);
    }

    @After
    public void tearDown() {
        iLoginAdapter = null;
    }


    @Test
    public void loginSuccededReturnedWhenCorrectCallEmailAndPassword() {

        when(serviceGenerator.createService(CivifyLoginService.class, "email", "contrasenya"))
                .thenReturn(() -> Calls.response(user));
        iLoginAdapter.login("email", "contrasenya", new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {
                assertNotNull(u);
            }
            @Override
            public void onLoginFailed(Throwable t) {
                assertNull(t);
            }
        });

    }

   @Test
    public void loginFailedReturnedWhenFailureCall() {

        when(serviceGenerator.createService(CivifyLoginService.class, "email", "contrasenya"))
               .thenReturn(() -> Calls.failure(new IOException("Network Exploded")));

        iLoginAdapter.login("email", "contrasenya", new LoginFinishedCallback() {
           @Override
           public void onLoginSucceeded(User u) {
               assertNull(u);
           }

           @Override
           public void onLoginFailed(Throwable t) {
               assertNotNull(t);
               assertEquals("Network Exploded", t.getMessage());
           }
        });
   }
}
