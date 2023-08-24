package stellarburgers.clients;

import groovy.util.logging.Log;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;
import stellarburgers.helpers.UserCredentials;

import static io.restassured.RestAssured.given;

public class AuthClient extends RestClient{

    private static final String CREATE_USER_PATH = "/api/auth/register";
    private static final String DELETE_USER_PATH = "/api/auth/user";
    private static final String UPDATE_USER_PATH = "/api/auth/user";
    private static final String LOGIN_USER_PATH = "/api/auth/login";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken){
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .delete(DELETE_USER_PATH)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_USER_PATH)
                .then();
    }

    @Step("Обновление данных пользователя")
    public ValidatableResponse update(User user, String accessToken){
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(UPDATE_USER_PATH)
                .then();
    }

    @Step("Обновление данных пользователя без авторизации")
    public ValidatableResponse update(User user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(UPDATE_USER_PATH)
                .then();
    }
}
