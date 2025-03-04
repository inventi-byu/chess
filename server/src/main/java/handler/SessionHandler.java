package handler;

import service.*;
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
        String authToken = req.headers("authorization");
        if(authToken.isEmpty()){
            throw new ResponseException(401, "Error: not authorized");
        }
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResult result = this.logoutService.logout(request);

        // Returns nothing but 200 status if working properly
        res.status(result.getStatus());

        return "";
    }
}
