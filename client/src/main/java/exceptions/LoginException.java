package exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class LoginException extends Exception {
    private final int statusCode;

    public LoginException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson(){
        return new Gson().toJson(Map.of("message", this.getMessage()));
    }

    public int getStatusCode(){
        return this.statusCode;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof LoginException){
            LoginException obj = (LoginException) o;
            if(this.getStatusCode() == obj.getStatusCode() && this.getMessage().equals(obj.getMessage())){
                return true;
            }
        }
        return false;
    }
}
