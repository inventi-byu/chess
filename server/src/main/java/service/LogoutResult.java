package service;

public class LogoutResult {
    private int status;

    public LogoutResult(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    @Override
    public String toString(){
        return ("LoginResult(status = " +
                status + ", message = \"\""
        );
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof LogoutResult){
            LogoutResult obj = (LogoutResult)o;
            if(this.getStatus() == obj.getStatus()){
                return true;
            }
        }
        return false;
    }
}
