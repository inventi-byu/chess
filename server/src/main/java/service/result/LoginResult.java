package service.result;

import model.AuthData;

public class LoginResult {
    private int status;
    private AuthData authData;

    public LoginResult(int status, AuthData authData){
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
        return ("LoginResult(status = " +
                status + ", AuthData = " +
                this.getAuthData().toString()
        );
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof LoginResult){
            LoginResult obj = (LoginResult)o;
            if(this.getStatus() == obj.getStatus() && this.authData.equals(obj.authData)){
                return true;
            }
        }
        return false;
    }
}
