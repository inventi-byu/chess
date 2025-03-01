package java.service;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.RequestMethod;
import service.ClearRequest;
import service.ClearResult;

public class ClearServiceTests {
    @Test
    public void testClearDatabase(){
        ClearService clearservice = new ClearService();
        ClearResult result = clearservice.clear(new ClearRequest("delete"));
        Assertions.assertEquals(new ClearResult(200, ""), result);
    }
}
