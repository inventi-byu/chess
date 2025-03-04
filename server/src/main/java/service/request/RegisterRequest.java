package service.request;

import model.UserData;

public class RegisterRequest {
    private UserData userData;

    public RegisterRequest(UserData userData){
        this.userData = userData;
    }

    public UserData getUserData(){return this.userData;}

}
