package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginServiceTests {

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.loginService = new LoginService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Valid Login Request")
    public void testLoginGoodInput() {


    }

    @Test
    @DisplayName("Invalid Login Request")
    public void testLoginBadInput() {


    }
}