package service;

public class ClearResult {

    private int status;
    private String message;

    public ClearResult(int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus(){
        return this.status;
    }

    public String getMessage(){
        return this.message;
    }

    @Override

    public String toString(){
        return ("ClearResult(status = " +
                status + ", message = " +
                message
                );
    }

}
