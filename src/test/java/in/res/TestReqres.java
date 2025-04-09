package in.res;

import in.res.dto.response.AuthResponse;
import in.res.dto.response.ResourceResponse;
import in.res.dto.response.UserResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static in.res.config.Specification.requestSpecification;
import static in.res.config.Specification.responseSpecification200Code;
import static in.res.config.Specification.responseSpecification204Code;
import static in.res.config.Specification.responseSpecification400Code;
import static in.res.uril.TestConstants.ERROR_AUTH_REQUEST;
import static in.res.uril.TestConstants.SUCCESS_AUTH_REQUEST;
import static in.res.uril.TestConstants.SUCCESS_AUTH_RESPONSE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestReqres {
    @Test
    @DisplayName("Проверка о совпадении автаров и имен файлов, проверка формата email")
    public void checkUsers() {
        List<UserResponse> users = given()
                .spec(requestSpecification)
                .when()
                .get("/api/users?page=2")
                .then()
                .spec(responseSpecification200Code)
                .extract()
                .body()
                .jsonPath()
                .getList("data", UserResponse.class);
        assertAll(
                "",
                () -> assertTrue(users.stream().allMatch(user -> user.email().endsWith("reqres.in"))),
                () -> {
                    for (UserResponse user : users) {
                        assertTrue(user.avatar().contains(String.valueOf(user.id())));
                    }
                }
        );
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    public void checkSignUp() {
        AuthResponse response = given()
                .spec(requestSpecification)
                .body(SUCCESS_AUTH_REQUEST)
                .when()
                .post("/api/register")
                .then()
                .spec(responseSpecification200Code)
                .extract()
                .as(AuthResponse.class);
        assertAll(
                "",
                () -> assertNotNull(response.id()),
                () -> assertNotNull(response.token()),
                () -> assertEquals(SUCCESS_AUTH_RESPONSE, response)
        );
    }

    @Test
    @DisplayName("Проверка неуспешной регистрации")
    public void checkUnsuccessfulSignUp() {
        Response response = given()
                .body(ERROR_AUTH_REQUEST)
                .spec(requestSpecification)
                .when()
                .post("/api/register")
                .then()
                .spec(responseSpecification400Code)
                .extract()
                .response();
        String error = response.jsonPath().getString("error");
        assertEquals("Missing email or username", error);
    }

    @Test
    @DisplayName("Проверка порядка ресурсов по возрастанию года")
    public void checkListResource() {
        List<ResourceResponse> resources = given()
                .spec(requestSpecification)
                .when()
                .get("api/unknown")
                .then()
                .spec(responseSpecification200Code)
                .extract()
                .body()
                .jsonPath()
                .getList("data", ResourceResponse.class);
        List<Integer> actualYears = resources.stream().map(ResourceResponse::year).toList();
        List<Integer> expectedYears = actualYears.stream().sorted().toList();
        assertAll(
                "",
                () -> assertNotNull(actualYears),
                () -> assertEquals(expectedYears, actualYears)
        );
    }

    @Test
    @DisplayName("Проверка кода ответа при удалении пользователя")
    public void checkDeleteUser() {
        given()
                .spec(requestSpecification)
                .when()
                .delete("/api/users/2")
                .then()
                .spec(responseSpecification204Code);
    }
}
