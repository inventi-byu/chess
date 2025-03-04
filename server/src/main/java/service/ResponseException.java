package service;
import com.google.gson.*;

import java.util.Map;

public class ResponseException extends RuntimeException {

    private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
    }

    public String toJson(){
        return new Gson().toJson(Map.of("status", this.getStatusCode(), "message", this.getMessage()));
    }

    public int getStatusCode(){
        return this.statusCode;
    }
}
