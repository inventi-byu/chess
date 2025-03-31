package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String message;

    public NotificationMessage(String message){
        super(ServerMessageType.ERROR);
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    @Override
    public boolean equals(Object o) {
        if(super.equals(o)){
            if (this == o) {
                return true;
            }
            if (!(o instanceof NotificationMessage)) {
                return false;
            }
            NotificationMessage that = (NotificationMessage) o;
            return this.getServerMessageType() == that.getServerMessageType() && this.getMessage().equals(that.getMessage());
        }
        return false;
    }

    @Override
    public String toString(){
        return "NotificationMessage(" + this.getMessage() + ")";
    }
}
