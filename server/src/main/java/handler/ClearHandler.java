package handler;

import service.request.ClearRequest;
import service.result.ClearResult;
import service.ClearService;
import service.exception.ResponseException;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {

    private ClearService clearService;

    public ClearHandler(ClearService clearService){
        this.clearService = clearService;
    }

    /**
     * Handles a clear request from http coming from the server endpoint
     * @param req the request as a Spark request.
     */
    public String handle(Request req, Response res) throws ResponseException  {
        ClearRequest request = new ClearRequest(req.requestMethod()); // There may be nothing in the body to convert.
        ClearResult result = this.clearService.clear(request);

        // No message for successful clear requests
        res.status(result.getStatus());

        return "";
    }
}
