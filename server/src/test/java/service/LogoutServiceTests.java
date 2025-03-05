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
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LogoutResult;
import service.result.RegisterResult;

public class LogoutServiceTests {

    private UserData userData;
    private String authToken;
    private UserService userService;
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        this.logoutService = new LogoutService(authDAO, userDAO, gameDAO);
        this.userService = new UserService(authDAO, userDAO, gameDAO);

        // Register the user then log them out to login
        String username = "bobLikesCheese";
        String password = "Wisconsin100";
        String email = "bob@bobisthebomb.com";
        this.userData = new UserData(username, password, email);

        RegisterResult result = this.userService.register(new RegisterRequest(this.userData));
        this.authToken = result.getAuthData().authToken();
    }

    @Test
    @DisplayName("Valid Logout Request")
    public void testLogoutGoodInput() {
        LogoutResult logoutResult  = this.logoutService.logout(new LogoutRequest(this.authToken));
        Assertions.assertEquals(new LogoutResult(200), logoutResult);

    }

    @Test
    @DisplayName("Invalid Logout Request")
    public void testLogoutBadInput() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class, () -> this.logoutService.logout(
                new LogoutRequest("superFakeAuthToken"))
        );

        Assertions.assertEquals(401, exception.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());

    }
}