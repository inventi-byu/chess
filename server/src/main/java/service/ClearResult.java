package service;

public class ClearResult {

    private int status;
    private String errorMessage;

    public ClearResult(int status, String errorMessage){
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus(){
        return this.status;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    @Override

    public String toString(){
        return ("ClearResult(status = " +
                status + ", errorMessage = " +
                errorMessage
                );
    }

}
