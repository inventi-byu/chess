package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogoutServiceTests {

    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.logoutService = new LogoutService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Valid Logout Request")
    public void testLogoutGoodInput() {


    }

    @Test
    @DisplayName("Invalid Logout Request")
    public void testLogoutBadInput() {


    }


}