package exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class ServerFacadeException extends Exception {
    private final int statusCode;

    public ServerFacadeException(int statusCode, String message) {
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
        if (o instanceof ServerFacadeException){
            ServerFacadeException obj = (ServerFacadeException) o;
            if(this.getStatusCode() == obj.getStatusCode() && this.getMessage().equals(obj.getMessage())){
                return true;
            }
        }
        return false;
    }
}
