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

import static constants.Code.*;
import static org.junit.Assert.assertEquals;


public class UserCreateTest {
    private AuthClient restClient;
    private User user;
    ValidatableResponse createUniqueUserResponse;
    private String createdUserAuth;

    @Before
    public void setUp(){
        restClient = new AuthClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void tearDown(){
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
        if( createdUserAuth != null ){
            restClient.delete(createdUserAuth);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяется возможность создания пользователя")
    public void userCanBeCreatedSuccess(){
        createUniqueUserResponse = restClient.create(user);
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");

        assertEquals(USER_CREATED_CODE, statusCode);
        assertEquals(true, responseSuccess);
    }


    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Проверяется невозможность создания двух одинаковых пользователей")
    public void createTwoEqualUsersFail(){

        createUniqueUserResponse = restClient.create(user);
        ValidatableResponse createExistedUserResponse = restClient.create(user);

        createUniqueUserResponse.log().body();
        createExistedUserResponse.log().body();

        int statusCode = createExistedUserResponse.extract().statusCode();
        String responseMessage = createExistedUserResponse.extract().path("message");
        boolean responseSuccess = createExistedUserResponse.extract().path("success");

        assertEquals(USER_EXISTS_CODE, statusCode);
        assertEquals("User already exists", responseMessage);
        assertEquals(false, responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutNameField(){
        createUniqueUserResponse = restClient.create(UserGenerator.getWithoutField("name"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertEquals(false, responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutEmailField(){
        createUniqueUserResponse = restClient.create(UserGenerator.getWithoutField("email"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertEquals(false, responseSuccess);
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    @Description("Проверяется невозможность создания пользователя без обязательного поля")
    public void userCanNotBeCratedWithoutPasswordField(){
        createUniqueUserResponse = restClient.create(UserGenerator.getWithoutField("password"));
        createUniqueUserResponse.log().body();

        int statusCode = createUniqueUserResponse.extract().statusCode();
        boolean responseSuccess = createUniqueUserResponse.extract().path("success");

        assertEquals(USER_REQUIRED_FIELD_EMPTY, statusCode);
        assertEquals(false, responseSuccess);
    }

}
