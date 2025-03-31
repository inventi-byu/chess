package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if(super.equals(o)){
            if (this == o) {
                return true;
            }
            if (!(o instanceof ErrorMessage)) {
                return false;
            }
            ErrorMessage that = (ErrorMessage) o;
            return this.getServerMessageType() == that.getServerMessageType() && this.getErrorMessage().equals(that.getErrorMessage());
        }
        return false;
    }

    @Override
    public String toString(){
        return "ErrorMessage(" + this.getErrorMessage() + ")";
    }
}
