package handler;

import service.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class SessionHandler {

    private LoginService loginService;
    private LogoutService logoutService;

    public SessionHandler(LoginService loginService, LogoutService logoutService){
        this.loginService = loginService;
        this.logoutService = logoutService;
    }

    /**
     * Handles a clear request from http coming from the server endpoint
     * @param req the request as a Spark request.
     */
    public String handleLogin(Request req, Response res) throws ResponseException {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult result = this.loginService.login(request);
        res.status(result.getStatus());
        return new Gson().toJson(result.getAuthData());
    }

    public String handleLogout(Request req, Response res) throws ResponseException {
        LogoutRequest request = new Gson().fromJson(req.body(), LogoutRequest.class);
        LogoutResult result = this.logoutService.logout(request);
        // Returns nothing but 200 status if working properly
        res.status(result.getStatus());
        return "";
    }
}
