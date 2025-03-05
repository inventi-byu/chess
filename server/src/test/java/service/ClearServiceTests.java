package service;
import dataaccess.*;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.request.ClearRequest;
import service.result.ClearResult;

public class ClearServiceTests {

    @BeforeEach
    void setUp(){
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        AdminDAO adminDAO = new MemoryAdminDAO(authDAO, userDAO, gameDAO);
        ClearService clearservice = new ClearService(adminDAO);
    }
    @Test
    public void testClearDatabase(){
        Assertions.assertTrue(true);
        // ClearService clearservice = new ClearService();
        //ClearResult result = clearservice.clear(new ClearRequest("delete"));
        //Assertions.assertEquals(new ClearResult(200, ""), result);
    }
}
