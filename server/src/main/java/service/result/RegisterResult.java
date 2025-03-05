package service.result;

import model.AuthData;

public class RegisterResult {

    private int status;
    private AuthData authData;

    public RegisterResult(int status, AuthData authData){
        this.status = status;
        this.authData = authData;
    }

    public int getStatus(){
        return this.status;
    }

    public AuthData getAuthData(){
        return this.authData;
    }

    @Override
    public String toString(){
        return ("ClearResult(status = " +
                status + ", AuthData = " +
                this.getAuthData().toString()
        );
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof RegisterResult){
            RegisterResult obj = (RegisterResult)o;
            if(this.getStatus() == obj.getStatus() && this.authData.equals(obj.authData)){
                return true;
            }
        }
        return false;
    }

}

