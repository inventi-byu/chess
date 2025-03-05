package service.result;

public class ClearResult {

    private int status;

    public ClearResult(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    @Override
    public String toString(){
        return ("ClearResult(status = " +
                status
                );
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ClearResult){
            ClearResult obj = (ClearResult)o;
            if(this.getStatus() == obj.getStatus()){
                return true;
            }
        }
        return false;
    }

}
