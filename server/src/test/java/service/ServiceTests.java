package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ServiceTests {
    private Service service;

    @BeforeEach
    void setUp() {
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        this.service = new Service(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Valid Authenticate With Credentials Call")
    public void testAuthenticateWithCredentialsGoodInput() {

    }

    @Test
    @DisplayName("Invalid Authenticate With Credentials Call")
    public void testAuthenticateWithCredentialsBadInput() {


    }

    @Test
    @DisplayName("Valid Authenticate With Token Call")
    public void testAuthenticateWithTokenGoodInput() {


    }

    @Test
    @DisplayName("Invalid Authenticate With Token Call")
    public void testAuthenticateWithTokenBadInput() {


    }
}