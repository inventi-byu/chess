package server.service.exception;
import com.google.gson.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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

    public static ResponseException fromJson(InputStream stream){
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        String message = map.get("message").toString();
        int status = 500;
        switch (message){
            case "Error: bad request" -> status = 400;
            case "Error: already taken" -> status = 403;
            case "Error: unauthorized" -> status = 401;
        }
        return new ResponseException(status, message);
    }

    public int getStatusCode(){
        return this.statusCode;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ResponseException){
            ResponseException obj = (ResponseException) o;
            if(this.getStatusCode() == obj.getStatusCode() && this.getMessage().equals(obj.getMessage())){
                return true;
            }
        }
        return false;
    }
}
