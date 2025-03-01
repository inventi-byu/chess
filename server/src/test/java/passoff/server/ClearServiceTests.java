package java.passoff.server;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.RequestMethod;

public class ClearServiceTests {

    @Test
    public void clearDatabase(){
        ClearService clearservice = new ClearService();
        assert clearservice.clearDB(new ClearRequest(method= RequestMethod.DELETE)) == true;
    }
}
