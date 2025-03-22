package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.service.exception.ResponseException;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class UserServiceTests {

    private UserService userService;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Valid Register Request")
    public void testRegisterGoodInput() {
        String username = "bobLikesChees";
        String password = "Wisconsin100";
        String email = "bob@bobisthebomb.com";
        UserData goodUser = new UserData(username, password, email);

        RegisterRequest request = new RegisterRequest(goodUser);
        RegisterResult result = this.userService.register(request);

        int expectedStatus = 200;
        Assertions.assertEquals(expectedStatus, result.getStatus());
        Assertions.assertEquals(username, result.getAuthData().username());
        Assertions.assertEquals(String.class, result.getAuthData().authToken().getClass());
    }

    @Test
    @DisplayName("Invalid Register Request")
    public void testRegisterBadInput() {
        String username = "forgetfulJoe";
        String password = null;
        String email = "joe@aol.com";
        UserData badRequestUser = new UserData(username, password, email);

        RegisterRequest badRequest = new RegisterRequest(badRequestUser);

        ResponseException badRequestException = Assertions.assertThrows(ResponseException.class, () -> this.userService.register(badRequest));
        Assertions.assertEquals(400, badRequestException.getStatusCode());
        Assertions.assertEquals("Error: bad request", badRequestException.getMessage());


        // Test Already Taken
        UserData anotherGoodUser = new UserData(username, "MyPassword!", email);
        this.userService.register(new RegisterRequest(anotherGoodUser));

        UserData takenUser = new UserData(username, "A different password", "joeMyOtherEmail@aol.com");
        RegisterRequest takenRequest = new RegisterRequest(takenUser);

        ResponseException takenException = Assertions.assertThrows(ResponseException.class, () -> this.userService.register(takenRequest));
        Assertions.assertEquals(403, takenException.getStatusCode());
        Assertions.assertEquals("Error: already taken", takenException.getMessage());
    }

}
