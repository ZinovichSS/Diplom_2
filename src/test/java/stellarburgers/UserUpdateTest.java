package stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import stellarburgers.clients.AuthClient;
import stellarburgers.generators.UserGenerator;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserUpdateTest {
    private AuthClient restClient;
    private String fieldName;
    private User user;
    ValidatableResponse createUniqueUserResponse;
    private String createdUserAuth;

    public UserUpdateTest(String fieldName, User user){
        this.fieldName = fieldName;
        this.user = user;
    }

    @Parameterized.Parameters(name = "Редактирование поля: {0}")
    public static Object[][] createTestUserData() {
        return new Object[][]{
                {"name", new User("testName", null, null)},
                {"email", new User(null, "testEmail", null)},
                {"password", new User(null, null, "testPassword")}
        };
    }

    @Before
    public void setUp(){
        restClient = new AuthClient();
        createUniqueUserResponse = restClient.create(UserGenerator.getRandom());
        createdUserAuth = createUniqueUserResponse.extract().path("accessToken");
    }

    @After
    public void tearDown(){
        if( createdUserAuth != null ){
            restClient.delete(createdUserAuth);
        }
    }

    @Test
    @DisplayName("Обновление пользователя")
    @Description("Проверяется возможность обновления пользователя")
    public void userCanBeUpdatedWithAuthSuccess(){
        ValidatableResponse updateCreatedUserResponse = restClient.update(user, createdUserAuth);

        int statusCode = updateCreatedUserResponse.extract().statusCode();
        boolean responseSuccess = updateCreatedUserResponse.extract().path("success");

        assertEquals(200, statusCode);
        assertTrue(responseSuccess);
    }

    @Test
    @DisplayName("Обновление пользователя без авторизации")
    @Description("Проверяется возможность обновления пользователя")
    public void userCanBeUpdatedWithOutAuthFail(){
        ValidatableResponse updateCreatedUserResponse = restClient.update(user);

        int statusCode = updateCreatedUserResponse.extract().statusCode();
        boolean responseSuccess = updateCreatedUserResponse.extract().path("success");

        assertEquals(401, statusCode);
        assertFalse(responseSuccess);
    }
}
