package handler;

import service.LoginRequest;
import service.LoginResult;
import service.LoginService;
import service.ResponseException;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class SessionHandler {
    public SessionHandler(){}

    /**
     * Handles a clear request from http coming from the server endpoint
     * @param req the request as a Spark request.
     */
    public String handleLogin(Request req, Response res) throws ResponseException {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginService service = new LoginService();
        LoginResult result = service.login(request);
        res.status(result.getStatus());
        return new Gson().toJson(result.getAuthData());
    }
}
