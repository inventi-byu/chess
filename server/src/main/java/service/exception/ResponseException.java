package service.exception;
import com.google.gson.*;

import java.util.Map;

public class ResponseException extends RuntimeException {

    private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message", this.getMessage()));
    }

    public int getStatusCode(){
        return this.statusCode;
    }
}
