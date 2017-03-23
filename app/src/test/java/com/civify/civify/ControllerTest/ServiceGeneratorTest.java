package com.civify.civify.ControllerTest;


import com.civify.civify.Controller.CivifyLoginService;
import com.civify.civify.Controller.ServiceGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;

import static junit.framework.Assert.assertNotNull;

/*
Mockito can't mock final classes. To test without ServiceGenerator dependences
 */


public class ServiceGeneratorTest {

    private ServiceGenerator serviceGenerator;

    @Before
    public void setUp() {
        serviceGenerator = ServiceGenerator.getInstance();

    }

    @After
    public void shutdown() {
        serviceGenerator = null;
    }

    @Test
    public void serviceGeneratedWhenNoCredentials() {
        assertNotNull(serviceGenerator);
        CivifyLoginService civifyLoginService = serviceGenerator.createService(CivifyLoginService.class);
        assertNotNull(civifyLoginService);

    }

    @Test
    public void serviceGeneratedWithCredentials() {
        CivifyLoginService civifyLoginService =
                serviceGenerator.createService(CivifyLoginService.class, "user", "password");
        assertNotNull(civifyLoginService);
    }

    @Test
    public void instanceGeneratesCorrectly() {
        ServiceGenerator serviceGenerator1 = ServiceGenerator.getInstance();
        assertNotNull(serviceGenerator1);
    }
}
