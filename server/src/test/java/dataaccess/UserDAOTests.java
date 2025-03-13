package dataaccess;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;
import service.exception.ResponseException;
import service.request.RegisterRequest;
import service.result.RegisterResult;

// TODO: Change this code so it is not the same as UserService Tests

public class UserDAOTests {

    @BeforeEach
    void setup(){

    }

    @Test
    public void createUserTestGoodInput(){
        throw new RuntimeException("Not implemented.");
    }
    public void createUserTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }

    public void getUserTestGoodInput(){
        throw new RuntimeException("Not implemented.");
    }

    public void getUserTestBadInput(){
        throw new RuntimeException("Not implemented.");
    }
}
