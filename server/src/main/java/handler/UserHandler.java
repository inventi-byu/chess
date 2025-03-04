package handler;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class UserHandler {
    public UserHandler(){}

    public Response handleRegister(Request req, Response res){
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        UserService service = new UserService();
        RegisterResult result = service.register(request);
        res.status(result.getStatusCode);
        res.body(new Gson().toJson(result.getAuthData));
        return res;
    };
}
