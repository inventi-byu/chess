package handler;

import service.*;
import service.exception.ResponseException;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class SessionHandler extends Handler {

    private LoginService loginService;
    private LogoutService logoutService;

    public SessionHandler(LoginService loginService, LogoutService logoutService){
        this.loginService = loginService;
        this.logoutService = logoutService;
    }

    /**
     * Handles a login request via the login service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return A JSON string of the AuthData returned from the login service.
     */
    public String handleLogin(Request req, Response res) throws ResponseException {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = this.loginService.login(request);
        res.status(result.getStatus());
        return new Gson().toJson(result.getAuthData());
    }

    /**
     * Handles a logout request via the login service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return an empty string (200 success response).
     */
    public String handleLogout(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResult result = this.logoutService.logout(request);

        // Returns nothing but 200 status if working properly
        res.status(result.getStatus());

        return "";
    }
}
