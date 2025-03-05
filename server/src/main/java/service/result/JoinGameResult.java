package service.result;

public class JoinGameResult {

    private int status;

    public JoinGameResult(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof JoinGameResult){
            JoinGameResult obj = (JoinGameResult)o;
            if(this.getStatus() == obj.getStatus()){
                return true;
            }
        }
        return false;
    }
}
