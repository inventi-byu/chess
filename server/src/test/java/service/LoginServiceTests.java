package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.exception.ResponseException;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
import service.result.RegisterResult;

public class LoginServiceTests {

    private UserData userData;
    private LoginService loginService;
    private LogoutService logoutService;
    private UserService userService;


    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        this.loginService = new LoginService(authDAO, userDAO, gameDAO);
        this.logoutService = new LogoutService(authDAO, userDAO, gameDAO);
        this.userService = new UserService(authDAO, userDAO, gameDAO);

        // Register the user then log them out to login
        String username = "bobLikesChees";
        String password = "Wisconsin100";
        String email = "bob@bobisthebomb.com";
        this.userData = new UserData(username, password, email);

        RegisterResult result = this.userService.register(new RegisterRequest(this.userData));
        LogoutResult logoutResult  = this.logoutService.logout(new LogoutRequest(result.getAuthData().authToken()));

    }

    @Test
    @DisplayName("Valid Login Request")
    public void testLoginGoodInput() {
        LoginResult result = this.loginService.login(new LoginRequest(
                this.userData.username(),
                this.userData.password())
        );

        Assertions.assertEquals(200, result.getStatus());
        Assertions.assertEquals(String.class, result.getAuthData().authToken().getClass());
    }

    @Test
    @DisplayName("Invalid Login Request")
    public void testLoginBadInput() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () ->this.loginService.login(new LoginRequest(
                "fakeUser",
                "fakepassword"))
        );

        Assertions.assertEquals(401, exception.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());

    }
}