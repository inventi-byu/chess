package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.service.exception.ResponseException;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class ServiceTests {
    private UserData userData;
    private String authToken;
    private UserService userService;
    private Service service;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();

        this.service = new Service(authDAO, userDAO, gameDAO);
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
    @DisplayName("Valid Authenticate With Credentials Call")
    public void testAuthenticateWithCredentialsGoodInput() {
        AuthData authData = this.service.authenticateWithCredentials(this.userData.username(), this.userData.password());
        Assertions.assertEquals(this.userData.username(), authData.username());
        Assertions.assertEquals(String.class, authData.authToken().getClass());
    }

    @Test
    @DisplayName("Invalid Authenticate With Credentials Call")
    public void testAuthenticateWithCredentialsBadInput() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> this.service.authenticateWithCredentials(this.userData.username(), "wrongpassword")
        );

        Assertions.assertEquals(401, exception.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("Valid Authenticate With Token Call")
    public void testAuthenticateWithTokenGoodInput() {
        AuthData authData = this.service.authenticateWithToken(this.authToken);
        Assertions.assertEquals(this.userData.username(), authData.username());
        Assertions.assertEquals(this.authToken, authData.authToken());

    }

    @Test
    @DisplayName("Invalid Authenticate With Token Call")
    public void testAuthenticateWithTokenBadInput() {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> this.service.authenticateWithToken("WrOnGtOkEn!")
        );

        Assertions.assertEquals(401, exception.getStatusCode());
        Assertions.assertEquals("Error: unauthorized", exception.getMessage());


    }

    @Test
    @DisplayName("Test that generateToken() generates a String")
    public void testGenerateToken() {
        Assertions.assertEquals(String.class, this.service.generateToken().getClass());
    }
}