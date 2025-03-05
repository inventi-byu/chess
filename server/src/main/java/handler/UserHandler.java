package handler;

import model.UserData;
import service.UserService;
import service.request.RegisterRequest;
import service.result.RegisterResult;
import service.exception.ResponseException;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class UserHandler extends Handler {

    private UserService userService;

    public UserHandler(UserService userService){
        this.userService = userService;
    }

    /**
     * Handles a register request via the user service.
     * @param req the Spark request from the client
     * @param res the Spark response to send back to the client
     * @return A JSON string of the AuthData returned from the register service.
     * @throws ResponseException if registration failed.
     */
    public String handleRegister(Request req, Response res) throws ResponseException {
        UserData userdata = new Gson().fromJson(req.body(), UserData.class);
        RegisterResult result = this.userService.register(new RegisterRequest(userdata));
        res.status(result.getStatus());
        return new Gson().toJson(result.getAuthData()); // Return the body
    };
}
