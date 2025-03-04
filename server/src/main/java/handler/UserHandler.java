package handler;

import model.UserData;
import service.RegisterRequest;
import service.ResponseException;
import service.UserService;
import service.RegisterResult;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class UserHandler {
    public UserHandler(){}

    public Response handleRegister(Request req, Response res) throws ResponseException {
        UserData userdata = new Gson().fromJson(req.body(), UserData.class);
        UserService service = new UserService();
        RegisterResult result = service.register(new RegisterRequest(userdata));
        res.status(result.getStatusCode());
        res.body(new Gson().toJson(result.getAuthData()));
        return res;
    };
}
