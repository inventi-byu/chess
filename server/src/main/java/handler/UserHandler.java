package handler;

import model.UserData;
import service.request.RegisterRequest;
import service.ResponseException;
import service.UserService;
import service.result.RegisterResult;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class UserHandler {

    private UserService userService;

    public UserHandler(UserService userService){
        this.userService = userService;
    }

    public String handleRegister(Request req, Response res) throws ResponseException {
        UserData userdata = new Gson().fromJson(req.body(), UserData.class);
        RegisterResult result = this.userService.register(new RegisterRequest(userdata));
        res.status(result.getStatusCode());
        return new Gson().toJson(result.getAuthData()); // Return the body
    };
}
