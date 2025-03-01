package handler;

import com.google.gson.Gson;
import service.ClearRequest;
import service.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public ClearHandler(){

    }

    /**
     * Handles a clear request from http coming from the server endpoint
     * @param req the request as a Spark request.
     */
    public Response handle(Request req, Response res) {
        /*
        Convert json to a request
        call the service function and get the result, convert the result into a json, and return that.
         */
        ClearRequest request = new Gson().fromJson(req.body(), ClearRequest.class); // There may be nothing in the body to convert.
        ClearService service = new ClearService();
        ClearResult result = service.clear(request);
        res.status(result.getStatus());
        String message = result.getMessage();
        if (!message.isEmpty()){
            res.header("message", message);
            // TODO: Should I put that in the body rather than a header?
        }
        return res;

    }


}
