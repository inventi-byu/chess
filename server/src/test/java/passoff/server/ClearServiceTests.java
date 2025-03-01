package java.passoff.server;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.RequestMethod;
import service.ClearRequest;
import service.ClearResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearServiceTests {


    @Test
    public void clearDatabaseTest(){
        ClearService clearservice = new ClearService();
        ClearResult result = clearservice.clear(new ClearRequest(RequestMethod.DELETE));
        assertEquals(new ClearResult(200, ""), result);
    }
}
