package stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.clients.AuthClient;
import stellarburgers.generators.UserGenerator;
import stellarburgers.helpers.UserCredentials;


import static constants.Code.USER_SUCCESS_LOGIN;

import static org.junit.Assert.*;

public class UserLoginTest {
//    логин под существующим пользователем
//    логин с неверным логином и паролем

    ValidatableResponse createUniqueUserResponse;
    private AuthClient restClient;
    private User user;
    private String createdUserAuth;

    @Before
    public void setUp() {
        restClient = new AuthClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void tearDown() {
        if (createUniqueUserResponse != null) {
            createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
            if (createdUserAuth != null) {
                restClient.delete(createdUserAuth);
            }
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверяется возможность авторизации пользователя")
    public void userCanBeAuthorized() {
        createUniqueUserResponse = restClient.create(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = restClient.login(UserCredentials.from(user));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(USER_SUCCESS_LOGIN, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Проверяется гнвозможность авторизации пользователя с неверным логином")
    public void userAuthIncorrectLoginFail() {
        createUniqueUserResponse = restClient.create(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = restClient.login(UserCredentials.from(user.setEmail("INCORRECT")));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверяется гнвозможность авторизации пользователя с неверным паролем")
    public void userAuthIncorrectPasswordFail() {
        createUniqueUserResponse = restClient.create(user);
        createUniqueUserResponse.log().body();

        ValidatableResponse loginResponse = restClient.login(UserCredentials.from(user.setPassword("INCORRECT")));
        loginResponse.log().body();

        int statusCode = loginResponse.extract().statusCode();
        boolean responseSuccess = loginResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }
}
