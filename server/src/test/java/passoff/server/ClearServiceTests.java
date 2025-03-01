package java.passoff.server;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.RequestMethod;
import service.ClearRequest;
import service.ClearResult;

public class ClearServiceTests {

    @Test
    public void clearDatabase(){
        ClearService clearservice = new ClearService();
        ClearResult result = clearservice.clear(new ClearRequest(RequestMethod.DELETE));
        assert result.equals(new ClearResult(200, ""));
    }
}
